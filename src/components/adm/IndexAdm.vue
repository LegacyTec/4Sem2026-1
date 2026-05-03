<script setup lang="ts">
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { computed, onMounted, reactive, ref } from 'vue'

type ContractStatus = 'Ativo' | 'Expirando' | 'Inativo'

type Contract = {
  id: number
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

type ContractApi = {
  id?: number
  nomeEmpresa?: string
  quantidadePlanta?: number
  dataInicio?: string
  dataFim?: string
  quantidadeAtivos?: number
  descricao?: string
}

type User = {
  initials: string
  nome: string
  email: string
  perfil: 'Administrador' | 'Supervisor' | 'Planejador' | 'Técnico'
  status: 'Online' | 'Em serviço' | 'Offline' | 'Indisponível'
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'

const isPanelOpen = ref(false)
const isSubmitting = ref(false)
const apiMessage = ref('')
const contractsFromApiLoaded = ref(false)

const newContract = reactive({
  nomeEmpresa: '',
  unidade: '',
  dataInicio: '',
  dataFim: '',
  quantidadePlantas: 1,
  quantidadeAtivos: 0,
  quantidadeSupervisoes: 1,
  descricao: '',
  criadoPor: 'Rafael Gomes',
})

const contracts = ref<Contract[]>([
  {
    id: 1,
    nomeEmpresa: 'Petrobras S.A.',
    unidade: 'Rio de Janeiro / RJ',
    dataInicio: '2023-03-01',
    dataFim: '2026-02-28',
    quantidadePlantas: 3,
    quantidadeAtivos: 124,
    ordensAbertas: 8,
    status: 'Ativo',
    descricao: 'Plataforma P-63 + Replan + REDUC',
  },
  {
    id: 2,
    nomeEmpresa: 'Vale S.A.',
    unidade: 'Belo Horizonte / MG',
    dataInicio: '2023-06-15',
    dataFim: '2026-06-14',
    quantidadePlantas: 2,
    quantidadeAtivos: 87,
    ordensAbertas: 3,
    status: 'Ativo',
    descricao: 'Operações de mineração e supervisão de ativos remotos',
  },
  {
    id: 3,
    nomeEmpresa: 'Volkswagen do Brasil',
    unidade: 'São Bernardo / SP',
    dataInicio: '2022-09-01',
    dataFim: '2025-08-31',
    quantidadePlantas: 1,
    quantidadeAtivos: 45,
    ordensAbertas: 2,
    status: 'Expirando',
    descricao: 'Planta industrial com prioridade de renovação contratual',
  },
  {
    id: 4,
    nomeEmpresa: 'Embraer S.A.',
    unidade: 'São José dos Campos / SP',
    dataInicio: '2024-01-10',
    dataFim: '2027-01-09',
    quantidadePlantas: 2,
    quantidadeAtivos: 56,
    ordensAbertas: 1,
    status: 'Ativo',
    descricao: 'Contrato de manutenção e instalação de sensores',
  },
  {
    id: 5,
    nomeEmpresa: 'Ambev',
    unidade: 'São Paulo / SP',
    dataInicio: '2023-04-05',
    dataFim: '2026-04-04',
    quantidadePlantas: 4,
    quantidadeAtivos: 0,
    ordensAbertas: 0,
    status: 'Inativo',
    descricao: 'Contrato em espera para ativação de ativos',
  },
])

const users: User[] = [
  {
    initials: 'CS',
    nome: 'Carlos Santos',
    email: 'carlos@altave.com.br',
    perfil: 'Técnico',
    status: 'Em serviço',
  },
  {
    initials: 'LM',
    nome: 'Lívia Mendes',
    email: 'livia@altave.com.br',
    perfil: 'Supervisor',
    status: 'Online',
  },
  {
    initials: 'TN',
    nome: 'Thiago Neves',
    email: 'thiago@altave.com.br',
    perfil: 'Planejador',
    status: 'Offline',
  },
]

const alerts = [
  'Contrato Volkswagen expira em 47 dias. Renovação pendente.',
  '8 ordens atrasadas no contrato Petrobras aguardam reatribuição.',
  '18 novos ativos cadastrados este mês. Preventivas ainda não agendadas.',
]

const assignmentRows = [
  { nome: 'Carlos Santos', cargo: 'Técnico manutenção', contrato: 'Petrobras · Vale', status: 'Em serviço' },
  { nome: 'Lívia Mendes', cargo: 'Supervisora', contrato: 'Petrobras', status: 'Online' },
  { nome: 'Thiago Neves', cargo: 'Planejador', contrato: 'Petrobras · Vale · Embraer', status: 'Offline' },
]

const activeContracts = computed(() => contracts.value.filter((contract) => contract.status !== 'Inativo'))
const totalAssets = computed(() => contracts.value.reduce((sum, contract) => sum + contract.quantidadeAtivos, 0))
const openOrders = computed(() => contracts.value.reduce((sum, contract) => sum + contract.ordensAbertas, 0))

const metrics = computed(() => [
  {
    label: 'Total de Contratos',
    value: contracts.value.length,
    delta: '+2 este mês',
    tone: 'blue',
  },
  {
    label: 'Total de Ativos',
    value: totalAssets.value,
    delta: '+18 este mês',
    tone: 'green',
  },
  {
    label: 'Total de Usuários',
    value: 47,
    delta: '+3 novos',
    tone: 'amber',
  },
  {
    label: 'Ordens em Andamento',
    value: openOrders.value + 28,
    delta: '14 atrasadas',
    tone: 'red',
  },
])

onMounted(() => {
  loadContracts()
})

function openPanel() {
  isPanelOpen.value = true
}

function closePanel() {
  isPanelOpen.value = false
}

async function loadContracts() {
  try {
    const response = await fetch(`${API_BASE_URL}/contrato`)

    if (!response.ok) {
      throw new Error('Não foi possível carregar contratos do backend.')
    }

    const apiContracts = (await response.json()) as ContractApi[]

    if (apiContracts.length > 0) {
      contracts.value = apiContracts.map((contract) => mapApiContract(contract))
      contractsFromApiLoaded.value = true
      apiMessage.value = 'Contratos carregados da API.'
    }
  } catch {
    contractsFromApiLoaded.value = false
    apiMessage.value = 'Exibindo dados demonstrativos. Para usar a API, rode o backend na porta 8080 ou configure VITE_API_BASE_URL.'
  }
}

async function submitContract() {
  isSubmitting.value = true

  const payload = {
    nomeEmpresa: newContract.nomeEmpresa,
    quantidadePlanta: Number(newContract.quantidadePlantas),
    dataInicio: newContract.dataInicio,
    dataFim: newContract.dataFim,
    quantidadeAtivos: Number(newContract.quantidadeAtivos),
    descricao: newContract.descricao,
  }

  try {
    const response = await fetch(`${API_BASE_URL}/contrato`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(payload),
    })

    if (!response.ok) {
      throw new Error('Falha ao salvar contrato.')
    }

    const savedContract = (await response.json()) as ContractApi
    contracts.value.unshift(mapApiContract(savedContract, newContract.unidade))
    apiMessage.value = 'Contrato criado e salvo na API.'
  } catch {
    contracts.value.unshift({
      id: Date.now(),
      nomeEmpresa: newContract.nomeEmpresa,
      unidade: newContract.unidade || 'Unidade não informada',
      dataInicio: newContract.dataInicio,
      dataFim: newContract.dataFim,
      quantidadePlantas: Number(newContract.quantidadePlantas),
      quantidadeAtivos: Number(newContract.quantidadeAtivos),
      ordensAbertas: 0,
      status: 'Ativo',
      descricao: newContract.descricao,
    })
    apiMessage.value = 'Backend indisponível: contrato adicionado somente na tela para prototipação.'
  } finally {
    isSubmitting.value = false
    resetForm()
    closePanel()
  }
}

function mapApiContract(contract: ContractApi, unidade = 'Unidade não informada'): Contract {
  return {
    id: contract.id ?? Date.now(),
    nomeEmpresa: contract.nomeEmpresa ?? 'Empresa sem nome',
    unidade,
    dataInicio: contract.dataInicio ?? '',
    dataFim: contract.dataFim ?? '',
    quantidadePlantas: contract.quantidadePlanta ?? 0,
    quantidadeAtivos: contract.quantidadeAtivos ?? 0,
    ordensAbertas: 0,
    status: resolveContractStatus(contract.dataFim),
    descricao: contract.descricao ?? 'Sem descrição cadastrada',
  }
}

function resolveContractStatus(endDate?: string): ContractStatus {
  if (!endDate) {
    return 'Ativo'
  }

  const today = new Date()
  const finish = new Date(`${endDate}T00:00:00`)
  const daysToFinish = Math.ceil((finish.getTime() - today.getTime()) / (1000 * 60 * 60 * 24))

  if (daysToFinish < 0) {
    return 'Inativo'
  }

  if (daysToFinish <= 60) {
    return 'Expirando'
  }

  return 'Ativo'
}

function resetForm() {
  newContract.nomeEmpresa = ''
  newContract.unidade = ''
  newContract.dataInicio = ''
  newContract.dataFim = ''
  newContract.quantidadePlantas = 1
  newContract.quantidadeAtivos = 0
  newContract.quantidadeSupervisoes = 1
  newContract.descricao = ''
  newContract.criadoPor = 'Rafael Gomes'
}

function formatDate(date: string) {
  if (!date) {
    return '—'
  }

  return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(new Date(`${date}T00:00:00`))
}
</script>

<template>
  <div class="adm-page">
    <SidebarAdm />

    <main class="adm-main">
      <header class="topbar">
        <div>
          <span class="topbar-title">Dashboard ADM</span>
          <span class="topbar-sub">Visão geral do sistema</span>
        </div>
        <div class="topbar-actions">
          <button class="btn btn-secondary btn-sm" type="button">
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="4" width="18" height="18" rx="2" />
              <line x1="16" y1="2" x2="16" y2="6" />
              <line x1="8" y1="2" x2="8" y2="6" />
              <line x1="3" y1="10" x2="21" y2="10" />
            </svg>
            Abril 2025
          </button>
          <button class="btn btn-primary btn-sm" type="button" @click="openPanel">
            <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="12" y1="5" x2="12" y2="19" />
              <line x1="5" y1="12" x2="19" y2="12" />
            </svg>
            Criar Contrato
          </button>
        </div>
      </header>

      <section class="content-area adm-content">
        <div class="page-intro">
          <div>
            <p class="eyebrow">Administrador</p>
            <h1>Gestão geral de contratos, ativos, usuários e ordens</h1>
            <p>
              O ADM visualiza todos os contratos, acompanha ativos e colaboradores e faz a atribuição de pessoas aos contratos.
            </p>
          </div>
          <div class="health-card">
            <span class="health-label">Status da base</span>
            <strong>{{ contractsFromApiLoaded ? 'API conectada' : 'Modo protótipo' }}</strong>
            <small>{{ apiMessage }}</small>
          </div>
        </div>

        <section class="metrics-grid" aria-label="Indicadores principais">
          <article v-for="metric in metrics" :key="metric.label" class="metric-card" :class="metric.tone">
            <div class="metric-label">{{ metric.label }}</div>
            <div class="metric-value">{{ metric.value }}</div>
            <div class="metric-delta" :class="metric.tone === 'red' ? 'delta-down' : 'delta-up'">{{ metric.delta }}</div>
          </article>
        </section>

        <section id="contratos" class="card contracts-card">
          <div class="card-header">
            <div>
              <div class="card-title">Contratos Ativos</div>
              <div class="card-sub">{{ activeContracts.length }} contratos ativos ou em renovação</div>
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
                  <th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="contract in contracts" :key="contract.id">
                  <td>
                    <strong class="table-title">{{ contract.nomeEmpresa }}</strong>
                    <span class="table-subtitle">{{ contract.unidade }}</span>
                  </td>
                  <td>{{ formatDate(contract.dataInicio) }}</td>
                  <td>{{ formatDate(contract.dataFim) }}</td>
                  <td>{{ contract.quantidadePlantas }}</td>
                  <td>{{ contract.quantidadeAtivos }}</td>
                  <td>{{ contract.ordensAbertas || '—' }}</td>
                  <td>
                    <span class="badge" :class="{
                      'badge-active': contract.status === 'Ativo',
                      'badge-pending': contract.status === 'Expirando',
                      'badge-done': contract.status === 'Inativo',
                    }">
                      {{ contract.status }}
                    </span>
                  </td>
                  <td><button class="link-button" type="button">Ver →</button></td>
                </tr>
              </tbody>
            </table>
          </div>
        </section>

        <section class="dashboard-grid">
          <article id="usuarios" class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Usuários Recentes</div>
                <div class="card-sub">Perfis operacionais do sistema</div>
              </div>
              <button class="link-button" type="button">Ver todos</button>
            </div>

            <div class="users-list">
              <div v-for="user in users" :key="user.email" class="user-row">
                <div class="avatar avatar-blue">{{ user.initials }}</div>
                <div class="user-row-info">
                  <strong>{{ user.nome }}</strong>
                  <span>{{ user.email }}</span>
                </div>
                <div class="user-row-meta">
                  <span>{{ user.perfil }}</span>
                  <span class="status-pill">{{ user.status }}</span>
                </div>
              </div>
            </div>
          </article>

          <article class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Alertas do Sistema</div>
                <div class="card-sub">{{ alerts.length + 1 }} pendências monitoradas</div>
              </div>
            </div>

            <div v-for="alert in alerts" :key="alert" class="alert alert-warning">
              <span>⚠</span>
              <p>{{ alert }}</p>
            </div>
          </article>
        </section>

        <section class="dashboard-grid secondary-grid">
          <article id="ativos" class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Resumo de Ativos</div>
                <div class="card-sub">Indicadores consolidados para o ADM</div>
              </div>
            </div>

            <div class="asset-summary">
              <div>
                <span>Ativos operando</span>
                <strong>284</strong>
                <div class="progress-bar-wrap"><div class="progress-bar progress-green" style="width: 91%"></div></div>
              </div>
              <div>
                <span>Em manutenção</span>
                <strong>23</strong>
                <div class="progress-bar-wrap"><div class="progress-bar progress-blue" style="width: 35%"></div></div>
              </div>
              <div>
                <span>Instalação pendente</span>
                <strong>5</strong>
                <div class="progress-bar-wrap"><div class="progress-bar progress-blue" style="width: 18%"></div></div>
              </div>
            </div>
          </article>

          <article id="ordens" class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Atribuição de Pessoas a Contratos</div>
                <div class="card-sub">Coluna de contrato editável no próximo passo</div>
              </div>
            </div>

            <div class="assignment-list">
              <div v-for="row in assignmentRows" :key="row.nome" class="assignment-row">
                <div>
                  <strong>{{ row.nome }}</strong>
                  <span>{{ row.cargo }}</span>
                </div>
                <select class="filter-select" :value="row.contrato" aria-label="Contrato atribuído">
                  <option>{{ row.contrato }}</option>
                  <option>Petrobras</option>
                  <option>Vale</option>
                  <option>Volkswagen</option>
                  <option>Embraer</option>
                </select>
              </div>
            </div>
          </article>
        </section>
      </section>
    </main>

    <div class="drawer-backdrop" :class="{ open: isPanelOpen }" @click="closePanel"></div>

    <aside class="detail-overlay contract-drawer" :class="{ open: isPanelOpen }" aria-label="Criar contrato">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="closePanel">✕</button>
        <strong>Criar Contrato</strong>
        <p>Cadastre os dados iniciais do contrato. Depois, será possível atribuir pessoas e criar ativos.</p>
      </div>

      <form class="detail-body contract-form" @submit.prevent="submitContract">
        <label>
          Nome da empresa *
          <input v-model="newContract.nomeEmpresa" type="text" placeholder="Ex.: Petrobras S.A." required />
        </label>

        <label>
          Unidade / Sede
          <input v-model="newContract.unidade" type="text" placeholder="Ex.: Rio de Janeiro / RJ" />
        </label>

        <div class="form-grid">
          <label>
            Data início *
            <input v-model="newContract.dataInicio" type="date" required />
          </label>

          <label>
            Data fim *
            <input v-model="newContract.dataFim" type="date" required />
          </label>
        </div>

        <div class="form-grid">
          <label>
            Plantas *
            <input v-model.number="newContract.quantidadePlantas" type="number" min="1" required />
          </label>

          <label>
            Ativos
            <input v-model.number="newContract.quantidadeAtivos" type="number" min="0" />
          </label>
        </div>

        <label>
          Supervisões
          <input v-model.number="newContract.quantidadeSupervisoes" type="number" min="0" />
        </label>

        <label>
          Descrição *
          <textarea v-model="newContract.descricao" rows="4" placeholder="Escopo do contrato, plantas envolvidas e observações" required></textarea>
        </label>

        <label>
          Criado por
          <input v-model="newContract.criadoPor" type="text" />
        </label>

        <div class="drawer-actions">
          <button class="btn btn-secondary" type="button" @click="closePanel">Cancelar</button>
          <button class="btn btn-primary" type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? 'Salvando...' : 'Salvar contrato' }}
          </button>
        </div>
      </form>
    </aside>
  </div>
</template>

<style scoped>
.adm-page {
  min-height: 100vh;
  background: var(--gray-50);
}

.adm-main {
  min-height: 100vh;
  margin-left: var(--sidebar-w);
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

.page-intro {
  display: flex;
  align-items: stretch;
  justify-content: space-between;
  gap: 18px;
  margin-bottom: 18px;
}

.page-intro h1 {
  font-size: 24px;
  line-height: 1.2;
  color: var(--gray-900);
  margin-bottom: 8px;
}

.page-intro p {
  color: var(--gray-500);
  max-width: 680px;
}

.eyebrow {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.09em;
  color: var(--blue-600) !important;
  text-transform: uppercase;
  margin-bottom: 6px;
}

.health-card {
  width: 280px;
  background: var(--blue-50);
  border: 1px solid var(--blue-100);
  border-radius: var(--radius-lg);
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.health-label {
  font-size: 11px;
  color: var(--gray-500);
  text-transform: uppercase;
  letter-spacing: 0.06em;
  font-weight: 700;
}

.health-card strong {
  color: var(--blue-700);
}

.health-card small {
  color: var(--gray-500);
  line-height: 1.35;
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

.dashboard-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.25fr) minmax(320px, 0.75fr);
  gap: 16px;
  margin-bottom: 16px;
}

.secondary-grid {
  grid-template-columns: minmax(320px, 0.8fr) minmax(0, 1.2fr);
}

.users-list,
.assignment-list,
.asset-summary {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.user-row,
.assignment-row {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  background: var(--gray-50);
}

.user-row-info,
.user-row-meta,
.assignment-row > div {
  display: flex;
  flex-direction: column;
}

.user-row-info {
  flex: 1;
}

.user-row-info span,
.user-row-meta span,
.assignment-row span {
  font-size: 12px;
  color: var(--gray-500);
}

.user-row-meta {
  align-items: flex-end;
  gap: 4px;
}

.status-pill {
  background: var(--green-bg);
  color: #15803d !important;
  border-radius: 20px;
  padding: 2px 8px;
  font-weight: 600;
}

.asset-summary > div {
  display: grid;
  grid-template-columns: 1fr auto;
  gap: 8px 14px;
  align-items: center;
  padding: 12px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
}

.asset-summary span {
  color: var(--gray-500);
}

.asset-summary strong {
  color: var(--gray-900);
  font-size: 18px;
}

.asset-summary .progress-bar-wrap {
  grid-column: 1 / -1;
}

.assignment-row {
  justify-content: space-between;
}

.assignment-row .filter-select {
  min-width: 220px;
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

@media (max-width: 1024px) {
  .metrics-grid,
  .dashboard-grid,
  .secondary-grid {
    grid-template-columns: 1fr 1fr;
  }

  .page-intro {
    flex-direction: column;
  }

  .health-card {
    width: 100%;
  }
}

@media (max-width: 860px) {
  .adm-main {
    margin-left: 0;
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

  .metrics-grid,
  .dashboard-grid,
  .secondary-grid {
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
