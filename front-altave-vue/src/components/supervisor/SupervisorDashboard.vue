<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarSupervisor from '@/components/geral/layout/SidebarSupervisor.vue'
import { buscarAtivosPorContrato } from '@/services/AtivoService'
import type { IAtivo } from '@/services/AtivoService'
import {
  calcularMtbfMedio,
  calcularMttrMedio,
  formatarMtbf,
  formatarMttr,
  isEmManutencao,
  isOperacional,
} from '@/services/AtivoMetricService'
import { buscarContratos, resolverStatus } from '@/services/ContratoService'
import {
  buscarOrdens,
  formatarData,
  iniciaisNome,
  avatarColor,
  labelTipo,
  labelStatus,
} from '@/services/OrdemService'
import type { IContrato } from '@/services/ContratoService'
import type { IOrdem } from '@/services/OrdemService'
import { computed, onMounted, ref } from 'vue'

// ── Estado ────────────────────────────────────
const contratos = ref<IContrato[]>([])
const ativos = ref<IAtivo[]>([])
const ordens = ref<IOrdem[]>([])
const isLoading = ref(false)
const filtroTipo = ref('')
const filtroStatus = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    const c = await buscarContratos()
    contratos.value = c

    const contrato = c.find((ct) => resolverStatus(ct.dataFim) === 'Ativo') ?? c[0] ?? null
    const [ativosData, o] = await Promise.all([
      contrato ? buscarAtivosPorContrato(contrato.id) : Promise.resolve([]),
      buscarOrdens(),
    ])
    ativos.value = ativosData
    ordens.value = o
  } catch (e) {
    console.error('Erro ao carregar dados do supervisor:', e)
  } finally {
    isLoading.value = false
  }
})

// ── Contrato ──────────────────────────────────
const contratoAtivo = computed<IContrato | null>(() =>
  contratos.value.find(c => resolverStatus(c.dataFim) === 'Ativo') ?? contratos.value[0] ?? null,
)

const nomeContrato = computed(() => contratoAtivo.value?.nomeEmpresa ?? 'Contrato')

const plantasContrato = computed(() => {
  const s = new Set<string>()
  ativos.value.forEach((a) => { if (a.planta) s.add(a.planta) })
  return [...s].join(' + ') || 'Plantas do contrato'
})

const idsAtivosContrato = computed(() => new Set(ativos.value.map((a) => a.id)))

const ordensContrato = computed(() =>
  ordens.value.filter((o) => o.ativo?.id != null && idsAtivosContrato.value.has(o.ativo.id)),
)

// ── KPIs ──────────────────────────────────────
const totalAtivos = computed(() => ativos.value.length)
const emManutencao = computed(() => ativos.value.filter((a) => isEmManutencao(a.status)).length)
const ativosOperando = computed(() => ativos.value.filter((a) => isOperacional(a.status)).length)

const mttrMedio = computed(() => calcularMttrMedio(ativos.value, ordensContrato.value))
const mtbfMedio = computed(() => calcularMtbfMedio(ativos.value, ordensContrato.value))

const ordensAtrasadas = computed(() => {
  const hoje = new Date()
  hoje.setHours(0, 0, 0, 0)
  return ordensContrato.value.filter(o => {
    if (!o.dataFim) return false
    const fim = new Date(o.dataFim + 'T00:00:00')
    return fim < hoje && o.status !== 'CONCLUIDA' && o.status !== 'CANCELADA'
  })
})

const ativoComMaisOrdens = computed(() => {
  const count: Record<string, number> = {}
  const names: Record<string, string> = {}
  ordensContrato.value.forEach(o => {
    if (o.ativo?.id != null) {
      const k = String(o.ativo.id)
      count[k] = (count[k] ?? 0) + 1
      names[k] = o.ativo.tipo ?? `Ativo ${o.ativo.id}`
    }
  })
  let maxKey = ''
  let maxCount = 0
  Object.entries(count).forEach(([k, v]) => {
    if (v > maxCount) { maxCount = v; maxKey = k }
  })
  return { nome: maxKey ? (names[maxKey] ?? '—') : '—', count: maxCount }
})

// ── Gráfico barras ────────────────────────────
const MONTH_ABBR = ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov', 'Dez']

const last6Months = computed(() => {
  const now = new Date()
  return Array.from({ length: 6 }, (_, i) => {
    const d = new Date(now.getFullYear(), now.getMonth() - (5 - i), 1)
    return { year: d.getFullYear(), month: d.getMonth() }
  })
})

const chartData = computed(() =>
  last6Months.value.map(({ year, month }) => {
    const prev = ordensContrato.value.filter(o => {
      if (!o.dataInicio) return false
      const d = new Date(o.dataInicio + 'T00:00:00')
      return d.getFullYear() === year && d.getMonth() === month && o.tipoManutencao === 'PREVENTIVA'
    }).length
    const corr = ordensContrato.value.filter(o => {
      if (!o.dataInicio) return false
      const d = new Date(o.dataInicio + 'T00:00:00')
      return d.getFullYear() === year && d.getMonth() === month && o.tipoManutencao === 'CORRETIVA'
    }).length
    return { label: MONTH_ABBR[month] ?? '', prev, corr }
  }),
)

const maxBarValue = computed(() => {
  const vals = chartData.value.flatMap(d => [d.prev, d.corr])
  return Math.max(...vals, 1)
})

function barPx(v: number): number {
  return Math.max(3, Math.round((v / maxBarValue.value) * 100))
}

// ── Ativos em Alerta ──────────────────────────
type OrdemAlerta = IOrdem & { diasDecorridos: number; atrasado: boolean }

const ativosEmAlerta = computed<OrdemAlerta[]>(() => {
  const hoje = new Date()
  hoje.setHours(0, 0, 0, 0)
  const limite = new Date(hoje)
  limite.setDate(limite.getDate() + 7)

  return ordensContrato.value
    .filter(o => {
      if (o.status !== 'PENDENTE' || !o.dataFim) return false
      const fim = new Date(o.dataFim + 'T00:00:00')
      return fim <= limite
    })
    .map(o => {
      const inicio = new Date(o.dataInicio + 'T00:00:00')
      const fim = new Date((o.dataFim ?? o.dataInicio) + 'T00:00:00')
      const diasDecorridos = Math.floor((hoje.getTime() - inicio.getTime()) / 86_400_000)
      const atrasado = fim < hoje
      return { ...o, diasDecorridos, atrasado }
    })
})

const criticosCount = computed(() => ativosEmAlerta.value.filter(a => a.atrasado).length)

// ── Tabela de ordens ──────────────────────────
const ordensFiltradas = computed(() =>
  ordensContrato.value.filter(o => {
    const tipoOk = !filtroTipo.value || o.tipoManutencao === filtroTipo.value
    const statusOk = !filtroStatus.value || o.status === filtroStatus.value
    return tipoOk && statusOk
  }),
)

function padId(id: number): string {
  return String(id).padStart(4, '0')
}

function isLate(o: IOrdem): boolean {
  if (!o.dataFim || o.status === 'CONCLUIDA' || o.status === 'CANCELADA') return false
  const hoje = new Date()
  hoje.setHours(0, 0, 0, 0)
  return new Date(o.dataFim + 'T00:00:00') < hoje
}

function tipoBadgeClass(tipo: string): string {
  const m: Record<string, string> = {
    PREVENTIVA: 'tipo-prev',
    CORRETIVA: 'tipo-corr',
    INSTALACAO: 'tipo-inst',
    REMOCAO: 'tipo-rem',
  }
  return m[tipo] ?? ''
}

function statusBadgeClass(status: string): string {
  const m: Record<string, string> = {
    PENDENTE: 'badge-pending',
    EM_ANDAMENTO: 'badge-progress',
    CONCLUIDA: 'badge-active',
    CANCELADA: 'badge-done',
  }
  return m[status] ?? ''
}
</script>

<template>
  <div class="sv-layout">
    <AppHeaderRoles />

    <div class="sv-workspace">
      <SidebarSupervisor />

      <main class="sv-main">
        <!-- Topbar -->
        <header class="sv-topbar">
          <div class="topbar-left">
            <span class="topbar-title">{{ nomeContrato }}</span>
            <span class="topbar-sub">{{ plantasContrato }}</span>
          </div>
          <div class="topbar-actions">
            <button class="btn btn-secondary btn-sm" type="button" disabled title="Em breve">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
                <polyline points="14,2 14,8 20,8" />
                <line x1="16" y1="13" x2="8" y2="13" />
                <line x1="16" y1="17" x2="11" y2="17" />
              </svg>
              Relatório
            </button>
            <button class="btn btn-primary btn-sm" type="button" disabled title="Em breve">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19" />
                <line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              + Novo Ativo
            </button>
          </div>
        </header>

        <!-- Conteúdo principal -->
        <section class="content-area sv-content">

          <!-- KPI Cards -->
          <div class="kpi-grid">
            <article class="kpi-card kpi-blue">
              <div class="kpi-label">Ativos Operando</div>
              <div class="kpi-value">{{ ativosOperando }}</div>
              <div class="kpi-sub">de {{ totalAtivos }} ativos</div>
            </article>

            <article class="kpi-card kpi-amber">
              <div class="kpi-label">Em Manutenção</div>
              <div class="kpi-value">{{ emManutencao }}</div>
              <div class="kpi-sub">ativos em manutenção</div>
            </article>

            <article class="kpi-card kpi-purple">
              <div class="kpi-label">MTTR Médio</div>
              <div class="kpi-value">{{ formatarMttr(mttrMedio) }}</div>
              <div class="kpi-sub">tempo médio de reparo</div>
            </article>

            <article class="kpi-card kpi-green">
              <div class="kpi-label">MTBF Médio</div>
              <div class="kpi-value">{{ formatarMtbf(mtbfMedio) }}</div>
              <div class="kpi-sub">tempo médio entre falhas</div>
            </article>

            <article class="kpi-card kpi-red">
              <div class="kpi-label">Ordens Atrasadas</div>
              <div class="kpi-value">{{ ordensAtrasadas.length }}</div>
              <div class="kpi-sub">
                <span v-if="ordensAtrasadas.length > 0" class="badge badge-late">Ação necessária</span>
                <span v-else>Em dia</span>
              </div>
            </article>

            <article class="kpi-card kpi-green">
              <div class="kpi-label">Mais Manutenções</div>
              <div class="kpi-value kpi-value-name" :title="ativoComMaisOrdens.nome">
                {{ ativoComMaisOrdens.nome }}
              </div>
              <div class="kpi-sub">{{ ativoComMaisOrdens.count }} ordens no ano</div>
            </article>
          </div>

          <!-- Seção central: gráfico + alertas -->
          <div class="middle-grid">

            <!-- Gráfico Preventiva × Corretiva -->
            <div class="card chart-card">
              <div class="card-header">
                <div>
                  <div class="card-title">Preventiva × Corretiva</div>
                  <div class="card-sub">Últimos 6 meses</div>
                </div>
                <div class="chart-legend">
                  <div class="legend-item">
                    <span class="legend-dot dot-prev"></span>
                    <span class="legend-label">Preventiva</span>
                  </div>
                  <div class="legend-item">
                    <span class="legend-dot dot-corr"></span>
                    <span class="legend-label">Corretiva</span>
                  </div>
                </div>
              </div>

              <div class="bars-container">
                <div v-for="(d, i) in chartData" :key="i" class="bar-month-group">
                  <div class="bars-pair">
                    <div
                      class="bar bar-prev"
                      :style="{ height: barPx(d.prev) + 'px' }"
                      :title="`Preventiva: ${d.prev}`"
                    ></div>
                    <div
                      class="bar bar-corr"
                      :style="{ height: barPx(d.corr) + 'px' }"
                      :title="`Corretiva: ${d.corr}`"
                    ></div>
                  </div>
                </div>
              </div>

              <div class="month-labels-row">
                <div v-for="(d, i) in chartData" :key="i" class="month-label">{{ d.label }}</div>
              </div>
            </div>

            <!-- Ativos em Alerta -->
            <div class="card alert-card">
              <div class="card-header">
                <div class="card-title-row">
                  <span class="card-title">Ativos em Alerta</span>
                  <span v-if="criticosCount > 0" class="badge badge-late">{{ criticosCount }} críticos</span>
                </div>
              </div>

              <div class="table-wrap">
                <table>
                  <thead>
                    <tr>
                      <th>Ativo</th>
                      <th>Tipo</th>
                      <th>Último PM</th>
                      <th>Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-if="ativosEmAlerta.length === 0">
                      <td colspan="4" class="table-empty">Nenhum ativo em alerta</td>
                    </tr>
                    <tr v-for="a in ativosEmAlerta" :key="a.id">
                      <td>
                        <span class="asset-name">{{ a.ativo?.tipo ?? '—' }}</span>
                        <span class="asset-sub">{{ a.ativo?.planta ?? '' }}</span>
                      </td>
                      <td>
                        <span class="badge" :class="tipoBadgeClass(a.tipoManutencao)">
                          {{ labelTipo(a.tipoManutencao) }}
                        </span>
                      </td>
                      <td class="cell-mono">{{ formatarData(a.dataInicio) }}</td>
                      <td>
                        <span class="badge" :class="a.atrasado ? 'badge-late' : 'badge-pending'">
                          {{ a.atrasado ? 'Atrasado' : 'A vencer' }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <!-- Tabela de ordens -->
          <div class="card orders-card">
            <div class="card-header">
              <div>
                <div class="card-title">Ordens em Andamento</div>
                <div class="card-sub">todas as plantas</div>
              </div>
              <div class="header-filters">
                <select v-model="filtroTipo" class="filter-select">
                  <option value="">Todos os tipos</option>
                  <option value="PREVENTIVA">Preventiva</option>
                  <option value="CORRETIVA">Corretiva</option>
                  <option value="INSTALACAO">Instalação</option>
                  <option value="REMOCAO">Remoção</option>
                </select>
                <select v-model="filtroStatus" class="filter-select">
                  <option value="">Todos os status</option>
                  <option value="PENDENTE">Pendente</option>
                  <option value="EM_ANDAMENTO">Em andamento</option>
                  <option value="CONCLUIDA">Concluída</option>
                  <option value="CANCELADA">Cancelada</option>
                </select>
              </div>
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Ordem</th>
                    <th>Tipo</th>
                    <th>Ativo</th>
                    <th>Técnico Resp.</th>
                    <th>Início Previsto</th>
                    <th>Prazo</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="isLoading">
                    <td colspan="7" class="table-empty">Carregando ordens...</td>
                  </tr>
                  <tr v-else-if="ordensFiltradas.length === 0">
                    <td colspan="7" class="table-empty">Nenhuma ordem encontrada</td>
                  </tr>
                  <template v-else>
                    <tr v-for="o in ordensFiltradas" :key="o.id">
                      <td>
                        <span class="order-id">#OM-{{ padId(o.id) }}</span>
                      </td>
                      <td>
                        <span class="badge" :class="tipoBadgeClass(o.tipoManutencao)">
                          {{ labelTipo(o.tipoManutencao) }}
                        </span>
                      </td>
                      <td>{{ o.ativo?.tipo ?? '—' }}</td>
                      <td>
                        <div v-if="o.usuarios && o.usuarios.length > 0" class="tech-cell">
                          <div
                            class="avatar-sm"
                            :style="{ background: avatarColor(o.usuarios.at(0)?.id ?? 0) }"
                            :title="o.usuarios.at(0)?.nomeCompleto ?? ''"
                          >
                            {{ iniciaisNome(o.usuarios.at(0)?.nomeCompleto ?? '') }}
                          </div>
                          <span class="tech-name">{{ o.usuarios.at(0)?.nomeCompleto ?? '—' }}</span>
                        </div>
                        <span v-else class="text-muted">—</span>
                      </td>
                      <td class="cell-mono">{{ formatarData(o.dataInicio) }}</td>
                      <td class="cell-mono" :class="{ 'text-late': isLate(o) }">
                        {{ formatarData(o.dataFim) }}
                      </td>
                      <td>
                        <span class="badge" :class="statusBadgeClass(o.status)">
                          {{ labelStatus(o.status) }}
                        </span>
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>
          </div>

        </section>
      </main>
    </div>
  </div>
</template>

<style scoped>
/* ── Layout ──────────────────────────────────── */
.sv-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--gray-50);
}

.sv-workspace {
  display: flex;
  flex: 1;
  min-height: 0;
}

.sv-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

/* ── Topbar ──────────────────────────────────── */
.sv-topbar {
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

.topbar-left {
  display: flex;
  align-items: baseline;
  gap: 8px;
  flex: 1;
  min-width: 0;
}

.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
  white-space: nowrap;
}

.topbar-sub {
  font-size: 12px;
  color: var(--gray-500);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

.topbar-actions .btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

/* ── Conteúdo ────────────────────────────────── */
.sv-content {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* ── KPI Grid (6 colunas) ────────────────────── */
.kpi-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 14px;
}

.kpi-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 16px 18px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.07);
  position: relative;
  overflow: hidden;
}

.kpi-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3px;
}

.kpi-blue::before   { background: var(--blue-600); }
.kpi-amber::before  { background: var(--amber); }
.kpi-purple::before { background: #7c3aed; }
.kpi-green::before  { background: var(--green); }
.kpi-red::before    { background: var(--red); }

.kpi-label {
  font-size: 10.5px;
  color: var(--gray-500);
  margin-bottom: 6px;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.kpi-value {
  font-size: 26px;
  font-weight: 700;
  color: var(--gray-900);
  line-height: 1.2;
  margin-bottom: 4px;
}

.kpi-value-name {
  font-size: 15px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.kpi-sub {
  font-size: 11px;
  color: var(--gray-400);
  min-height: 18px;
  display: flex;
  align-items: center;
}

.kpi-delta {
  font-size: 11px;
  margin-top: 4px;
}

.kpi-delta.up   { color: var(--green); }
.kpi-delta.down { color: var(--amber); }

/* ── Seção central ───────────────────────────── */
.middle-grid {
  display: grid;
  grid-template-columns: 3fr 2fr;
  gap: 16px;
  align-items: start;
}

/* ── Gráfico de barras ───────────────────────── */
.chart-card .card-header {
  margin-bottom: 20px;
}

.chart-legend {
  display: flex;
  align-items: center;
  gap: 16px;
  flex-shrink: 0;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 6px;
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 2px;
  flex-shrink: 0;
}

.dot-prev { background: var(--blue-400); }
.dot-corr { background: #f97316; }

.legend-label {
  font-size: 12px;
  color: var(--gray-500);
}

.bars-container {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  height: 110px;
  border-bottom: 1px solid var(--gray-200);
  padding: 0 2px;
}

.bar-month-group {
  flex: 1;
  height: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: center;
}

.bars-pair {
  display: flex;
  align-items: flex-end;
  gap: 3px;
}

.bar {
  width: 14px;
  border-radius: 3px 3px 0 0;
  min-height: 3px;
  transition: height 0.4s ease;
}

.bar-prev { background: var(--blue-400); }
.bar-corr { background: #f97316; }

.month-labels-row {
  display: flex;
  gap: 8px;
  margin-top: 8px;
  padding: 0 2px;
}

.month-label {
  flex: 1;
  text-align: center;
  font-size: 11px;
  color: var(--gray-500);
}

/* ── Ativos em alerta ────────────────────────── */
.card-title-row {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.asset-name {
  display: block;
  font-weight: 500;
  color: var(--gray-900);
  font-size: 13px;
}

.asset-sub {
  display: block;
  font-size: 11px;
  color: var(--gray-400);
}

/* ── Tabela de ordens ────────────────────────── */
.header-filters {
  display: flex;
  align-items: center;
  gap: 8px;
}

.order-id {
  font-family: var(--mono);
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-700);
}

.tech-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.avatar-sm {
  width: 26px;
  height: 26px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 10px;
  font-weight: 700;
  color: white;
  flex-shrink: 0;
}

.tech-name {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 130px;
}

.cell-mono {
  font-family: var(--mono);
  font-size: 12px;
}

.text-muted { color: var(--gray-400); }
.text-late  { color: var(--red); font-weight: 500; }

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
}

/* ── Badges de tipo ──────────────────────────── */
.tipo-prev { background: var(--blue-50);  color: var(--blue-700); }
.tipo-corr { background: #fff7ed;         color: #c2410c; }
.tipo-inst { background: #ede9fe;         color: #5b21b6; }
.tipo-rem  { background: var(--gray-100); color: var(--gray-500); }

/* ── Responsivo ──────────────────────────────── */
@media (max-width: 1400px) {
  .kpi-grid { grid-template-columns: repeat(3, 1fr); }
}

@media (max-width: 1100px) {
  .middle-grid { grid-template-columns: 1fr; }
}

@media (max-width: 860px) {
  .sv-workspace { flex-direction: column; }

  .sv-topbar {
    height: auto;
    flex-direction: column;
    align-items: flex-start;
    padding: 14px 16px;
    gap: 10px;
  }

  .topbar-actions {
    width: 100%;
    flex-wrap: wrap;
  }

  .sv-content { padding: 16px; }

  .kpi-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 480px) {
  .kpi-grid { grid-template-columns: 1fr; }
}
</style>
