<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { buscarAtivosPorContrato } from '@/services/AtivoService'
import type { IAtivo } from '@/services/AtivoService'
import { buscarContratos } from '@/services/ContratoService'
import type { IContrato } from '@/services/ContratoService'
import { buscarSupervisoesPorContrato } from '@/services/SupervisaoService'
import type { ISupervisao } from '@/services/SupervisaoService'
import { computed, onMounted, ref, watch } from 'vue'

type AtivoEnriquecido = IAtivo & { contratoId: number; contratoNome: string }

const isLoading = ref(false)
const errorMessage = ref('')

const contratos = ref<IContrato[]>([])
const todosAtivos = ref<AtivoEnriquecido[]>([])
const supervisoes = ref<ISupervisao[]>([])

const filtroContrato = ref<number | ''>('')
const filtroSupervisao = ref<number | ''>('')
const busca = ref('')

const ativosFiltrados = computed(() => {
  let lista = todosAtivos.value

  if (filtroContrato.value !== '') {
    lista = lista.filter((a) => a.contratoId === filtroContrato.value)
  }

  if (filtroSupervisao.value !== '') {
    const sup = supervisoes.value.find((s) => s.id === filtroSupervisao.value)
    if (sup?.plantas?.length) {
      const nomesPlanta = sup.plantas.map((p) => p.nome.toLowerCase())
      lista = lista.filter((a) => a.planta && nomesPlanta.includes(a.planta.toLowerCase()))
    }
  }

  if (busca.value.trim()) {
    const q = busca.value.trim().toLowerCase()
    lista = lista.filter(
      (a) =>
        a.nome?.toLowerCase().includes(q) ||
        a.tipo?.toLowerCase().includes(q) ||
        a.fabricante?.toLowerCase().includes(q) ||
        a.planta?.toLowerCase().includes(q),
    )
  }

  return lista
})

const metricas = computed(() => {
  const lista = ativosFiltrados.value
  const ativos = lista.filter((a) => a.status?.toLowerCase() === 'ativo').length
  const inativos = lista.filter((a) => a.status?.toLowerCase() === 'inativo').length
  const manutencao = lista.filter((a) => a.status?.toLowerCase() === 'manutenção' || a.status?.toLowerCase() === 'manutencao').length
  return [
    { label: 'Total de Ativos', value: lista.length, tone: 'blue' as const },
    { label: 'Ativos', value: ativos, tone: 'green' as const },
    { label: 'Em Manutenção', value: manutencao, tone: 'amber' as const },
    { label: 'Inativos', value: inativos, tone: 'red' as const },
  ]
})

onMounted(async () => {
  isLoading.value = true
  try {
    const listaContratos = await buscarContratos()
    contratos.value = listaContratos

    const resultados = await Promise.allSettled(
      listaContratos.map((c) =>
        buscarAtivosPorContrato(c.id).then((ativos) =>
          ativos.map((a) => ({ ...a, contratoId: c.id, contratoNome: c.nomeEmpresa })),
        ),
      ),
    )

    todosAtivos.value = resultados
      .filter((r): r is PromiseFulfilledResult<AtivoEnriquecido[]> => r.status === 'fulfilled')
      .flatMap((r) => r.value)
  } catch (e) {
    errorMessage.value = 'Erro ao carregar ativos. Verifique se o backend está rodando.'
    console.error(e)
  } finally {
    isLoading.value = false
  }
})

watch(filtroContrato, async (novoId) => {
  filtroSupervisao.value = ''
  supervisoes.value = []
  if (novoId !== '') {
    try {
      supervisoes.value = await buscarSupervisoesPorContrato(Number(novoId))
    } catch (e) {
      console.error(e)
    }
  }
})

function formatarData(iso: string | null | undefined): string {
  if (!iso) return '—'
  try {
    return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(new Date(`${iso}T00:00:00`))
  } catch {
    return '—'
  }
}

function limparFiltros() {
  filtroContrato.value = ''
  filtroSupervisao.value = ''
  busca.value = ''
}
</script>

<template>
  <div class="adm-layout">
    <AppHeaderRoles />

    <div class="adm-workspace">
      <SidebarAdm />

      <main class="adm-main">
        <header class="topbar">
          <div>
            <span class="topbar-title">Ativos</span>
            <span class="topbar-sub">Visualização e gestão de todos os ativos</span>
          </div>
        </header>

        <section class="content-area">
          <!-- Métricas -->
          <section class="metrics-grid" aria-label="Indicadores de ativos">
            <article v-for="m in metricas" :key="m.label" class="metric-card" :class="m.tone">
              <div class="metric-label">{{ m.label }}</div>
              <div class="metric-value">{{ m.value }}</div>
            </article>
          </section>

          <!-- Filtros + Tabela -->
          <section class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Todos os Ativos</div>
                <div class="card-sub">
                  {{
                    isLoading
                      ? 'Carregando...'
                      : ativosFiltrados.length === 0
                        ? 'Nenhum ativo encontrado'
                        : `${ativosFiltrados.length} ativo(s)`
                  }}
                </div>
              </div>

              <div class="filter-bar">
                <!-- Busca por texto -->
                <div class="search-input">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="11" cy="11" r="8" />
                    <line x1="21" y1="21" x2="16.65" y2="16.65" />
                  </svg>
                  <input
                    v-model="busca"
                    type="text"
                    placeholder="Buscar por nome, tipo, fabricante..."
                  />
                </div>

                <!-- Filtro por Contrato -->
                <div class="select-wrap">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <rect x="2" y="3" width="20" height="4" rx="1"/>
                    <path d="M6 11h12M9 17h6"/>
                  </svg>
                  <select v-model="filtroContrato">
                    <option value="">Todos os contratos</option>
                    <option v-for="c in contratos" :key="c.id" :value="c.id">
                      {{ c.nomeEmpresa }}
                    </option>
                  </select>
                </div>

                <!-- Filtro por Supervisão (habilitado só quando contrato selecionado) -->
                <div class="select-wrap" :class="{ disabled: filtroContrato === '' }">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
                    <circle cx="9" cy="7" r="4"/>
                    <path d="M23 21v-2a4 4 0 0 0-3-3.87M16 3.13a4 4 0 0 1 0 7.75"/>
                  </svg>
                  <select v-model="filtroSupervisao" :disabled="filtroContrato === ''">
                    <option value="">
                      {{ filtroContrato === '' ? 'Selecione um contrato' : 'Todas as supervisões' }}
                    </option>
                    <option v-for="s in supervisoes" :key="s.id" :value="s.id">
                      {{ s.nome }}
                    </option>
                  </select>
                </div>

                <!-- Limpar filtros -->
                <button
                  v-if="filtroContrato !== '' || filtroSupervisao !== '' || busca.trim()"
                  class="btn-clear"
                  type="button"
                  @click="limparFiltros"
                  title="Limpar filtros"
                >
                  <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <line x1="18" y1="6" x2="6" y2="18"/>
                    <line x1="6" y1="6" x2="18" y2="18"/>
                  </svg>
                  Limpar
                </button>
              </div>
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Status</th>
                    <th>Fabricante</th>
                    <th>Planta</th>
                    <th>Prédio</th>
                    <th>Periodicidade</th>
                    <th>Instalação</th>
                    <th>Contrato</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="isLoading">
                    <td colspan="9" class="table-empty">Carregando ativos...</td>
                  </tr>
                  <tr v-else-if="errorMessage && todosAtivos.length === 0">
                    <td colspan="9" class="table-empty table-error">{{ errorMessage }}</td>
                  </tr>
                  <tr v-else-if="ativosFiltrados.length === 0">
                    <td colspan="9" class="table-empty">
                      Nenhum ativo encontrado com os filtros aplicados.
                    </td>
                  </tr>
                  <template v-else>
                    <tr v-for="a in ativosFiltrados" :key="`${a.contratoId}-${a.id}`">
                      <td><strong class="table-title">{{ a.nome || '—' }}</strong></td>
                      <td>{{ a.tipo || '—' }}</td>
                      <td>
                        <span
                          class="badge"
                          :class="{
                            'badge-active': a.status?.toLowerCase() === 'ativo',
                            'badge-pending': a.status?.toLowerCase() === 'manutenção' || a.status?.toLowerCase() === 'manutencao',
                            'badge-done': a.status?.toLowerCase() === 'inativo',
                          }"
                        >
                          {{ a.status || '—' }}
                        </span>
                      </td>
                      <td>{{ a.fabricante || '—' }}</td>
                      <td>{{ a.planta || '—' }}</td>
                      <td>{{ a.predio || '—' }}</td>
                      <td>
                        {{ a.periodicidadeManutencao ? `${a.periodicidadeManutencao} dias` : '—' }}
                      </td>
                      <td>{{ formatarData(a.dataInstalacao) }}</td>
                      <td>
                        <span class="contrato-tag">{{ a.contratoNome }}</span>
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>
          </section>
        </section>
      </main>
    </div>
  </div>
</template>

<style scoped>
.adm-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--gray-50);
}

.adm-workspace {
  display: flex;
  flex: 1;
  min-height: calc(100vh - var(--app-header-roles-h, 48px));
}

.adm-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.topbar {
  height: var(--topbar-h);
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  flex-shrink: 0;
  position: sticky;
  top: 0;
  z-index: 10;
}

.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
}

.topbar-sub {
  font-size: 12px;
  color: var(--gray-500);
  margin-left: 8px;
}

.content-area {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* Card header com filtros */
.card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  padding: 16px 20px;
  border-bottom: 1px solid var(--gray-200);
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-900);
}

.card-sub {
  font-size: 12px;
  color: var(--gray-500);
  margin-top: 2px;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.search-input {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--gray-50);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md, 6px);
  padding: 6px 10px;
  color: var(--gray-400);
}

.search-input input {
  border: none;
  background: transparent;
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  min-width: 180px;
}

.search-input input::placeholder {
  color: var(--gray-400);
}

.select-wrap {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--gray-50);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md, 6px);
  padding: 6px 10px;
  color: var(--gray-400);
}

.select-wrap select {
  border: none;
  background: transparent;
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  cursor: pointer;
  min-width: 160px;
}

.select-wrap.disabled {
  opacity: 0.5;
}

.select-wrap.disabled select {
  cursor: not-allowed;
}

.btn-clear {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 6px 10px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md, 6px);
  background: var(--white);
  color: var(--gray-500);
  font: inherit;
  font-size: 12px;
  cursor: pointer;
  transition: all 0.15s;
}

.btn-clear:hover {
  border-color: var(--gray-400);
  color: var(--gray-700);
}

/* Tabela */
.table-wrap {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

thead tr {
  background: var(--gray-50);
}

th {
  padding: 10px 16px;
  text-align: left;
  font-size: 11px;
  font-weight: 600;
  color: var(--gray-500);
  text-transform: uppercase;
  letter-spacing: 0.04em;
  white-space: nowrap;
  border-bottom: 1px solid var(--gray-200);
}

td {
  padding: 12px 16px;
  color: var(--gray-700);
  border-bottom: 1px solid var(--gray-100);
  white-space: nowrap;
}

tbody tr:hover td {
  background: var(--gray-50);
}

.table-title {
  display: block;
  color: var(--gray-900);
  font-weight: 600;
}

.table-empty {
  padding: 32px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
  white-space: normal !important;
}

.table-error {
  color: var(--red) !important;
}

.contrato-tag {
  display: inline-block;
  background: var(--blue-50, #eff6ff);
  color: var(--blue-700, #1d4ed8);
  font-size: 11px;
  font-weight: 500;
  padding: 2px 8px;
  border-radius: 20px;
  white-space: nowrap;
}

/* Responsivo */
@media (max-width: 1024px) {
  .metrics-grid {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 860px) {
  .adm-workspace {
    flex-direction: column;
  }

  .topbar {
    position: static;
    height: auto;
    padding: 16px;
  }

  .card-header {
    flex-direction: column;
  }

  .filter-bar {
    width: 100%;
  }

  .search-input input,
  .select-wrap select {
    min-width: 120px;
  }

  .metrics-grid {
    grid-template-columns: 1fr 1fr;
  }
}

@media (max-width: 480px) {
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}
</style>
