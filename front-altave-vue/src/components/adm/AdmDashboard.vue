<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { buscarContratos, criarContrato, desabilitarContrato, editarContrato, formatarData, resolverStatus } from '@/services/ContratoService'
import { buscarOrdens, buscarUsuarios } from '@/services/OrdemService'
import { criarPlanta } from '@/services/PlantaService'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

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
  usuarios?: { id: number; nomeCompleto: string; cargo?: string; email?: string }[]
}

/* ─── estado ─── */
const isLoading    = ref(false)
const errorMessage = ref('')
const contratos    = ref<Contract[]>([])
const totalUsuarios = ref(0)
const totalOrdens   = ref(0)

/* ─── drawer criar/editar contrato ─── */
const isPanelOpen  = ref(false)
const isSubmitting = ref(false)
const formError    = ref('')
const contratoEmEdicao = ref<number | null>(null)
const excluindoId = ref<number | null>(null)
const novaPlantaNome = ref('')
const novoContrato = reactive({
  nomeEmpresa: '',
  dataInicio: '',
  dataFim: '',
  plantas: [] as string[],
  descricao: '',
})

function adicionarPlanta() {
  const nome = novaPlantaNome.value.trim().toUpperCase()
  if (!nome) return
  if (novoContrato.plantas.includes(nome)) return
  novoContrato.plantas.push(nome)
  novaPlantaNome.value = ''
}

function removerPlanta(index: number) {
  novoContrato.plantas.splice(index, 1)
}



/* ─── KPIs computados ─── */
const totalAtivos = computed(() => contratos.value.reduce((acc, c) => acc + (c.quantidadeAtivos || 0), 0))

const metricas = computed(() => [
  {
    label: 'Total de Contratos',
    value: contratos.value.length,
    tone: 'blue' as const,
    icon: 'doc',
  },
  {
    label: 'Total de Ativos',
    value: totalAtivos.value,
    tone: 'green' as const,
    icon: 'box',
  },
  {
    label: 'Total de Usuários',
    value: totalUsuarios.value,
    tone: 'amber' as const,
    icon: 'users',
  },
  {
    label: 'Ordens em Andamento',
    value: totalOrdens.value,
    tone: 'red' as const,
    icon: 'clock',
  },
])

/* ─── carga ─── */
onMounted(async () => {
  isLoading.value = true
  try {
    const [dadosContratos, dadosUsuarios, dadosOrdens] = await Promise.all([
      buscarContratos(),
      buscarUsuarios(),
      buscarOrdens(),
    ])

    contratos.value = dadosContratos.map(d => ({
      id: d.id,
      nomeEmpresa: d.nomeEmpresa,
      dataInicio: d.dataInicio,
      dataFim: d.dataFim,
      quantidadePlantas: d.quantidadePlanta || 0,
      quantidadeAtivos: d.quantidadeAtivos || 0,
      status: resolverStatus(d.dataFim),
      descricao: d.descricao || '',
      usuarios: (d as any).usuarios || [],
    }))

    totalUsuarios.value = dadosUsuarios.length

    totalOrdens.value = dadosOrdens.filter(o => o.status === 'EM_ANDAMENTO').length
  } catch (e: any) {
    errorMessage.value = 'Erro ao carregar dados. Verifique se o backend está rodando.'
    console.error(e)
  } finally {
    isLoading.value = false
  }
})

/* ─── criar/editar contrato ─── */
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
  novoContrato.descricao = c.descricao
  formError.value = ''
  isPanelOpen.value = true
}

function fecharPainel() {
  isPanelOpen.value = false
  contratoEmEdicao.value = null
  formError.value = ''
  limparFormulario()
}

async function salvarContrato() {
  formError.value = ''
  if (!novoContrato.nomeEmpresa.trim() || !novoContrato.dataInicio || !novoContrato.dataFim || !novoContrato.descricao.trim()) {
    formError.value = 'Preencha todos os campos obrigatórios.'
    return
  }
  if (contratoEmEdicao.value === null && novoContrato.plantas.length === 0) {
    formError.value = 'Adicione pelo menos uma planta.'
    return
  }
  isSubmitting.value = true
  const payload = {
    nomeEmpresa: novoContrato.nomeEmpresa.trim(),
    quantidadePlanta: contratoEmEdicao.value !== null
      ? contratos.value.find(c => c.id === contratoEmEdicao.value)?.quantidadePlantas ?? 1
      : novoContrato.plantas.length,
    dataInicio: novoContrato.dataInicio,
    dataFim: novoContrato.dataFim,
    descricao: novoContrato.descricao.trim(),
  }
  try {
    if (contratoEmEdicao.value !== null) {
      const atualizado = await editarContrato(contratoEmEdicao.value, payload)
      const idx = contratos.value.findIndex(c => c.id === contratoEmEdicao.value)
      if (idx !== -1) {
        contratos.value[idx] = {
          ...contratos.value[idx]!,
          nomeEmpresa: atualizado.nomeEmpresa,
          dataInicio: atualizado.dataInicio,
          dataFim: atualizado.dataFim,
          quantidadePlantas: atualizado.quantidadePlanta || 0,
          quantidadeAtivos: atualizado.quantidadeAtivos || 0,
          status: resolverStatus(atualizado.dataFim),
          descricao: atualizado.descricao || '',
        }
      }
      fecharPainel()
      return
    }

    const criado = await criarContrato(payload)
    await Promise.all(novoContrato.plantas.map(nome => criarPlanta(nome, criado.id)))

    contratos.value.unshift({
      id: criado.id,
      nomeEmpresa: criado.nomeEmpresa,
      dataInicio: criado.dataInicio,
      dataFim: criado.dataFim,
      quantidadePlantas: novoContrato.plantas.length,
      quantidadeAtivos: criado.quantidadeAtivos || 0,
      status: resolverStatus(criado.dataFim),
      descricao: criado.descricao || '',
      usuarios: [],
    })
    fecharPainel()
  } catch {
    formError.value = contratoEmEdicao.value !== null ? 'Erro ao atualizar contrato.' : 'Erro ao salvar contrato.'
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
    contratos.value = contratos.value.filter(ct => ct.id !== c.id)
  } catch {
    formError.value = 'Erro ao excluir contrato.'
  } finally {
    excluindoId.value = null
  }
}

function limparFormulario() {
  novoContrato.nomeEmpresa = ''
  novoContrato.dataInicio  = ''
  novoContrato.dataFim     = ''
  novoContrato.plantas     = []
  novoContrato.descricao   = ''
  novaPlantaNome.value     = ''
}


</script>

<template>
  <div class="adm-layout">
    <AppHeaderRoles />

    <div class="adm-workspace">
      <SidebarAdm />

      <main class="adm-main">
        <!-- topbar -->
        <header class="topbar">
          <div>
            <span class="topbar-title">Geral</span>
            <span class="topbar-sub">Visão geral do sistema</span>
          </div>
          <div class="topbar-actions">
            <button class="btn btn-primary btn-sm" type="button" @click="abrirPainel">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Criar Contrato
            </button>
          </div>
        </header>

        <section class="content-area">

          <!-- KPIs -->
          <section class="metrics-grid" aria-label="Indicadores principais">
            <article v-for="m in metricas" :key="m.label" class="metric-card" :class="m.tone">
              <div class="metric-label">{{ m.label }}</div>
              <div class="metric-value">{{ isLoading ? '—' : m.value }}</div>
            </article>
          </section>

          <!-- tabela contratos -->
          <section class="card contracts-card">
            <div class="card-header">
              <div>
                <div class="card-title">Contratos</div>
                <div class="card-sub">
                  {{ isLoading ? 'Carregando...' : contratos.length === 0 ? 'Nenhum contrato' : `${contratos.length} contrato(s) cadastrado(s)` }}
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
                    <th>Status</th>
                    <th>Ações</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="isLoading">
                    <td colspan="8" class="table-empty">Carregando contratos...</td>
                  </tr>
                  <tr v-else-if="errorMessage">
                    <td colspan="8" class="table-empty error-text">{{ errorMessage }}</td>
                  </tr>
                  <tr v-else-if="contratos.length === 0">
                    <td colspan="8" class="table-empty">
                      Nenhum contrato ainda. Clique em <strong>Criar Contrato</strong>.
                    </td>
                  </tr>
                  <template v-else>
                    <tr v-for="c in contratos" :key="c.id" class="table-row-hover">
                      <td>
                        <strong class="table-title">{{ c.nomeEmpresa }}</strong>
                      </td>
                      <td>{{ formatarData(c.dataInicio) }}</td>
                      <td>{{ formatarData(c.dataFim) }}</td>
                      <td>{{ c.quantidadePlantas }}</td>
                      <td>{{ c.quantidadeAtivos }}</td>
                      <td>
                        <span class="badge" :class="{
                          'badge-active':  c.status === 'Ativo',
                          'badge-pending': c.status === 'Expirando',
                          'badge-done':    c.status === 'Inativo',
                        }">{{ c.status }}</span>
                      </td>
                      <td>
                        <div class="row-actions">
                          <button class="link-button" type="button" @click="router.push(`/adm/contratos/${c.id}`)">
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

    <!-- backdrop -->
    <div
      class="drawer-backdrop"
      :class="{ open: isPanelOpen }"
      @click="fecharPainel()"
    />

    <!-- drawer: CRIAR CONTRATO -->
    <aside class="detail-overlay contract-drawer" :class="{ open: isPanelOpen }">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="fecharPainel">✕</button>
        <strong>{{ contratoEmEdicao !== null ? 'Editar Contrato' : 'Criar Contrato' }}</strong>
        <p>{{ contratoEmEdicao !== null ? 'Atualize os dados do contrato.' : 'Preencha os dados do novo contrato.' }}</p>
      </div>

      <form class="detail-body contract-form" @submit.prevent="salvarContrato">
        <label>
          Nome da empresa *
          <input v-model="novoContrato.nomeEmpresa" type="text" placeholder="Ex: Petrobras S.A." required />
        </label>

        <div class="form-grid">
          <label>
            Data de início *
            <input v-model="novoContrato.dataInicio" type="date" required />
          </label>
          <label>
            Data de término *
            <input v-model="novoContrato.dataFim" type="date" required />
          </label>
        </div>

        <div v-if="contratoEmEdicao === null" class="field-group">
          <span class="field-label">Plantas * <small>(mínimo 1)</small></span>
          <div class="plant-input-row">
            <input
              v-model="novaPlantaNome"
              type="text"
              class="plant-input"
              placeholder="Nome da planta (ex: SJK, BOT)"
              maxlength="50"
              @keyup.enter="adicionarPlanta"
            />
            <button type="button" class="btn btn-secondary btn-sm" @click="adicionarPlanta">+ Adicionar</button>
          </div>
          <div v-if="novoContrato.plantas.length === 0" class="plants-empty">
            Nenhuma planta adicionada ainda.
          </div>
          <div v-else class="plants-chips">
            <span v-for="(p, i) in novoContrato.plantas" :key="i" class="plant-chip">
              {{ p }}
              <button type="button" class="chip-remove" @click="removerPlanta(i)">×</button>
            </span>
          </div>
        </div>

        <label>
          Descrição *
          <textarea v-model="novoContrato.descricao" rows="4" placeholder="Escopo e observações do contrato" required />
        </label>

        <p v-if="formError" class="form-error">{{ formError }}</p>

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
  min-height: calc(100vh - var(--app-header-roles-h));
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
.topbar-actions { display: flex; gap: 10px; }

/* content */
.content-area { padding: 24px; display: flex; flex-direction: column; gap: 20px; }

/* KPIs — usa estilo global de .metrics-grid / .metric-card (assets/main.css) */

/* tabela */
.contracts-card { margin-bottom: 0; }
.table-title { font-weight: 600; color: var(--gray-900); }
.table-empty { padding: 32px 20px; text-align: center; color: var(--gray-500); font-size: 14px; }
.error-text  { color: #ef4444 !important; }
.table-row-hover:hover { background: var(--gray-50); }

.link-button {
  border: 0; background: transparent;
  color: var(--blue-600, #2563eb);
  font-weight: 600; cursor: pointer;
  font-family: var(--font);
}
.link-button:hover { text-decoration: underline; }

.row-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.link-danger {
  color: var(--red, #ef4444) !important;
}

.link-danger:hover {
  color: #991b1b !important;
}

.link-danger:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* drawer backdrop */
.drawer-backdrop {
  position: fixed; inset: 0;
  background: rgba(11,31,51,.3);
  opacity: 0; visibility: hidden;
  transition: all .2s ease;
  z-index: 90;
}
.drawer-backdrop.open { opacity: 1; visibility: visible; }

/* drawer detalhe info */
.detail-info { display: flex; flex-direction: column; gap: 20px; }
.info-group {
  background: var(--gray-50);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 14px 16px;
}
.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 0;
  border-bottom: 1px solid var(--gray-100);
}
.info-row:last-child { border-bottom: none; }
.info-label { font-size: 11px; font-weight: 600; color: var(--gray-500); text-transform: uppercase; letter-spacing: .05em; }
.info-value { font-size: 13px; font-weight: 500; color: var(--gray-900); }
.info-desc  { font-size: 13px; color: var(--gray-700); line-height: 1.6; margin: 0; }
.info-desc.muted { color: var(--gray-400); }
.mb-6 { margin-bottom: 6px; }
.mt-4 { margin-top: 4px; }

.user-list { display: flex; flex-direction: column; gap: 8px; }
.user-chip {
  display: flex; align-items: center; gap: 10px;
  background: var(--white); border: 1px solid var(--gray-200);
  border-radius: 8px; padding: 8px 12px;
}
.chip-avatar {
  width: 30px; height: 30px; border-radius: 50%;
  background: var(--blue-400, #60a5fa); color: white;
  display: flex; align-items: center; justify-content: center;
  font-size: 12px; font-weight: 600; flex-shrink: 0;
}
.chip-name { font-size: 13px; font-weight: 500; color: var(--gray-900); }
.chip-role { font-size: 11px; color: var(--gray-500); }

/* form drawer */
.contract-form { display: flex; flex-direction: column; gap: 14px; }
.contract-form label {
  display: flex; flex-direction: column; gap: 6px;
  color: var(--gray-700); font-size: 12px; font-weight: 600;
}
.contract-form input,
.contract-form textarea {
  padding: 10px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit; font-size: 13px;
  color: var(--gray-900); outline: none;
  background: var(--white);
}
.contract-form input:focus,
.contract-form textarea:focus {
  border-color: var(--blue-400);
  box-shadow: 0 0 0 3px var(--blue-50);
}
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.form-error { color: #ef4444; font-size: 12px; margin: 0; }

/* plantas */
.field-group { display: flex; flex-direction: column; gap: 8px; }
.field-label { font-size: 12px; font-weight: 600; color: var(--gray-700); }
.field-label small { font-weight: 400; color: var(--gray-400); margin-left: 4px; }
.plant-input-row { display: flex; gap: 8px; }
.plant-input {
  flex: 1; padding: 10px 12px;
  border: 1px solid var(--gray-300); border-radius: var(--radius-md);
  font: inherit; font-size: 13px; color: var(--gray-900); outline: none;
  background: var(--white);
}
.plant-input:focus { border-color: var(--blue-400); box-shadow: 0 0 0 3px var(--blue-50); }
.plants-empty { font-size: 12px; color: var(--gray-400); padding: 6px 0; }
.plants-chips { display: flex; flex-wrap: wrap; gap: 6px; }
.plant-chip {
  display: inline-flex; align-items: center; gap: 6px;
  background: #eff6ff; color: #1d4ed8;
  border: 1px solid #bfdbfe; border-radius: 20px;
  padding: 4px 10px 4px 12px; font-size: 12px; font-weight: 600;
}
.chip-remove {
  border: none; background: none; cursor: pointer;
  color: #60a5fa; font-size: 14px; line-height: 1;
  padding: 0; display: flex; align-items: center;
}
.chip-remove:hover { color: #1d4ed8; }

.drawer-actions {
  display: flex; justify-content: flex-end;
  gap: 10px; margin-top: 6px;
  padding-top: 16px;
  border-top: 1px solid var(--gray-200);
}
.drawer-actions .btn:disabled { opacity: .65; cursor: not-allowed; }

/* detalhe overlay (herdado global) */
.detail-header p { margin-top: 6px; color: var(--gray-500); font-size: 13px; }

@media (max-width: 1100px) { .metrics-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 860px) {
  .adm-workspace { flex-direction: column; }
  .metrics-grid  { grid-template-columns: 1fr 1fr; }
  .form-grid     { grid-template-columns: 1fr; }
}
@media (max-width: 540px) { .metrics-grid { grid-template-columns: 1fr; } }
</style>
