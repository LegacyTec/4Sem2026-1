<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import ContratoService, { type IContrato } from '@/services/ContratoService'
import { computed, onMounted, reactive, ref } from 'vue'

type ContractStatus = 'Ativo' | 'Expirando' | 'Inativo'

type Contract = {
  id: number | string
  nomeEmpresa: string
  unidade: string
  dataInicio: string
  dataFim: string
  quantidadePlantas: number
  quantidadeAtivos: number
  ordensAbertas: number
  status: ContractStatus
  descricao: string
}

const isPanelOpen = ref(false)
const isSubmitting = ref(false)
const isLoading = ref(false)
const errorMessage = ref('')

/** Formulário alinhado ao payload da API. */
const novoContrato = reactive({
  nomeEmpresa: '',
  dataInicio: '',
  dataFim: '',
  quantidadePlantas: 1,
  descricao: '',
})

/** Contratos carregados da API. */
const contratos = ref<Contract[]>([])

/** Somente contratos; totais de usuários/ordens globais virão de outros endpoints. */
const totalUsuarios = ref(0)

const totalAtivos = computed(() => contratos.value.reduce((acc, c) => acc + (c.quantidadeAtivos || 0), 0))
const ordensAbertasSum = computed(() => contratos.value.reduce((acc, c) => acc + (c.ordensAbertas || 0), 0))

const metricas = computed(() => [
  { label: 'Total de Contratos', value: contratos.value.length, tone: 'blue' as const },
  { label: 'Total de Ativos', value: totalAtivos.value, tone: 'green' as const },
  { label: 'Total de Usuários', value: totalUsuarios.value, tone: 'amber' as const },
  { label: 'Ordens em Andamento', value: ordensAbertasSum.value, tone: 'red' as const },
])

/**
 * Carrega os contratos da API ao montar o componente
 */
onMounted(async () => {
  console.log('🔵 [AdmDashboard] Componente montado, carregando contratos...')
  await carregarContratos()
})

/**
 * Busca contratos do backend
 */
async function carregarContratos() {
  console.log('📡 [AdmDashboard] Iniciando carregamento de contratos...')
  isLoading.value = true
  errorMessage.value = ''
  try {
    console.log('📡 [ContratoService] Chamando buscarTodos()')
    const dados = await ContratoService.buscarTodos()
    console.log('✅ [ContratoService] Dados recebidos:', dados)
    console.log(`📊 Total de contratos: ${dados.length}`)
    contratos.value = transformarDadosBackend(dados)
    console.log('✅ [AdmDashboard] Contratos carregados e transformados')
  } catch (error: any) {
    console.error('❌ [AdmDashboard] Erro ao carregar:', error)
    console.error('Status:', error?.response?.status)
    console.error('Mensagem:', error?.message)
    errorMessage.value =
      error?.response?.data?.message || 'Erro ao carregar contratos. Verifique se o backend está rodando.'
  } finally {
    isLoading.value = false
    console.log('}
}

/**
 * Transforma dados do backend para o formato da UI
 */
function transformarDadosBackend(dados: IContrato[]): Contract[] {
  return dados.map((d) => ({
    id: d.id,
    nomeEmpresa: d.nomeEmpresa,
    unidade: '', // Backend não retorna este campo
    dataInicio: d.dataInicio,
    dataFim: d.dataFim,
    quantidadePlantas: d.quantidadePlanta || 0,
    quantidadeAtivos: d.quantidadeAtivos || 0,
    ordensAbertas: 0, // Dados virão de outro endpoint no futuro
    status: ContratoService.resolverStatus(d.dataFim),
    descricao: d.descricao,
  }))
}

function abrirPainel() {
  isPanelOpen.value = true
}

function fecharPainel() {
  isPanelOpen.value = false
}

/**
 * Salva um novo contrato via API
 */
async function salvarContrato() {
  errorMessage.value = ''
  // Validação básica
  if (
    !novoContrato.nomeEmpresa.trim() ||
    !novoContrato.dataInicio ||
    !novoContrato.dataFim ||
    !novoContrato.descricao.trim()
  ) {
    errorMessage.value = 'Por favor, preencha todos os campos obrigatórios.'
    return
  }

  isSubmitting.value = true
  try {
    const novoData = await ContratoService.criar({
      nomeEmpresa: novoContrato.nomeEmpresa.trim(),
      quantidadePlanta: Number(novoContrato.quantidadePlantas),
      dataInicio: novoContrato.dataInicio,
      dataFim: novoContrato.dataFim,
      descricao: novoContrato.descricao.trim(),
    })

    // Adiciona o novo contrato à lista
    contratos.value.unshift({
      id: novoData.id,
      nomeEmpresa: novoData.nomeEmpresa,
      unidade: '',
      dataInicio: novoData.dataInicio,
      dataFim: novoData.dataFim,
      quantidadePlantas: novoData.quantidadePlanta || 0,
      quantidadeAtivos: novoData.quantidadeAtivos || 0,
      ordensAbertas: 0,
      status: ContratoService.resolverStatus(novoData.dataFim),
      descricao: novoData.descricao,
    })

    limparFormulario()
    fecharPainel()
  } catch (error: any) {
    console.error('Erro ao salvar contrato:', error)
    errorMessage.value = error?.response?.data?.message || 'Erro ao salvar contrato.'
  } finally {
    isSubmitting.value = false
  }
}

function limparFormulario() {
  novoContrato.nomeEmpresa = ''
  novoContrato.dataInicio = ''
  novoContrato.dataFim = ''
  novoContrato.quantidadePlantas = 1
  novoContrato.descricao = ''
  errorMessage.value = ''
}

function formatarData(iso: string) {
  return ContratoService.formatarData(iso)
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
            <span class="topbar-title">Dashboard</span>
            <span class="topbar-sub">Visão geral do sistema</span>
          </div>
          <div class="topbar-actions">
            <button class="btn btn-secondary btn-sm" type="button" disabled title="Período — integração com API">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <rect x="3" y="4" width="18" height="18" rx="2" />
                <line x1="16" y1="2" x2="16" y2="6" />
                <line x1="8" y1="2" x2="8" y2="6" />
                <line x1="3" y1="10" x2="21" y2="10" />
              </svg>
              Período
            </button>
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
          <!-- Estado de carregamento -->
          <div v-if="isLoading" class="loading-container">
            <div class="loading-spinner"></div>
            <p class="loading-text">Carregando contratos...</p>
          </div>

          <!-- Mensagem de erro -->
          <div v-if="errorMessage" class="error-banner">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
            <span>{{ errorMessage }}</span>
            <button class="error-close" type="button" @click="errorMessage = ''" title="Fechar">
              ✕
            </button>
          </div>

          <template v-if="!isLoading">
            <section class="metrics-grid" aria-label="Indicadores principais">
              <article v-for="m in metricas" :key="m.label" class="metric-card" :class="m.tone">
                <div class="metric-label">{{ m.label }}</div>
                <div class="metric-value">{{ m.value }}</div>
              </article>
            </section>

            <section id="contratos" class="card contracts-card">
              <div class="card-header">
                <div>
                  <div class="card-title">Contratos Ativos</div>
                  <div class="card-sub">
                    {{ contratos.length === 0 ? 'Nenhum contrato carregado' : `${contratos.length} contrato(s)` }}
                  </div>
                </div>
                <button class="btn btn-secondary btn-sm" type="button">Exportar</button>
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
                      <th />
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="contratos.length === 0">
                      <td colspan="8" class="table-empty">
                        Nenhum contrato ainda. Cadastre pelo botão <strong>Criar Contrato</strong> para começar.
                      </td>
                    </tr>
                    <template v-else>
                      <tr v-for="c in contratos" :key="c.id">
                        <td>
                          <strong class="table-title">{{ c.nomeEmpresa }}</strong>
                          <span class="table-subtitle">{{ c.unidade || '—' }}</span>
                        </td>
                        <td>{{ formatarData(c.dataInicio) }}</td>
                        <td>{{ formatarData(c.dataFim) }}</td>
                        <td>{{ c.quantidadePlantas }}</td>
                        <td>{{ c.quantidadeAtivos }}</td>
                        <td>{{ c.ordensAbertas || '—' }}</td>
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
                        <td><button class="link-button" type="button">Ver →</button></td>
                      </tr>
                    </template>
                  </tbody>
                </table>
              </div>
            </section>
          </template>
        </section>
      </main>
    </div>

    <div class="drawer-backdrop" :class="{ open: isPanelOpen }" @click="fecharPainel" />

    <aside class="detail-overlay contract-drawer" :class="{ open: isPanelOpen }" aria-label="Criar contrato">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="fecharPainel">✕</button>
        <strong>Criar Contrato</strong>
        <p>Preencha os dados. Ao integrar o backend, este formulário enviará para a API de contratos.</p>
      </div>

      <form class="detail-body contract-form" @submit.prevent="salvarContrato">
        <!-- Mensagem de erro no formulário -->
        <div v-if="errorMessage" class="form-error-banner">
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="12" r="10" />
            <line x1="12" y1="8" x2="12" y2="12" />
            <line x1="12" y1="16" x2="12.01" y2="16" />
          </svg>
          <span>{{ errorMessage }}</span>
        </div>

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
          <textarea v-model="novoContrato.descricao" rows="4" placeholder="Escopo e observações do contrato" required />
        </label>

        <div class="drawer-actions">
          <button class="btn btn-secondary" type="button" @click="fecharPainel" :disabled="isSubmitting">
            Cancelar
          </button>
          <button class="btn btn-primary" type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? 'Salvando...' : 'Salvar contrato' }}
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
  min-height: 0;
  min-height: calc(100vh - var(--app-header-roles-h));
}

.adm-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  min-height: 0;
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
  font-weight: 400;
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

.contracts-card {
  margin-bottom: 16px;
}

.table-title,
.table-subtitle {
  display: block;
}

.table-title {
  color: var(--gray-900);
  font-weight: 600;
}

.table-subtitle {
  color: var(--gray-500);
  font-size: 12px;
  margin-top: 2px;
}

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
  line-height: 1.5;
}

.link-button {
  border: 0;
  background: transparent;
  color: var(--blue-600);
  font-weight: 600;
  cursor: pointer;
  font-family: var(--font);
}

.link-button:hover {
  color: var(--blue-700);
  text-decoration: underline;
}

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
  max-width: 330px;
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

/* Estados de carregamento e erro */
.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 60px 24px;
  min-height: 400px;
  color: var(--gray-500);
}

.loading-spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--gray-200);
  border-top-color: var(--blue-500);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.loading-text {
  font-size: 14px;
  margin: 0;
}

.error-banner {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: var(--red-50);
  border: 1px solid var(--red-200);
  border-radius: var(--radius-md);
  color: var(--red-700);
  font-size: 13px;
  margin-bottom: 16px;
}

.error-banner svg {
  color: var(--red-500);
  flex-shrink: 0;
}

.error-banner span {
  flex: 1;
}

.error-close {
  border: none;
  background: transparent;
  color: var(--red-600);
  cursor: pointer;
  font-size: 16px;
  padding: 0 4px;
  font-weight: bold;
}

.error-close:hover {
  color: var(--red-800);
}

.form-error-banner {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: var(--red-50);
  border: 1px solid var(--red-200);
  border-radius: var(--radius-md);
  color: var(--red-700);
  font-size: 13px;
  margin-bottom: 12px;
}

.form-error-banner svg {
  color: var(--red-500);
  flex-shrink: 0;
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
    flex-wrap: wrap;
  }

  .metrics-grid {
    grid-template-columns: 1fr;
  }

  .contract-drawer {
    width: min(420px, 100vw);
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
