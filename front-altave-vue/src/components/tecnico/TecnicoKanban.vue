<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarTecnico from '@/components/geral/layout/SidebarTecnico.vue'
import {
  atualizarStatus,
  avatarColor,
  buscarOrdens,
  formatarData,
  iniciaisNome,
  labelStatus,
  labelTipo,
  type IOrdem,
} from '@/services/OrdemService'
import { getCurrentUser } from '@/services/AuthService'
import { computed, onMounted, ref } from 'vue'

const tecnicoLogado = getCurrentUser()

/* ─── estado ─── */
const todasOrdens = ref<IOrdem[]>([])
const isLoading = ref(false)
const erro = ref('')

/* ─── drawer de detalhes ─── */
const ordemSelecionada = ref<IOrdem | null>(null)
const drawerAberto = ref(false)
const salvandoStatus = ref(false)

/* ─── ordens filtradas: só as do técnico logado ─── */
const minhasOrdens = computed(() => {
  const id = tecnicoLogado?.id
  if (!id) return []
  return todasOrdens.value.filter((o) =>
    (o.usuarios ?? []).some((u) => u.id === id),
  )
})

const pendentes = computed(() =>
  minhasOrdens.value.filter((o) => o.status === 'PENDENTE'),
)
const emAndamento = computed(() =>
  minhasOrdens.value.filter((o) => o.status === 'EM_ANDAMENTO'),
)
const concluidas = computed(() =>
  minhasOrdens.value.filter((o) => o.status === 'CONCLUIDA' || o.status === 'CANCELADA'),
)
const atrasadas = computed(() =>
  minhasOrdens.value.filter((o) => isAtrasada(o)),
)

/* ─── helpers ─── */
function isAtrasada(o: IOrdem) {
  if (!o.dataFim || o.status === 'CONCLUIDA' || o.status === 'CANCELADA') return false
  return new Date(`${o.dataFim}T00:00:00`) < new Date()
}

function labelAtivo(o: IOrdem) {
  if (!o.ativo) return '—'
  return [o.ativo.tipo, o.ativo.predio, o.ativo.planta]
    .filter(Boolean)
    .join(' · ') || `Ativo #${o.ativo.id}`
}

function classTipoBadge(tipo: string) {
  return {
    'badge-preventiva': tipo === 'PREVENTIVA',
    'badge-corretiva': tipo === 'CORRETIVA',
    'badge-instalacao': tipo === 'INSTALACAO',
    'badge-remocao': tipo === 'REMOCAO',
  }
}

/* ─── ciclo de vida ─── */
onMounted(async () => {
  isLoading.value = true
  try {
    todasOrdens.value = await buscarOrdens()
  } catch (e: any) {
    erro.value = 'Não foi possível carregar as ordens.'
    console.error(e)
  } finally {
    isLoading.value = false
  }
})

/* ─── detalhe ─── */
function abrirDetalhe(o: IOrdem) {
  ordemSelecionada.value = { ...o }
  drawerAberto.value = true
}
function fecharDetalhe() {
  drawerAberto.value = false
}

/* ─── mudar status ─── */
const TRANSICOES: Record<IOrdem['status'], { label: string; proximo: IOrdem['status'] }[]> = {
  PENDENTE: [{ label: 'Iniciar ordem', proximo: 'EM_ANDAMENTO' }],
  EM_ANDAMENTO: [
    { label: 'Concluir', proximo: 'CONCLUIDA' },
    { label: 'Pausar', proximo: 'PENDENTE' },
  ],
  CONCLUIDA: [],
  CANCELADA: [],
}

async function mudarStatus(novoStatus: IOrdem['status']) {
  if (!ordemSelecionada.value) return
  salvandoStatus.value = true
  try {
    const atualizada = await atualizarStatus(ordemSelecionada.value.id, novoStatus)
    /* atualiza na lista local */
    const idx = todasOrdens.value.findIndex((o) => o.id === atualizada.id)
    if (idx !== -1) todasOrdens.value[idx] = atualizada
    ordemSelecionada.value = atualizada
  } catch (e) {
    console.error(e)
  } finally {
    salvandoStatus.value = false
  }
}
</script>

<template>
  <div class="tk-layout">
    <AppHeaderRoles />

    <div class="tk-workspace">
      <SidebarTecnico />

      <main class="tk-main">
      <!-- topbar -->
      <header class="topbar">
        <div class="topbar-left">
          <h1 class="topbar-title">Minhas Ordens</h1>
          <span class="topbar-count">{{ minhasOrdens.length }} ordens atribuídas</span>
          <span v-if="atrasadas.length > 0" class="badge-atrasadas">
            {{ atrasadas.length }} atrasada{{ atrasadas.length > 1 ? 's' : '' }}
          </span>
        </div>
      </header>

      <!-- conteúdo -->
      <div class="tk-content">
        <!-- loading -->
        <div v-if="isLoading" class="estado-vazio">Carregando ordens...</div>
        <div v-else-if="erro" class="estado-vazio estado-erro">{{ erro }}</div>

        <template v-else>
          <!-- mensagem vazia -->
          <div v-if="minhasOrdens.length === 0" class="estado-vazio">
            Nenhuma ordem atribuída a você no momento.
          </div>

          <!-- kanban -->
          <div v-else class="kanban-board">

            <!-- coluna: Pendente -->
            <div class="kanban-col">
              <div class="col-header col-header--pendente">
                <div class="col-header-left">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <circle cx="12" cy="12" r="10" />
                  </svg>
                  Pendente
                </div>
                <span class="col-count">{{ pendentes.length }}</span>
              </div>

              <div class="cards-list">
                <div
                  v-for="o in pendentes"
                  :key="o.id"
                  class="order-card"
                  :class="{ 'card--atrasada': isAtrasada(o) }"
                  @click="abrirDetalhe(o)"
                >
                  <div class="card-top">
                    <span class="card-nome">{{ o.nome }}</span>
                    <span v-if="isAtrasada(o)" class="badge-pill badge-atrasada-pill">Atrasada</span>
                  </div>

                  <span class="card-ativo">{{ labelAtivo(o) }}</span>

                  <div class="card-tags">
                    <span class="badge" :class="classTipoBadge(o.tipoManutencao)">
                      {{ labelTipo(o.tipoManutencao) }}
                    </span>
                    <span v-if="o.planta || o.predio" class="card-local">
                      {{ [o.planta, o.predio].filter(Boolean).join(' · ') }}
                    </span>
                  </div>

                  <div class="card-footer">
                    <div class="avatares">
                      <div
                        v-for="u in (o.usuarios ?? []).slice(0, 3)"
                        :key="u.id"
                        class="avatar-xs"
                        :style="{ background: avatarColor(u.id) }"
                        :title="u.nomeCompleto"
                      >
                        {{ iniciaisNome(u.nomeCompleto) }}
                      </div>
                    </div>
                    <span v-if="o.dataFim" class="card-prazo" :class="{ 'prazo-atrasado': isAtrasada(o) }">
                      {{ formatarData(o.dataFim) }}
                      <svg v-if="isAtrasada(o)" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                        <path d="M10.29 3.86L1.82 18a2 2 0 0 0 1.71 3h16.94a2 2 0 0 0 1.71-3L13.71 3.86a2 2 0 0 0-3.42 0z"/>
                        <line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>
                      </svg>
                    </span>
                  </div>
                </div>

                <div v-if="pendentes.length === 0" class="col-empty">Sem ordens pendentes</div>
              </div>
            </div>

            <!-- coluna: Em Progresso -->
            <div class="kanban-col">
              <div class="col-header col-header--andamento">
                <div class="col-header-left">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                    <path d="M12 2a10 10 0 0 1 7.38 16.74"/>
                    <path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83"/>
                  </svg>
                  Em Progresso
                </div>
                <span class="col-count">{{ emAndamento.length }}</span>
              </div>

              <div class="cards-list">
                <div
                  v-for="o in emAndamento"
                  :key="o.id"
                  class="order-card"
                  @click="abrirDetalhe(o)"
                >
                  <div class="card-top">
                    <span class="card-nome">{{ o.nome }}</span>
                    <span class="badge" :class="classTipoBadge(o.tipoManutencao)">
                      {{ labelTipo(o.tipoManutencao) }}
                    </span>
                  </div>

                  <span class="card-ativo">{{ labelAtivo(o) }}</span>

                  <div class="card-footer">
                    <div class="avatares">
                      <div
                        v-for="u in (o.usuarios ?? []).slice(0, 3)"
                        :key="u.id"
                        class="avatar-xs"
                        :style="{ background: avatarColor(u.id) }"
                        :title="u.nomeCompleto"
                      >
                        {{ iniciaisNome(u.nomeCompleto) }}
                      </div>
                    </div>
                    <span v-if="o.dataFim" class="card-prazo">
                      {{ formatarData(o.dataFim) }}
                    </span>
                  </div>
                </div>

                <div v-if="emAndamento.length === 0" class="col-empty">Sem ordens em andamento</div>
              </div>
            </div>

            <!-- coluna: Concluída -->
            <div class="kanban-col">
              <div class="col-header col-header--concluida">
                <div class="col-header-left">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                    <polyline points="20 6 9 17 4 12" />
                  </svg>
                  Concluída
                </div>
                <span class="col-count col-count--green">{{ concluidas.length }}</span>
              </div>

              <div class="cards-list">
                <div
                  v-for="o in concluidas"
                  :key="o.id"
                  class="order-card card--concluida"
                  @click="abrirDetalhe(o)"
                >
                  <div class="card-top">
                    <span class="card-nome card-nome--riscado">{{ o.nome }}</span>
                    <span class="badge badge-concluida-pill">Concluída</span>
                  </div>

                  <span class="card-ativo">{{ labelAtivo(o) }}</span>

                  <div class="card-tags">
                    <span class="badge" :class="classTipoBadge(o.tipoManutencao)">
                      {{ labelTipo(o.tipoManutencao) }}
                    </span>
                  </div>

                  <div class="card-footer">
                    <span class="card-concluida-em">
                      <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                        <polyline points="20 6 9 17 4 12" />
                      </svg>
                      Concluída em {{ formatarData(o.dataFim) }}
                    </span>
                  </div>
                </div>

                <div v-if="concluidas.length === 0" class="col-empty">Nenhuma concluída</div>
              </div>
            </div>

          </div>
        </template>
      </div>
      </main>
    </div>

    <!-- backdrop -->
    <div class="drawer-backdrop" :class="{ open: drawerAberto }" @click="fecharDetalhe" />

    <!-- drawer de detalhes + ações -->
    <aside class="detail-overlay" :class="{ open: drawerAberto }">
      <template v-if="ordemSelecionada">
        <div class="detail-header">
          <button class="detail-close" type="button" @click="fecharDetalhe">✕</button>
          <div class="detail-badges">
            <span class="badge" :class="classTipoBadge(ordemSelecionada.tipoManutencao)">
              {{ labelTipo(ordemSelecionada.tipoManutencao) }}
            </span>
            <span
              class="badge"
              :class="{
                'badge-status-pendente': ordemSelecionada.status === 'PENDENTE',
                'badge-status-andamento': ordemSelecionada.status === 'EM_ANDAMENTO',
                'badge-status-concluida': ordemSelecionada.status === 'CONCLUIDA',
                'badge-status-cancelada': ordemSelecionada.status === 'CANCELADA',
              }"
            >
              {{ labelStatus(ordemSelecionada.status) }}
            </span>
          </div>
          <strong class="detail-title">
            #{{ String(ordemSelecionada.id).padStart(4, '0') }} — {{ ordemSelecionada.nome }}
          </strong>
        </div>

        <div class="detail-body">
          <!-- ações de status -->
          <div
            v-if="TRANSICOES[ordemSelecionada.status].length > 0"
            class="status-actions"
          >
            <button
              v-for="t in TRANSICOES[ordemSelecionada.status]"
              :key="t.proximo"
              class="btn-status"
              :class="{
                'btn-status--iniciar': t.proximo === 'EM_ANDAMENTO',
                'btn-status--concluir': t.proximo === 'CONCLUIDA',
                'btn-status--pausar': t.proximo === 'PENDENTE',
              }"
              :disabled="salvandoStatus"
              @click="mudarStatus(t.proximo)"
            >
              <svg v-if="t.proximo === 'EM_ANDAMENTO'" width="14" height="14" viewBox="0 0 24 24" fill="currentColor">
                <polygon points="5 3 19 12 5 21 5 3"/>
              </svg>
              <svg v-else-if="t.proximo === 'CONCLUIDA'" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <polyline points="20 6 9 17 4 12"/>
              </svg>
              <svg v-else-if="t.proximo === 'PENDENTE'" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <rect x="6" y="4" width="4" height="16"/><rect x="14" y="4" width="4" height="16"/>
              </svg>
              {{ salvandoStatus ? 'Salvando...' : t.label }}
            </button>
          </div>
          <div v-else class="status-final">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            Ordem finalizada
          </div>

          <div class="divider" />

          <!-- campos -->
          <div class="info-grid">
            <div class="info-row">
              <span class="info-label">Descrição</span>
              <span class="info-value">{{ ordemSelecionada.descricao || '—' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">Planta</span>
              <span class="info-value">{{ ordemSelecionada.planta || '—' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">Prédio</span>
              <span class="info-value">{{ ordemSelecionada.predio || '—' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">Ativo</span>
              <span class="info-value">{{ labelAtivo(ordemSelecionada) }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">Início previsto</span>
              <span class="info-value">{{ formatarData(ordemSelecionada.dataInicio) }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">Prazo</span>
              <span class="info-value" :class="{ 'info-value--red': isAtrasada(ordemSelecionada) }">
                {{ formatarData(ordemSelecionada.dataFim) }}
                <span v-if="isAtrasada(ordemSelecionada)" class="badge-pill badge-atrasada-pill">Atrasada</span>
              </span>
            </div>
            <div v-if="ordemSelecionada.requisito" class="info-row">
              <span class="info-label">Requisito</span>
              <span class="info-value">
                <span class="tag-requisito">{{ ordemSelecionada.requisito }}</span>
              </span>
            </div>
          </div>

          <!-- técnicos -->
          <div v-if="(ordemSelecionada.usuarios ?? []).length > 0" class="tecnicos-section">
            <span class="info-label">Técnicos</span>
            <div class="tecnicos-list">
              <div v-for="u in ordemSelecionada.usuarios" :key="u.id" class="tecnico-row">
                <div class="avatar-sm" :style="{ background: avatarColor(u.id) }">
                  {{ iniciaisNome(u.nomeCompleto) }}
                </div>
                <div>
                  <div class="tecnico-nome">{{ u.nomeCompleto }}</div>
                  <div class="tecnico-cargo">{{ u.funcao || u.cargo || 'Técnico' }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </aside>
  </div>
</template>

<style scoped>
/* ── layout ── */
.tk-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--gray-50);
}
.tk-workspace {
  display: flex;
  flex: 1;
  min-height: calc(100vh - var(--app-header-roles-h, 48px));
}
.tk-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  min-height: 0;
}

/* ── topbar ── */
.topbar {
  height: var(--topbar-h, 56px);
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  display: flex;
  align-items: center;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 10;
  flex-shrink: 0;
}
.topbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
}
.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
  margin: 0;
}
.topbar-count {
  font-size: 13px;
  color: var(--gray-500);
}
.badge-atrasadas {
  padding: 3px 10px;
  background: #fee2e2;
  color: #b91c1c;
  border-radius: 99px;
  font-size: 12px;
  font-weight: 600;
}

/* ── conteúdo ── */
.tk-content {
  padding: 24px;
  flex: 1;
  overflow-x: auto;
}
.estado-vazio {
  text-align: center;
  color: var(--gray-400);
  font-size: 14px;
  padding: 80px 20px;
}
.estado-erro { color: #ef4444; }

/* ── kanban ── */
.kanban-board {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
  min-width: 680px;
  align-items: start;
}
.kanban-col {
  background: var(--gray-100);
  border-radius: 12px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}
.col-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  font-size: 14px;
  font-weight: 600;
}
.col-header-left {
  display: flex;
  align-items: center;
  gap: 8px;
}
.col-header--pendente  { color: var(--gray-700); }
.col-header--andamento { color: #1e40af; }
.col-header--concluida { color: #166534; }
.col-count {
  background: var(--gray-300);
  color: var(--gray-700);
  font-size: 12px;
  font-weight: 700;
  padding: 2px 8px;
  border-radius: 99px;
}
.col-count--green {
  background: #d1fae5;
  color: #166534;
}
.col-empty {
  font-size: 12px;
  color: var(--gray-400);
  text-align: center;
  padding: 24px 16px;
}
.cards-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 0 10px 12px;
}

/* ── cards ── */
.order-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: 10px;
  padding: 14px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  gap: 8px;
  transition: box-shadow 0.15s, transform 0.1s;
}
.order-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,.08);
  transform: translateY(-1px);
}
.card--atrasada {
  border-left: 3px solid #ef4444;
  background: #fff9f9;
}
.card--concluida {
  opacity: 0.7;
}
.card-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
}
.card-nome {
  font-size: 13.5px;
  font-weight: 600;
  color: var(--gray-900);
  line-height: 1.3;
}
.card-nome--riscado {
  text-decoration: line-through;
  color: var(--gray-400);
}
.card-ativo {
  font-size: 11px;
  color: var(--gray-400);
  font-family: var(--font-mono, monospace);
  text-transform: uppercase;
  letter-spacing: .03em;
}
.card-tags {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}
.card-local {
  font-size: 11px;
  color: var(--gray-500);
}
.card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 2px;
}
.avatares {
  display: flex;
  gap: -4px;
}
.avatar-xs {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  border: 2px solid var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 9px;
  font-weight: 700;
  color: white;
  margin-right: -4px;
  flex-shrink: 0;
}
.card-prazo {
  font-size: 12px;
  color: var(--gray-500);
  display: flex;
  align-items: center;
  gap: 4px;
}
.prazo-atrasado {
  color: #ef4444;
  font-weight: 600;
}
.card-concluida-em {
  font-size: 11px;
  color: #16a34a;
  display: flex;
  align-items: center;
  gap: 4px;
}

/* ── badges ── */
.badge {
  display: inline-block;
  padding: 2px 9px;
  border-radius: 99px;
  font-size: 11px;
  font-weight: 600;
  white-space: nowrap;
}
.badge-preventiva  { background: #dbeafe; color: #1d4ed8; }
.badge-corretiva   { background: #fee2e2; color: #b91c1c; }
.badge-instalacao  { background: #f3e8ff; color: #7e22ce; }
.badge-remocao     { background: #ffedd5; color: #c2410c; }
.badge-pill { display: inline-block; padding: 2px 8px; border-radius: 99px; font-size: 11px; font-weight: 600; }
.badge-atrasada-pill { background: #fee2e2; color: #b91c1c; }
.badge-concluida-pill { background: #d1fae5; color: #166534; }

/* ── drawer ── */
.drawer-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(11,31,51,.3);
  opacity: 0;
  visibility: hidden;
  transition: all .2s;
  z-index: 90;
}
.drawer-backdrop.open { opacity: 1; visibility: visible; }
.detail-overlay {
  position: fixed;
  top: 0;
  right: 0;
  height: 100vh;
  width: 400px;
  background: var(--white);
  box-shadow: -4px 0 24px rgba(0,0,0,.12);
  z-index: 100;
  transform: translateX(100%);
  transition: transform .25s cubic-bezier(.4,0,.2,1);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}
.detail-overlay.open { transform: translateX(0); }
.detail-header {
  padding: 20px 20px 16px;
  border-bottom: 1px solid var(--gray-200);
  position: relative;
}
.detail-close {
  position: absolute;
  top: 16px;
  right: 16px;
  border: none;
  background: none;
  font-size: 16px;
  cursor: pointer;
  color: var(--gray-400);
}
.detail-close:hover { color: var(--gray-700); }
.detail-badges { display: flex; gap: 6px; margin-bottom: 8px; }
.detail-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-900);
  display: block;
  line-height: 1.4;
}
.detail-body {
  padding: 20px;
  overflow-y: auto;
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

/* ── status badges no drawer ── */
.badge-status-pendente   { background: #fef9c3; color: #92400e; }
.badge-status-andamento  { background: #dbeafe; color: #1e40af; }
.badge-status-concluida  { background: #d1fae5; color: #065f46; }
.badge-status-cancelada  { background: var(--gray-100); color: var(--gray-500); }

/* ── ações de status ── */
.status-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.btn-status {
  display: flex;
  align-items: center;
  gap: 7px;
  padding: 9px 16px;
  border-radius: 8px;
  border: none;
  font: inherit;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: filter .15s;
  flex: 1;
  justify-content: center;
}
.btn-status:disabled { opacity: .6; cursor: not-allowed; }
.btn-status:hover:not(:disabled) { filter: brightness(0.93); }
.btn-status--iniciar  { background: #2563eb; color: white; }
.btn-status--concluir { background: #16a34a; color: white; }
.btn-status--pausar   { background: var(--gray-100); color: var(--gray-700); border: 1px solid var(--gray-200); }
.status-final {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  color: #16a34a;
  font-weight: 500;
  padding: 8px 0;
}
.divider {
  height: 1px;
  background: var(--gray-100);
  margin: 0 -20px;
}

/* ── info ── */
.info-grid { display: flex; flex-direction: column; gap: 12px; }
.info-row  { display: flex; flex-direction: column; gap: 3px; }
.info-label {
  font-size: 11px;
  font-weight: 600;
  color: var(--gray-400);
  text-transform: uppercase;
  letter-spacing: .04em;
}
.info-value {
  font-size: 13px;
  color: var(--gray-800);
  display: flex;
  align-items: center;
  gap: 6px;
}
.info-value--red { color: #ef4444; font-weight: 600; }
.tag-requisito {
  display: inline-block;
  padding: 2px 8px;
  background: #f0fdf4;
  color: #15803d;
  border: 1px solid #bbf7d0;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
}

/* ── técnicos ── */
.tecnicos-section { display: flex; flex-direction: column; gap: 8px; }
.tecnicos-list    { display: flex; flex-direction: column; gap: 10px; }
.tecnico-row      { display: flex; align-items: center; gap: 10px; }
.avatar-sm {
  width: 32px; height: 32px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 11px; font-weight: 700; color: white; flex-shrink: 0;
}
.tecnico-nome  { font-size: 13px; font-weight: 500; color: var(--gray-900); }
.tecnico-cargo { font-size: 11px; color: var(--gray-400); }

/* ── responsivo ── */
@media (max-width: 900px) {
  .kanban-board { grid-template-columns: 1fr; min-width: unset; }
  .detail-overlay { width: 100vw; }
}
</style>
