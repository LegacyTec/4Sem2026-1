<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarPlanejador from '@/components/geral/layout/SidebarPlanejador.vue'
import { computed, reactive, ref } from 'vue'

/** Contadores exibidos nos KPIs — substituir por resposta da API (totais agregados). */
const ordensBacklog = ref(0)
const ordensAtrasadas = ref(0)
const ordensEmAndamento = ref(0)

type OrdemLinha = {
  id: string
  nomeOrdem: string
  tipo: string
  ativo: string
  tecnicos: string
  inicio: string
  prazo: string
  status: string
}

const ordens = ref<OrdemLinha[]>([])

const busca = ref('')
const filtroTipo = ref('')
const filtroStatus = ref('')
const filtroContrato = ref('')

const isPanelOpen = ref(false)
const isSubmitting = ref(false)

const novaOrdem = reactive({
  nomeOrdem: '',
  descricao: '',
  planta: '',
  predio: '',
  dataInicio: '',
  dataFim: '',
  ativo: '',
  tipoManutencao: '',
  requisito: '',
  tecnicosIds: [] as string[],
})

/** Opções para multi-select de técnicos — preencher via GET (equipe / usuários). */
const tecnicosDisponiveis = ref<{ id: string; nome: string }[]>([])

const metricas = computed(() => [
  { label: 'Ordens no backlog', value: ordensBacklog.value, tone: 'blue' as const },
  { label: 'Ordens atrasadas', value: ordensAtrasadas.value, tone: 'red' as const },
  { label: 'Ordens em andamento', value: ordensEmAndamento.value, tone: 'amber' as const },
])

const tiposManutencao = [
  { value: 'preventiva', label: 'Preventiva' },
  { value: 'corretiva', label: 'Corretiva' },
  { value: 'instalacao', label: 'Instalação' },
  { value: 'predial', label: 'Predial' },
]

function abrirPainel() {
  isPanelOpen.value = true
}

function fecharPainel() {
  isPanelOpen.value = false
}

function limparFormulario() {
  novaOrdem.nomeOrdem = ''
  novaOrdem.descricao = ''
  novaOrdem.planta = ''
  novaOrdem.predio = ''
  novaOrdem.dataInicio = ''
  novaOrdem.dataFim = ''
  novaOrdem.ativo = ''
  novaOrdem.tipoManutencao = ''
  novaOrdem.requisito = ''
  novaOrdem.tecnicosIds = []
}

/** TODO: POST /ordens-manutencao (ou rota definida pelo backend). */
function salvarOrdem() {
  isSubmitting.value = true

  const sel = tecnicosDisponiveis.value.filter((t) => novaOrdem.tecnicosIds.includes(t.id))
  const tecnicosLabel = sel.length ? sel.map((t) => t.nome).join(', ') : '—'

  ordens.value.unshift({
    id: String(Date.now()),
    nomeOrdem: novaOrdem.nomeOrdem.trim(),
    tipo: labelTipo(novaOrdem.tipoManutencao) || '—',
    ativo: novaOrdem.ativo.trim(),
    tecnicos: tecnicosLabel,
    inicio: novaOrdem.dataInicio || '—',
    prazo: novaOrdem.dataFim || '—',
    status: 'Pendente',
  })

  isSubmitting.value = false
  limparFormulario()
  fecharPainel()
}

function labelTipo(value: string) {
  if (!value) return ''
  return tiposManutencao.find((t) => t.value === value)?.label ?? ''
}

function formatarData(iso: string) {
  if (!iso || iso === '—') return '—'
  return new Intl.DateTimeFormat('pt-BR', { timeZone: 'UTC' }).format(new Date(`${iso}T00:00:00`))
}

function classeBadgeTipo(tipo: string) {
  const t = tipo.toLowerCase()
  if (t.includes('corretiva')) return 'badge-late'
  if (t.includes('instala')) return 'badge-install'
  if (t.includes('preventiva')) return 'badge-progress'
  return 'badge-pending'
}

function classeBadgeStatus(status: string) {
  const s = status.toLowerCase()
  if (s.includes('atras')) return 'badge-late'
  if (s.includes('andament')) return 'badge-progress'
  if (s.includes('pend')) return 'badge-pending'
  if (s.includes('conclu')) return 'badge-active'
  return 'badge-done'
}
</script>

<template>
  <div class="plnj-layout">
    <AppHeaderRoles />

    <div class="plnj-workspace">
      <SidebarPlanejador />

      <main class="plnj-main">
        <header class="topbar">
          <div class="topbar-head">
            <div>
              <span class="topbar-title">Ordens de Manutenção</span>
              <span class="topbar-sub">Planejamento e acompanhamento</span>
            </div>
          </div>
          <div class="topbar-actions">
            <button type="button" class="btn btn-secondary btn-sm" disabled title="Integração com API">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3" />
              </svg>
              Filtros
            </button>
            <button type="button" class="btn btn-primary btn-sm" @click="abrirPainel">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Criar Ordem
            </button>
          </div>
        </header>

        <section class="content-area plnj-content">
          <section class="metrics-grid metrics-grid--3" aria-label="Indicadores de ordens">
            <article v-for="m in metricas" :key="m.label" class="metric-card" :class="m.tone">
              <div class="metric-label">{{ m.label }}</div>
              <div class="metric-value">{{ m.value }}</div>
            </article>
          </section>

          <section class="action-cards" aria-label="Atalhos">
            <button type="button" class="action-card" @click="abrirPainel">
              <svg class="action-card-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              <span class="action-card-label">Criar Ordem</span>
            </button>
            <button type="button" class="action-card action-card--muted" disabled title="Em breve">
              <svg class="action-card-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2" />
                <circle cx="12" cy="7" r="4" />
              </svg>
              <span class="action-card-label">Minha Equipe</span>
            </button>
            <button type="button" class="action-card action-card--muted" disabled title="Em breve">
              <svg class="action-card-svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                <circle cx="12" cy="5" r="3" />
                <path d="M12 8v4M8 12H4a8 8 0 0 0 16 0h-4" />
              </svg>
              <span class="action-card-label">Ativos</span>
            </button>
            <button type="button" class="action-card action-card--muted" disabled title="Em breve">
              <svg class="action-card-svg action-card-svg--danger" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" aria-hidden="true">
                <circle cx="12" cy="12" r="10" />
                <path d="M12 6v6l4 2" />
              </svg>
              <span class="action-card-label">Atrasadas</span>
            </button>
          </section>

          <section id="lista-ordens" class="card">
            <div class="card-header">
              <div>
                <div class="card-title">Todas as ordens</div>
                <div class="card-sub">
                  {{ ordens.length === 0 ? 'Nenhuma ordem carregada' : `${ordens.length} ordem(ns)` }}
                </div>
              </div>
            </div>

            <div class="table-toolbar">
              <input
                v-model="busca"
                type="search"
                class="table-search"
                placeholder="Buscar ordem ou ativo..."
                disabled
                title="Habilitar após integração"
              />
              <select v-model="filtroTipo" class="filter-select" disabled title="Integração com API">
                <option value="">Todos os tipos</option>
              </select>
              <select v-model="filtroStatus" class="filter-select" disabled title="Integração com API">
                <option value="">Todos os status</option>
              </select>
              <select v-model="filtroContrato" class="filter-select" disabled title="Integração com API">
                <option value="">Todos os contratos</option>
              </select>
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Nome da ordem</th>
                    <th>Tipo</th>
                    <th>Ativo</th>
                    <th>Técnico(s)</th>
                    <th>Início</th>
                    <th>Prazo</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="ordens.length === 0">
                    <td colspan="8" class="table-empty">
                      Nenhuma ordem listada. Use <strong>Criar Ordem</strong> para testar o fluxo localmente ou aguarde o carregamento pela API.
                    </td>
                  </tr>
                  <template v-else>
                    <tr v-for="o in ordens" :key="o.id">
                      <td>{{ o.id }}</td>
                      <td><strong>{{ o.nomeOrdem }}</strong></td>
                      <td>
                        <span class="badge" :class="classeBadgeTipo(o.tipo)">{{ o.tipo }}</span>
                      </td>
                      <td>{{ o.ativo }}</td>
                      <td>{{ o.tecnicos }}</td>
                      <td>{{ formatarData(o.inicio) }}</td>
                      <td :class="{ 'cell-late': o.prazo !== '—' && o.status === 'Atrasado' }">
                        {{ formatarData(o.prazo) }}
                      </td>
                      <td>
                        <span class="badge" :class="classeBadgeStatus(o.status)">{{ o.status }}</span>
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

    <div class="drawer-backdrop" :class="{ open: isPanelOpen }" @click="fecharPainel" />

    <aside class="detail-overlay ordem-drawer" :class="{ open: isPanelOpen }" aria-label="Nova ordem de manutenção">
      <div class="detail-header">
        <button type="button" class="detail-close" @click="fecharPainel">✕</button>
        <strong>Nova ordem de manutenção</strong>
        <p>Preencha os campos. Os dados serão enviados ao backend quando a integração estiver pronta.</p>
      </div>

      <form class="detail-body ordem-form" @submit.prevent="salvarOrdem">
        <label>
          Nome da ordem *
          <input v-model="novaOrdem.nomeOrdem" type="text" required placeholder="Identificação da ordem" />
        </label>

        <label>
          Descrição *
          <textarea v-model="novaOrdem.descricao" rows="3" required placeholder="Detalhes da intervenção" />
        </label>

        <label>
          Planta *
          <input v-model="novaOrdem.planta" type="text" required placeholder="Planta (virá de cadastro via API)" />
        </label>

        <label>
          Prédio *
          <input v-model="novaOrdem.predio" type="text" required placeholder="Prédio (virá de cadastro via API)" />
        </label>

        <div class="form-grid">
          <label>
            Data prevista para início
            <input v-model="novaOrdem.dataInicio" type="date" />
          </label>
          <label>
            Data prevista para final
            <input v-model="novaOrdem.dataFim" type="date" />
          </label>
        </div>

        <label>
          Ativo *
          <input v-model="novaOrdem.ativo" type="text" required placeholder="Identificação do ativo" />
        </label>

        <label>
          Técnicos envolvidos
          <select v-model="novaOrdem.tecnicosIds" class="form-multiselect" multiple size="4">
            <option v-for="t in tecnicosDisponiveis" :key="t.id" :value="t.id">{{ t.nome }}</option>
          </select>
          <span v-if="tecnicosDisponiveis.length === 0" class="field-hint">Lista de técnicos será carregada pela API.</span>
        </label>

        <label>
          Tipo de manutenção
          <select v-model="novaOrdem.tipoManutencao">
            <option value="">Selecione</option>
            <option v-for="opt in tiposManutencao" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
          </select>
        </label>

        <label>
          Requisito
          <textarea v-model="novaOrdem.requisito" rows="3" placeholder="Requisitos de segurança, EPI, isolamento, etc." />
        </label>

        <div class="drawer-actions">
          <button type="button" class="btn btn-secondary" @click="fecharPainel">Cancelar</button>
          <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
            {{ isSubmitting ? 'Salvando...' : 'Salvar ordem' }}
          </button>
        </div>
      </form>
    </aside>
  </div>
</template>

<style scoped>
.plnj-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--gray-50);
}

.plnj-workspace {
  display: flex;
  flex: 1;
  min-height: calc(100vh - var(--app-header-roles-h));
}

.plnj-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

.topbar {
  height: auto;
  min-height: var(--topbar-h);
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 24px;
  gap: 16px;
  flex-wrap: wrap;
  flex-shrink: 0;
  position: sticky;
  top: 0;
  z-index: 10;
}

.topbar-head {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.topbar-title {
  font-size: 17px;
  font-weight: 600;
  color: var(--gray-900);
}

.topbar-sub {
  font-size: 12px;
  color: var(--gray-500);
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.plnj-content {
  padding: 24px;
}

.action-cards {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
  margin-bottom: 20px;
}

.action-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 22px 16px;
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-lg);
  box-shadow: var(--shadow);
  cursor: pointer;
  font-family: var(--font);
  transition: border-color 0.15s, box-shadow 0.15s;
}

.action-card:not(:disabled):hover {
  border-color: var(--blue-200);
  box-shadow: var(--shadow-md);
}

.action-card--muted {
  opacity: 0.55;
  cursor: not-allowed;
}

.action-card-svg {
  width: 28px;
  height: 28px;
  color: var(--blue-600);
}

.action-card-svg--danger {
  color: var(--red);
}

.action-card-label {
  font-size: 13px;
  font-weight: 600;
  color: var(--gray-800);
}

.table-toolbar {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 16px;
  align-items: center;
}

.table-search {
  flex: 1;
  min-width: 200px;
  padding: 9px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit;
  font-size: 13px;
}

.filter-select {
  padding: 9px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit;
  font-size: 13px;
  min-width: 150px;
  background: var(--white);
}

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
  line-height: 1.5;
}

.cell-late {
  color: var(--red);
  font-weight: 600;
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

.ordem-drawer {
  display: block;
}

.detail-header p {
  margin-top: 6px;
  color: var(--gray-500);
  font-size: 13px;
  max-width: 380px;
}

.ordem-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.ordem-form label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  color: var(--gray-700);
  font-size: 12px;
  font-weight: 600;
}

.ordem-form input,
.ordem-form textarea,
.ordem-form select {
  padding: 11px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  background: var(--white);
}

.ordem-form input:focus,
.ordem-form textarea:focus,
.ordem-form select:focus {
  border-color: var(--blue-400);
  box-shadow: 0 0 0 3px var(--blue-50);
}

.form-multiselect {
  min-height: 96px;
}

.field-hint {
  font-size: 11px;
  font-weight: 400;
  color: var(--gray-500);
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

@media (max-width: 1024px) {
  .action-cards {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 860px) {
  .plnj-workspace {
    flex-direction: column;
  }

  .action-cards {
    grid-template-columns: 1fr;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .ordem-drawer {
    width: min(420px, 100vw);
  }
}
</style>
