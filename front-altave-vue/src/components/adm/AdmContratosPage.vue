<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import {
  buscarContratos,
  criarContrato,
  desabilitarContrato,
  editarContrato,
  formatarData,
  resolverStatus,
} from '@/services/ContratoService'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

type ContractStatus = 'Ativo' | 'Expirando' | 'Inativo'

type Contract = {
  id: number
  nomeEmpresa: string
  dataInicio: string
  dataFim: string
  quantidadePlantas: number
  quantidadeAtivos: number
  status: ContractStatus
  descricao: string
}

const router = useRouter()
const isPanelOpen = ref(false)
const isSubmitting = ref(false)
const isLoading = ref(false)
const errorMessage = ref('')
const busca = ref('')
const contratoEmEdicao = ref<number | null>(null)
const excluindoId = ref<number | null>(null)

const novoContrato = reactive({
  nomeEmpresa: '',
  dataInicio: '',
  dataFim: '',
  quantidadePlantas: 1,
  descricao: '',
})

const contratos = ref<Contract[]>([])

const contratosFiltrados = computed(() => {
  const q = busca.value.trim().toLowerCase()
  if (!q) return contratos.value
  return contratos.value.filter((c) => c.nomeEmpresa.toLowerCase().includes(q))
})

const totalAtivos = computed(() => contratos.value.filter((c) => c.status === 'Ativo').length)
const totalExpirando = computed(() => contratos.value.filter((c) => c.status === 'Expirando').length)
const totalInativos = computed(() => contratos.value.filter((c) => c.status === 'Inativo').length)

const metricas = computed(() => [
  { label: 'Total de Contratos', value: contratos.value.length, tone: 'blue' as const },
  { label: 'Ativos', value: totalAtivos.value, tone: 'green' as const },
  { label: 'Expirando', value: totalExpirando.value, tone: 'amber' as const },
  { label: 'Inativos', value: totalInativos.value, tone: 'red' as const },
])

onMounted(async () => {
  isLoading.value = true
  try {
    const dados = await buscarContratos()
    contratos.value = dados.map((d) => ({
      id: d.id,
      nomeEmpresa: d.nomeEmpresa,
      dataInicio: d.dataInicio,
      dataFim: d.dataFim,
      quantidadePlantas: d.quantidadePlanta || 0,
      quantidadeAtivos: d.quantidadeAtivos || 0,
      status: resolverStatus(d.dataFim),
      descricao: d.descricao,
    }))
  } catch (e: unknown) {
    errorMessage.value = 'Erro ao carregar contratos. Verifique se o backend está rodando.'
    console.error(e)
  } finally {
    isLoading.value = false
  }
})

function verContrato(id: number) {
  router.push(`/adm/contratos/${id}`)
}

function abrirPainel() {
  contratoEmEdicao.value = null
  limparFormulario()
  isPanelOpen.value = true
}

function abrirEdicao(c: Contract) {
  contratoEmEdicao.value = c.id
  novoContrato.nomeEmpresa = c.nomeEmpresa
  novoContrato.dataInicio = c.dataInicio
  novoContrato.dataFim = c.dataFim
  novoContrato.quantidadePlantas = c.quantidadePlantas
  novoContrato.descricao = c.descricao
  errorMessage.value = ''
  isPanelOpen.value = true
}

function fecharPainel() {
  isPanelOpen.value = false
  contratoEmEdicao.value = null
  errorMessage.value = ''
  limparFormulario()
}

async function salvarContrato() {
  if (
    !novoContrato.nomeEmpresa.trim() ||
    !novoContrato.dataInicio ||
    !novoContrato.dataFim ||
    !novoContrato.descricao.trim()
  ) {
    errorMessage.value = 'Preencha todos os campos obrigatórios.'
    return
  }
  isSubmitting.value = true
  errorMessage.value = ''
  const payload = {
    nomeEmpresa: novoContrato.nomeEmpresa.trim(),
    quantidadePlanta: Number(novoContrato.quantidadePlantas),
    dataInicio: novoContrato.dataInicio,
    dataFim: novoContrato.dataFim,
    descricao: novoContrato.descricao.trim(),
  }
  try {
    if (contratoEmEdicao.value !== null) {
      const atualizado = await editarContrato(contratoEmEdicao.value, payload)
      const idx = contratos.value.findIndex((c) => c.id === contratoEmEdicao.value)
      if (idx !== -1) {
        contratos.value[idx] = {
          id: atualizado.id,
          nomeEmpresa: atualizado.nomeEmpresa,
          dataInicio: atualizado.dataInicio,
          dataFim: atualizado.dataFim,
          quantidadePlantas: atualizado.quantidadePlanta || 0,
          quantidadeAtivos: atualizado.quantidadeAtivos || 0,
          status: resolverStatus(atualizado.dataFim),
          descricao: atualizado.descricao,
        }
      }
    } else {
      const criado = await criarContrato(payload)
      contratos.value.unshift({
        id: criado.id,
        nomeEmpresa: criado.nomeEmpresa,
        dataInicio: criado.dataInicio,
        dataFim: criado.dataFim,
        quantidadePlantas: criado.quantidadePlanta || 0,
        quantidadeAtivos: criado.quantidadeAtivos || 0,
        status: resolverStatus(criado.dataFim),
        descricao: criado.descricao,
      })
    }
    fecharPainel()
  } catch (e: unknown) {
    errorMessage.value = contratoEmEdicao.value !== null
      ? 'Erro ao atualizar contrato.'
      : 'Erro ao salvar contrato.'
    console.error(e)
  } finally {
    isSubmitting.value = false
  }
}

async function excluirContrato(c: Contract) {
  const confirmar = window.confirm(
    `Desabilitar o contrato de "${c.nomeEmpresa}"? Esta ação não pode ser desfeita.`,
  )
  if (!confirmar) return
  excluindoId.value = c.id
  try {
    await desabilitarContrato(c.id)
    contratos.value = contratos.value.filter((ct) => ct.id !== c.id)
  } catch (e: unknown) {
    errorMessage.value = 'Erro ao excluir contrato.'
    console.error(e)
  } finally {
    excluindoId.value = null
  }
}

function limparFormulario() {
  novoContrato.nomeEmpresa = ''
  novoContrato.dataInicio = ''
  novoContrato.dataFim = ''
  novoContrato.quantidadePlantas = 1
  novoContrato.descricao = ''
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
            <span class="topbar-title">Contratos</span>
            <span class="topbar-sub">Gestão de contratos do sistema</span>
          </div>
          <div class="topbar-actions">
            <button class="btn btn-primary btn-sm" type="button" @click="abrirPainel">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Criar Contrato
            </button>
          </div>
        </header>

        <section class="content-area adm-content">
          <!-- Métricas -->
          <section class="metrics-grid" aria-label="Indicadores de contratos">
            <article v-for="m in metricas" :key="m.label" class="metric-card" :class="m.tone">
              <div class="metric-label">{{ m.label }}</div>
              <div class="metric-value">{{ m.value }}</div>
            </article>
          </section>

          <!-- Filtro e tabela -->
          <section class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Todos os Contratos</div>
                <div class="card-sub">
                  {{
                    isLoading
                      ? 'Carregando...'
                      : contratosFiltrados.length === 0
                        ? 'Nenhum contrato encontrado'
                        : `${contratosFiltrados.length} contrato(s)`
                  }}
                </div>
              </div>
              <div class="filter-bar" style="margin-bottom: 0;">
                <div class="search-input">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="11" cy="11" r="8" />
                    <line x1="21" y1="21" x2="16.65" y2="16.65" />
                  </svg>
                  <input
                    v-model="busca"
                    type="text"
                    placeholder="Buscar por empresa..."
                    style="min-width: 200px;"
                  />
                </div>
              </div>
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Empresa</th>
                    <th>Início</th>
                    <th>Término</th>
                    <th>Plantas</th>
                    <th>Ativos</th>
                    <th>Ordens abertas</th>
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="isLoading">
                    <td colspan="8" class="table-empty">Carregando contratos...</td>
                  </tr>
                  <tr v-else-if="errorMessage && contratos.length === 0">
                    <td colspan="8" class="table-empty table-error">{{ errorMessage }}</td>
                  </tr>
                  <tr v-else-if="contratosFiltrados.length === 0">
                    <td colspan="8" class="table-empty">
                      {{
                        busca.trim()
                          ? `Nenhum contrato encontrado para "${busca}".`
                          : 'Nenhum contrato ainda. Cadastre pelo botão Criar Contrato.'
                      }}
                    </td>
                  </tr>
                  <template v-else>
                    <tr v-for="c in contratosFiltrados" :key="c.id">
                      <td>
                        <strong class="table-title">{{ c.nomeEmpresa }}</strong>
                      </td>
                      <td>{{ formatarData(c.dataInicio) }}</td>
                      <td>{{ formatarData(c.dataFim) }}</td>
                      <td>{{ c.quantidadePlantas }}</td>
                      <td>{{ c.quantidadeAtivos }}</td>
                      <td>—</td>
                      <td>
                        <span
                          class="badge"
                          :class="{
                            'badge-active': c.status === 'Ativo',
                            'badge-pending': c.status === 'Expirando',
                            'badge-done': c.status === 'Inativo',
                          }"
                        >
                          {{ c.status }}
                        </span>
                      </td>
                      <td>
                        <div class="row-actions">
                          <button class="link-button" type="button" @click="verContrato(c.id)">
                            Ver
                          </button>
                          <button class="link-button" type="button" @click="abrirEdicao(c)">
                            Editar
                          </button>
                          <button
                            class="link-button link-danger"
                            type="button"
                            :disabled="excluindoId === c.id"
                            @click="excluirContrato(c)"
                          >
                            {{ excluindoId === c.id ? 'Excluindo...' : 'Excluir' }}
                          </button>
                        </div>
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

    <!-- Backdrop -->
    <div class="drawer-backdrop" :class="{ open: isPanelOpen }" @click="fecharPainel" />

    <!-- Drawer criar contrato -->
    <aside class="detail-overlay contract-drawer" :class="{ open: isPanelOpen }" aria-label="Criar contrato">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="fecharPainel">✕</button>
        <strong>{{ contratoEmEdicao !== null ? 'Editar Contrato' : 'Criar Contrato' }}</strong>
        <p>{{ contratoEmEdicao !== null ? 'Atualize os dados do contrato.' : 'Preencha os dados do novo contrato.' }}</p>
      </div>

      <form class="detail-body contract-form" @submit.prevent="salvarContrato">
        <p v-if="errorMessage" class="form-error">{{ errorMessage }}</p>

        <label>
          Empresa *
          <input v-model="novoContrato.nomeEmpresa" type="text" placeholder="Nome da empresa" required />
        </label>

        <div class="form-grid">
          <label>
            Data início *
            <input v-model="novoContrato.dataInicio" type="date" required />
          </label>
          <label>
            Data término *
            <input v-model="novoContrato.dataFim" type="date" required />
          </label>
        </div>

        <label>
          Plantas *
          <input v-model.number="novoContrato.quantidadePlantas" type="number" min="1" required />
        </label>

        <label>
          Descrição *
          <textarea
            v-model="novoContrato.descricao"
            rows="4"
            placeholder="Escopo e observações do contrato"
            required
          />
        </label>

        <div class="drawer-actions">
          <button class="btn btn-secondary" type="button" @click="fecharPainel">Cancelar</button>
          <button class="btn btn-primary" type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? 'Salvando...' : contratoEmEdicao !== null ? 'Salvar alterações' : 'Salvar contrato' }}
          </button>
        </div>
      </form>
    </aside>
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
  gap: 12px;
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

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.adm-content {
  padding: 24px;
}

.table-title {
  display: block;
  color: var(--gray-900);
  font-weight: 600;
}

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
}

.table-error {
  color: var(--red) !important;
}

.link-button {
  border: 0;
  background: transparent;
  color: var(--blue-600);
  font-weight: 600;
  cursor: pointer;
  font-family: var(--font);
  font-size: 13px;
}

.link-button:hover {
  color: var(--blue-700);
  text-decoration: underline;
}

.row-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.link-danger {
  color: var(--red) !important;
}

.link-danger:hover {
  color: #991b1b !important;
}

.link-danger:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* Drawer */
.drawer-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(11, 31, 51, 0.3);
  opacity: 0;
  visibility: hidden;
  transition: all 0.2s ease;
  z-index: 90;
}

.drawer-backdrop.open {
  opacity: 1;
  visibility: visible;
}

.contract-drawer {
  display: block;
}

.detail-header p {
  margin-top: 6px;
  color: var(--gray-500);
  font-size: 13px;
}

.contract-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.contract-form label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  color: var(--gray-700);
  font-size: 12px;
  font-weight: 600;
}

.contract-form input,
.contract-form textarea {
  padding: 11px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  background: var(--white);
}

.contract-form input:focus,
.contract-form textarea:focus {
  border-color: var(--blue-400);
  box-shadow: 0 0 0 3px var(--blue-50);
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.form-error {
  background: var(--red-bg);
  color: #991b1b;
  border-radius: var(--radius-sm);
  padding: 10px 12px;
  font-size: 13px;
}

.drawer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 6px;
  padding-top: 16px;
  border-top: 1px solid var(--gray-200);
}

.drawer-actions .btn:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

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
    flex-direction: column;
    align-items: flex-start;
    height: auto;
    padding: 16px;
  }

  .topbar-actions {
    width: 100%;
  }

  .metrics-grid {
    grid-template-columns: 1fr 1fr;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 480px) {
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}
</style>
