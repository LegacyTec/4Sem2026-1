<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { computed, reactive, ref } from 'vue'

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

const isPanelOpen = ref(false)
const isSubmitting = ref(false)

/** Formulário alinhado ao payload futuro da API (somente front por enquanto). */
const novoContrato = reactive({
  nomeEmpresa: '',
  dataInicio: '',
  dataFim: '',
  quantidadePlantas: 1,
  descricao: '',
})

/** Populado via GET /contratos (ou equivalente) quando integrar o backend. */
const contratos = ref<Contract[]>([])

/** Somente contratos; totais de usuários/ordens globais virão de outros endpoints. */
const totalUsuarios = ref(0)

const totalAtivos = computed(() => contratos.value.reduce((acc, c) => acc + c.quantidadeAtivos, 0))
const ordensAbertasSum = computed(() => contratos.value.reduce((acc, c) => acc + c.ordensAbertas, 0))

const metricas = computed(() => [
  { label: 'Total de Contratos', value: contratos.value.length, tone: 'blue' as const },
  { label: 'Total de Ativos', value: totalAtivos.value, tone: 'green' as const },
  { label: 'Total de Usuários', value: totalUsuarios.value, tone: 'amber' as const },
  { label: 'Ordens em Andamento', value: ordensAbertasSum.value, tone: 'red' as const },
])

function abrirPainel() {
  isPanelOpen.value = true
}

function fecharPainel() {
  isPanelOpen.value = false
}

/** TODO: substituir por POST ao backend; hoje só atualiza o estado local após validação. */
function salvarContrato() {
  isSubmitting.value = true
  contratos.value.unshift({
    id: Date.now(),
    nomeEmpresa: novoContrato.nomeEmpresa.trim(),
    unidade: '',
    dataInicio: novoContrato.dataInicio,
    dataFim: novoContrato.dataFim,
    quantidadePlantas: Number(novoContrato.quantidadePlantas),
    quantidadeAtivos: 0,
    ordensAbertas: 0,
    status: resolverStatusContrato(novoContrato.dataFim),
    descricao: novoContrato.descricao.trim(),
  })
  isSubmitting.value = false
  limparFormulario()
  fecharPainel()
}

function resolverStatusContrato(dataFim: string): ContractStatus {
  if (!dataFim) return 'Ativo'
  const hoje = new Date()
  const fim = new Date(`${dataFim}T00:00:00`)
  const dias = Math.ceil((fim.getTime() - hoje.getTime()) / (1000 * 60 * 60 * 24))
  if (dias < 0) return 'Inativo'
  if (dias <= 60) return 'Expirando'
  return 'Ativo'
}

function limparFormulario() {
  novoContrato.nomeEmpresa = ''
  novoContrato.dataInicio = ''
  novoContrato.dataFim = ''
  novoContrato.quantidadePlantas = 1
  novoContrato.descricao = ''
}

function formatarData(iso: string) {
  if (!iso) return '—'
  return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(new Date(`${iso}T00:00:00`))
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
                      Nenhum contrato ainda. Cadastre pelo botão <strong>Criar Contrato</strong> ou carregue os dados quando a API estiver disponível.
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
          <button class="btn btn-secondary" type="button" @click="fecharPainel">Cancelar</button>
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