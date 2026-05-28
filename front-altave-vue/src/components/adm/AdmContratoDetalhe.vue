<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { buscarContratos, formatarData, resolverStatus } from '@/services/ContratoService'
import type { IContrato } from '@/services/ContratoService'
import { buscarOrdens, labelStatus, labelTipo } from '@/services/OrdemService'
import type { IOrdem } from '@/services/OrdemService'
import api from '@/config/axios'
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

// ─── Rota e navegação ──────────────────────────────────────────────────────────
const route = useRoute()
const router = useRouter()
const contratoId = Number(route.params.id)

// ─── Dados do contrato ─────────────────────────────────────────────────────────
const contrato = ref<IContrato | null>(null)
const isLoadingContrato = ref(false)
const erroContrato = ref('')

const statusResolvido = computed(() =>
  contrato.value ? resolverStatus(contrato.value.dataFim) : '—',
)

// usuarios não está no IContrato mas a API retorna; usa any para não alterar o service
const usuariosContrato = computed<any[]>(() => (contrato.value as any)?.usuarios ?? [])

// ─── Abas ──────────────────────────────────────────────────────────────────────
type TabId = 'visao' | 'supervisoes' | 'ativos' | 'ordens'
const activeTab = ref<TabId>('visao')
const tabs: { id: TabId; label: string }[] = [
  { id: 'visao', label: 'Visão Geral' },
  { id: 'supervisoes', label: 'Supervisões' },
  { id: 'ativos', label: 'Ativos' },
  { id: 'ordens', label: 'Ordens' },
]

// ─── Aba Supervisões ───────────────────────────────────────────────────────────
type Supervisao = {
  id: number
  nome: string
  planta: string
  responsavel: string
  criacao: string
}

const supervisoes = ref<Supervisao[]>([
  { id: 1, nome: 'Supervisão Norte', planta: 'Planta 1', responsavel: 'Carlos Menezes', criacao: '2024-03-10' },
  { id: 2, nome: 'Supervisão Sul', planta: 'Planta 2', responsavel: 'Ana Beatriz', criacao: '2024-05-22' },
])

const isSupervDrawerOpen = ref(false)
const novaSupervIsSubmitting = ref(false)
const novaSupervError = ref('')
const novaSupervProximoId = ref(3)

const novaSupervForm = reactive({
  nome: '',
  planta: '',
  responsavel: '',
})

function abrirSupervDrawer() {
  isSupervDrawerOpen.value = true
}

function fecharSupervDrawer() {
  isSupervDrawerOpen.value = false
  novaSupervError.value = ''
  novaSupervForm.nome = ''
  novaSupervForm.planta = ''
  novaSupervForm.responsavel = ''
}

async function salvarSupervisao() {
  if (!novaSupervForm.nome.trim() || !novaSupervForm.planta.trim() || !novaSupervForm.responsavel.trim()) {
    novaSupervError.value = 'Preencha todos os campos.'
    return
  }
  novaSupervIsSubmitting.value = true
  // Simulação de delay de rede (mock — sem endpoint)
  await new Promise((r) => setTimeout(r, 400))
  supervisoes.value.unshift({
    id: novaSupervProximoId.value++,
    nome: novaSupervForm.nome.trim(),
    planta: novaSupervForm.planta.trim(),
    responsavel: novaSupervForm.responsavel.trim(),
    criacao: new Date().toISOString().slice(0, 10),
  })
  novaSupervIsSubmitting.value = false
  fecharSupervDrawer()
}

// ─── Aba Ativos (mock) ─────────────────────────────────────────────────────────
type AtivoLocal = {
  id: number
  tipo: string
  planta: string
  predio: string
  status: string
}

const ativos = ref<AtivoLocal[]>([
  { id: 1, tipo: 'Elevador', planta: 'Planta 1', predio: 'Bloco A', status: 'Operacional' },
  { id: 2, tipo: 'Gerador', planta: 'Planta 1', predio: 'Bloco B', status: 'Manutenção' },
  { id: 3, tipo: 'HVAC', planta: 'Planta 2', predio: 'Bloco C', status: 'Operacional' },
])

// ─── Aba Ordens ────────────────────────────────────────────────────────────────
const ordens = ref<IOrdem[]>([])
const isLoadingOrdens = ref(false)

// ─── Desabilitar contrato ──────────────────────────────────────────────────────
const isDesabilitando = ref(false)

async function desabilitarContrato() {
  if (!contrato.value) return
  const confirmar = window.confirm(
    `Desabilitar o contrato de "${contrato.value.nomeEmpresa}"? Esta ação não pode ser desfeita.`,
  )
  if (!confirmar) return
  isDesabilitando.value = true
  try {
    await api.patch(`/contrato/${contratoId}/desabilitar`)
    router.push('/adm/contratos')
  } catch (e: unknown) {
    console.error('Erro ao desabilitar contrato:', e)
    erroContrato.value = 'Erro ao desabilitar. Tente novamente.'
  } finally {
    isDesabilitando.value = false
  }
}

// ─── Ciclo de vida ─────────────────────────────────────────────────────────────
onMounted(async () => {
  isLoadingContrato.value = true
  isLoadingOrdens.value = true

  try {
    const lista = await buscarContratos()
    contrato.value = lista.find((c) => c.id === contratoId) ?? null
    if (!contrato.value) erroContrato.value = 'Contrato não encontrado.'
  } catch (e: unknown) {
    console.error(e)
    erroContrato.value = 'Erro ao carregar contrato.'
  } finally {
    isLoadingContrato.value = false
  }

  try {
    ordens.value = await buscarOrdens()
  } catch (e: unknown) {
    console.error('Erro ao carregar ordens:', e)
  } finally {
    isLoadingOrdens.value = false
  }
})
</script>

<template>
  <div class="adm-layout">
    <AppHeaderRoles />

    <div class="adm-workspace">
      <SidebarAdm />

      <main class="adm-main">
        <!-- Topbar -->
        <header class="topbar">
          <div class="topbar-left">
            <button class="btn btn-ghost btn-sm" type="button" @click="router.push('/adm/contratos')">
              <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="19" y1="12" x2="5" y2="12" />
                <polyline points="12 19 5 12 12 5" />
              </svg>
              Voltar
            </button>
            <div class="topbar-title-block">
              <span class="topbar-title">
                {{ isLoadingContrato ? 'Carregando...' : (contrato?.nomeEmpresa ?? 'Contrato') }}
              </span>
              <span
                v-if="!isLoadingContrato && contrato"
                class="badge"
                :class="{
                  'badge-active': statusResolvido === 'Ativo',
                  'badge-pending': statusResolvido === 'Expirando',
                  'badge-done': statusResolvido === 'Inativo',
                }"
              >
                {{ statusResolvido }}
              </span>
            </div>
          </div>
          <div class="topbar-actions">
            <button
              v-if="contrato"
              class="btn btn-danger btn-sm"
              type="button"
              :disabled="isDesabilitando || statusResolvido === 'Inativo'"
              @click="desabilitarContrato"
            >
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="10" />
                <line x1="4.93" y1="4.93" x2="19.07" y2="19.07" />
              </svg>
              {{ isDesabilitando ? 'Desabilitando...' : 'Desabilitar' }}
            </button>
          </div>
        </header>

        <!-- Erro global -->
        <div v-if="erroContrato && !isLoadingContrato" class="content-area" style="padding: 24px;">
          <div class="alert alert-warning">{{ erroContrato }}</div>
        </div>

        <template v-else-if="!isLoadingContrato && contrato">
          <!-- Abas -->
          <div class="tabs-bar">
            <button
              v-for="tab in tabs"
              :key="tab.id"
              class="tab-btn"
              :class="{ active: activeTab === tab.id }"
              type="button"
              @click="activeTab = tab.id"
            >
              {{ tab.label }}
            </button>
          </div>

          <section class="content-area adm-content">
            <!-- ── Aba Visão Geral ─────────────────────────────────────────── -->
            <div v-show="activeTab === 'visao'">
              <div class="grid-2 info-grid">
                <!-- Período -->
                <div class="card">
                  <div class="card-title" style="margin-bottom: 14px;">Período do Contrato</div>
                  <div class="info-row">
                    <div class="info-field">
                      <div class="detail-field-label">Início</div>
                      <div class="detail-field-value">{{ formatarData(contrato.dataInicio) }}</div>
                    </div>
                    <div class="info-field">
                      <div class="detail-field-label">Término</div>
                      <div class="detail-field-value">{{ formatarData(contrato.dataFim) }}</div>
                    </div>
                  </div>
                </div>

                <!-- Escopo -->
                <div class="card">
                  <div class="card-title" style="margin-bottom: 14px;">Escopo</div>
                  <div class="info-row">
                    <div class="info-field">
                      <div class="detail-field-label">Plantas</div>
                      <div class="detail-field-value">{{ contrato.quantidadePlanta }}</div>
                    </div>
                    <div class="info-field">
                      <div class="detail-field-label">Ativos</div>
                      <div class="detail-field-value">{{ contrato.quantidadeAtivos }}</div>
                    </div>
                  </div>
                </div>
              </div>

              <!-- Descrição -->
              <div class="card" style="margin-top: 14px;">
                <div class="card-title" style="margin-bottom: 10px;">Descrição</div>
                <p class="desc-text">{{ contrato.descricao || '—' }}</p>
              </div>

              <!-- Usuários vinculados -->
              <div class="card" style="margin-top: 14px;">
                <div class="card-header" style="margin-bottom: 14px;">
                  <div class="card-title">Usuários Vinculados</div>
                </div>
                <div v-if="usuariosContrato.length === 0" class="empty-state">
                  Nenhum usuário vinculado a este contrato.
                </div>
                <ul v-else class="user-list">
                  <li v-for="u in usuariosContrato" :key="u.id" class="user-list-item">
                    <div class="avatar avatar-blue" style="font-size: 11px;">
                      {{ String(u.nomeCompleto ?? u.nome ?? '?').charAt(0).toUpperCase() }}
                    </div>
                    <div>
                      <div class="user-name-text">{{ u.nomeCompleto ?? u.nome }}</div>
                      <div class="user-role-text">{{ u.cargo ?? u.funcao ?? '' }}</div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>

            <!-- ── Aba Supervisões ──────────────────────────────────────────── -->
            <div v-show="activeTab === 'supervisoes'">
              <div class="card">
                <div class="card-header">
                  <div>
                    <div class="card-title">Supervisões</div>
                    <div class="card-sub">{{ supervisoes.length }} supervisão(ões)</div>
                  </div>
                  <button class="btn btn-primary btn-sm" type="button" @click="abrirSupervDrawer">
                    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <line x1="12" y1="5" x2="12" y2="19" />
                      <line x1="5" y1="12" x2="19" y2="12" />
                    </svg>
                    Nova Supervisão
                  </button>
                </div>

                <div class="table-wrap">
                  <table>
                    <thead>
                      <tr>
                        <th>Nome</th>
                        <th>Planta</th>
                        <th>Responsável</th>
                        <th>Criado em</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-if="supervisoes.length === 0">
                        <td colspan="4" class="table-empty">Nenhuma supervisão cadastrada.</td>
                      </tr>
                      <tr v-for="s in supervisoes" :key="s.id">
                        <td><strong>{{ s.nome }}</strong></td>
                        <td>{{ s.planta }}</td>
                        <td>{{ s.responsavel }}</td>
                        <td>{{ formatarData(s.criacao) }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <!-- ── Aba Ativos ───────────────────────────────────────────────── -->
            <div v-show="activeTab === 'ativos'">
              <div class="card">
                <div class="card-header">
                  <div>
                    <div class="card-title">Ativos do Contrato</div>
                    <div class="card-sub">{{ ativos.length }} ativo(s)</div>
                  </div>
                  <button class="btn btn-primary btn-sm" type="button" disabled title="Em breve">
                    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <line x1="12" y1="5" x2="12" y2="19" />
                      <line x1="5" y1="12" x2="19" y2="12" />
                    </svg>
                    Novo Ativo
                  </button>
                </div>

                <div class="table-wrap">
                  <table>
                    <thead>
                      <tr>
                        <th>Tipo</th>
                        <th>Planta</th>
                        <th>Prédio</th>
                        <th>Status</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-if="ativos.length === 0">
                        <td colspan="4" class="table-empty">Nenhum ativo cadastrado.</td>
                      </tr>
                      <tr v-for="a in ativos" :key="a.id">
                        <td><strong>{{ a.tipo }}</strong></td>
                        <td>{{ a.planta }}</td>
                        <td>{{ a.predio }}</td>
                        <td>
                          <span
                            class="badge"
                            :class="a.status === 'Operacional' ? 'badge-active' : 'badge-pending'"
                          >
                            {{ a.status }}
                          </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>

            <!-- ── Aba Ordens ───────────────────────────────────────────────── -->
            <div v-show="activeTab === 'ordens'">
              <div class="card">
                <div class="card-header">
                  <div>
                    <div class="card-title">Ordens de Manutenção</div>
                    <div class="card-sub">
                      {{ isLoadingOrdens ? 'Carregando...' : `${ordens.length} ordem(ns)` }}
                    </div>
                  </div>
                </div>

                <div class="table-wrap">
                  <table>
                    <thead>
                      <tr>
                        <th>Nome</th>
                        <th>Tipo</th>
                        <th>Status</th>
                        <th>Início</th>
                        <th>Término</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-if="isLoadingOrdens">
                        <td colspan="5" class="table-empty">Carregando ordens...</td>
                      </tr>
                      <tr v-else-if="ordens.length === 0">
                        <td colspan="5" class="table-empty">Nenhuma ordem encontrada.</td>
                      </tr>
                      <tr v-for="o in ordens" :key="o.id">
                        <td><strong>{{ o.nome }}</strong></td>
                        <td>{{ labelTipo(o.tipoManutencao) }}</td>
                        <td>
                          <span
                            class="badge"
                            :class="{
                              'badge-pending': o.status === 'PENDENTE',
                              'badge-progress': o.status === 'EM_ANDAMENTO',
                              'badge-active': o.status === 'CONCLUIDA',
                              'badge-done': o.status === 'CANCELADA',
                            }"
                          >
                            {{ labelStatus(o.status) }}
                          </span>
                        </td>
                        <td>{{ formatarData(o.dataInicio) }}</td>
                        <td>{{ formatarData(o.dataFim) }}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </section>
        </template>

        <!-- Loading do contrato -->
        <div v-else-if="isLoadingContrato" class="content-area" style="padding: 32px; text-align: center; color: var(--gray-500);">
          Carregando contrato...
        </div>
      </main>
    </div>

    <!-- ── Backdrop ──────────────────────────────────────────────────────────── -->
    <div class="drawer-backdrop" :class="{ open: isSupervDrawerOpen }" @click="fecharSupervDrawer" />

    <!-- ── Drawer Nova Supervisão ────────────────────────────────────────────── -->
    <aside class="detail-overlay" :class="{ open: isSupervDrawerOpen }" aria-label="Nova supervisão">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="fecharSupervDrawer">✕</button>
        <strong>Nova Supervisão</strong>
        <p>Cadastre uma supervisão para este contrato.</p>
      </div>

      <form class="detail-body contract-form" @submit.prevent="salvarSupervisao">
        <p v-if="novaSupervError" class="form-error">{{ novaSupervError }}</p>

        <label>
          Nome *
          <input v-model="novaSupervForm.nome" type="text" placeholder="Ex: Supervisão Norte" required />
        </label>

        <label>
          Planta *
          <input v-model="novaSupervForm.planta" type="text" placeholder="Ex: Planta 1" required />
        </label>

        <label>
          Responsável *
          <input v-model="novaSupervForm.responsavel" type="text" placeholder="Nome do responsável" required />
        </label>

        <div class="drawer-actions">
          <button class="btn btn-secondary" type="button" @click="fecharSupervDrawer">Cancelar</button>
          <button class="btn btn-primary" type="submit" :disabled="novaSupervIsSubmitting">
            {{ novaSupervIsSubmitting ? 'Salvando...' : 'Criar supervisão' }}
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

/* Topbar */
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

.topbar-left {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.topbar-title-block {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
}

.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 320px;
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-shrink: 0;
}

/* Botão perigo */
.btn-danger {
  background: var(--red-bg);
  color: var(--red);
  border: 1px solid #fca5a5;
}

.btn-danger:hover:not(:disabled) {
  background: var(--red);
  color: white;
  border-color: var(--red);
}

.btn-danger:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* Abas */
.tabs-bar {
  display: flex;
  gap: 0;
  padding: 0 24px;
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  flex-shrink: 0;
}

.tab-btn {
  padding: 12px 18px;
  border: none;
  background: transparent;
  font-family: var(--font);
  font-size: 13.5px;
  font-weight: 500;
  color: var(--gray-500);
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.15s;
  margin-bottom: -1px;
}

.tab-btn:hover {
  color: var(--gray-700);
}

.tab-btn.active {
  color: var(--blue-700);
  border-bottom-color: var(--blue-600);
  font-weight: 600;
}

/* Conteúdo */
.adm-content {
  padding: 24px;
}

.info-grid {
  gap: 14px;
}

.info-row {
  display: flex;
  gap: 24px;
}

.info-field {
  flex: 1;
}

.desc-text {
  font-size: 13.5px;
  color: var(--gray-700);
  line-height: 1.6;
  white-space: pre-wrap;
}

/* Lista de usuários */
.user-list {
  list-style: none;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.user-list-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 0;
  border-bottom: 1px solid var(--gray-100);
}

.user-list-item:last-child {
  border-bottom: none;
}

.user-name-text {
  font-size: 13px;
  font-weight: 500;
  color: var(--gray-900);
}

.user-role-text {
  font-size: 11.5px;
  color: var(--gray-500);
}

/* Estado vazio */
.empty-state {
  padding: 20px;
  text-align: center;
  color: var(--gray-400);
  font-size: 13px;
}

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
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

@media (max-width: 860px) {
  .adm-workspace {
    flex-direction: column;
  }

  .topbar {
    position: static;
    flex-direction: column;
    align-items: flex-start;
    height: auto;
    padding: 12px 16px;
    gap: 10px;
  }

  .topbar-left {
    flex-wrap: wrap;
  }

  .topbar-title {
    max-width: 240px;
  }

  .tabs-bar {
    overflow-x: auto;
    padding: 0 16px;
  }

  .info-grid {
    grid-template-columns: 1fr !important;
  }

  .info-row {
    flex-direction: column;
    gap: 12px;
  }
}
</style>
