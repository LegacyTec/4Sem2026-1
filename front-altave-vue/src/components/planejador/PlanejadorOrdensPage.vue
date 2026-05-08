<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarPlanejador from '@/components/geral/layout/SidebarPlanejador.vue'
import {
  avatarColor,
  buscarOrdens,
  buscarUsuarios,
  criarOrdem,
  formatarData,
  iniciaisNome,
  labelStatus,
  labelTipo,
  type IOrdem,
  type IUsuario,
} from '@/services/OrdemService'
import { computed, onMounted, reactive, ref } from 'vue'

/* ─── estado principal ─── */
const ordens = ref<IOrdem[]>([])
const usuarios = ref<IUsuario[]>([])
const isLoading = ref(false)
const errorMessage = ref('')

/* ─── drawer de DETALHES ─── */
const detalheAberto = ref(false)
const ordemSelecionada = ref<IOrdem | null>(null)

function abrirDetalhe(o: IOrdem) {
  ordemSelecionada.value = o
  detalheAberto.value = true
}
function fecharDetalhe() {
  detalheAberto.value = false
}

/* ─── drawer de CRIAR ─── */
const criarAberto = ref(false)
const isSubmitting = ref(false)
const formError = ref('')
const tecnicosSelecionados = ref<number[]>([])

const form = reactive({
  nome: '',
  descricao: '',
  planta: '',
  predio: '',
  dataInicio: '',
  dataFim: '',
  ativoId: '' as string | number,
  tipoManutencao: 'PREVENTIVA' as IOrdem['tipoManutencao'],
  requisito: '',
})

function abrirCriar() {
  criarAberto.value = true
  formError.value = ''
}
function fecharCriar() {
  criarAberto.value = false
  formError.value = ''
  tecnicosSelecionados.value = []
  Object.assign(form, {
    nome: '', descricao: '', planta: '', predio: '',
    dataInicio: '', dataFim: '', ativoId: '',
    tipoManutencao: 'PREVENTIVA', requisito: '',
  })
}

function toggleTecnico(id: number) {
  const idx = tecnicosSelecionados.value.indexOf(id)
  if (idx === -1) tecnicosSelecionados.value.push(id)
  else tecnicosSelecionados.value.splice(idx, 1)
}

async function salvar() {
  formError.value = ''
  if (!form.nome.trim() || !form.descricao.trim() || !form.planta.trim() || !form.predio.trim() || !form.ativoId) {
    formError.value = 'Preencha os campos obrigatórios: Nome, Descrição, Planta, Prédio e Ativo.'
    return
  }
  isSubmitting.value = true
  try {
    const criada = await criarOrdem({
      nome: form.nome.trim(),
      descricao: form.descricao.trim(),
      planta: form.planta.trim(),
      predio: form.predio.trim(),
      dataInicio: form.dataInicio || new Date().toISOString().split('T')[0],
      dataFim: form.dataFim || undefined,
      tipoManutencao: form.tipoManutencao,
      status: 'PENDENTE',
      requisito: form.requisito.trim() || undefined,
      ativo: { id: Number(form.ativoId) },
      usuarios: tecnicosSelecionados.value.map((id) => ({ id })),
    })
    ordens.value.unshift(criada)
    fecharCriar()
  } catch (e: any) {
    formError.value = e?.response?.data?.message ?? 'Erro ao salvar. Verifique os dados.'
    console.error(e)
  } finally {
    isSubmitting.value = false
  }
}

/* ─── filtros ─── */
const busca = ref('')
const filtroTipo = ref('')
const filtroStatus = ref('')

const ordensFiltradas = computed(() =>
  ordens.value.filter((o) => {
    const matchBusca = !busca.value ||
      o.nome.toLowerCase().includes(busca.value.toLowerCase()) ||
      (o.ativo?.tipo ?? '').toLowerCase().includes(busca.value.toLowerCase())
    return matchBusca &&
      (!filtroTipo.value || o.tipoManutencao === filtroTipo.value) &&
      (!filtroStatus.value || o.status === filtroStatus.value)
  }),
)

const totalAtrasadas = computed(() =>
  ordens.value.filter((o) => {
    if (!o.dataFim || o.status === 'CONCLUIDA' || o.status === 'CANCELADA') return false
    return new Date(`${o.dataFim}T00:00:00`) < new Date()
  }).length,
)
const totalEmAndamento = computed(() => ordens.value.filter((o) => o.status === 'EM_ANDAMENTO').length)

/* ─── helpers visuais ─── */
function classTipo(tipo: string) {
  return {
    'badge-preventiva': tipo === 'PREVENTIVA',
    'badge-corretiva': tipo === 'CORRETIVA',
    'badge-instalacao': tipo === 'INSTALACAO',
    'badge-remocao': tipo === 'REMOCAO',
  }
}
function classStatus(status: string) {
  return {
    'badge-pendente': status === 'PENDENTE',
    'badge-em-andamento': status === 'EM_ANDAMENTO',
    'badge-concluida': status === 'CONCLUIDA',
    'badge-cancelada': status === 'CANCELADA',
  }
}
function isAtrasada(o: IOrdem) {
  if (!o.dataFim || o.status === 'CONCLUIDA' || o.status === 'CANCELADA') return false
  return new Date(`${o.dataFim}T00:00:00`) < new Date()
}
function labelAtivo(o: IOrdem) {
  if (!o.ativo) return '—'
  return [o.ativo.tipo, o.ativo.predio, o.ativo.planta].filter(Boolean).join(' · ') || `Ativo #${o.ativo.id}`
}

/* ─── ciclo de vida ─── */
onMounted(async () => {
  isLoading.value = true
  try {
    const [ordsData, usrsData] = await Promise.all([buscarOrdens(), buscarUsuarios()])
    ordens.value = ordsData
    usuarios.value = usrsData
  } catch (e: any) {
    errorMessage.value = 'Não foi possível carregar os dados. Verifique o backend.'
    console.error(e)
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <div class="pl-layout">
    <AppHeaderRoles />

    <div class="pl-workspace">
      <SidebarPlanejador />

      <main class="pl-main">
        <!-- ── topbar ── -->
        <header class="topbar">
          <div class="topbar-left">
            <h1 class="topbar-title">Ordens de Manutenção</h1>
          </div>
          <div class="topbar-actions">
            <button class="btn btn-primary btn-sm" type="button" @click="abrirCriar">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
              </svg>
              Criar Ordem
            </button>
          </div>
        </header>

        <div class="pl-content">

          <!-- ── cards de resumo ── -->
          <section class="summary-grid">
            <article class="summary-card summary-create" @click="abrirCriar">
              <div class="summary-icon summary-icon--blue">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <line x1="12" y1="5" x2="12" y2="19" /><line x1="5" y1="12" x2="19" y2="12" />
                </svg>
              </div>
              <div class="summary-label">Criar Ordem</div>
              <div class="summary-sub">Nova manutenção</div>
            </article>

            <article class="summary-card">
              <div class="summary-icon summary-icon--gray">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/>
                  <path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>
                </svg>
              </div>
              <div class="summary-label">Em andamento</div>
              <div class="summary-sub">{{ totalEmAndamento }} ordens</div>
            </article>

            <article class="summary-card">
              <div class="summary-icon summary-icon--gray">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                </svg>
              </div>
              <div class="summary-label">Total de ordens</div>
              <div class="summary-sub">{{ ordens.length }} registros</div>
            </article>

            <article class="summary-card">
              <div class="summary-icon summary-icon--red">
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                  <circle cx="12" cy="12" r="10"/>
                  <line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/>
                </svg>
              </div>
              <div class="summary-label" :class="totalAtrasadas > 0 ? 'text-red' : ''">Atrasadas</div>
              <div class="summary-sub" :class="totalAtrasadas > 0 ? 'text-red' : ''">{{ totalAtrasadas }} ordens</div>
            </article>
          </section>

          <!-- ── tabela de ordens ── -->
          <section class="card orders-card">
            <div class="card-header">
              <div>
                <div class="card-title">Todas as Ordens</div>
                <div class="card-sub">{{ ordensFiltradas.length }} ordem(s) no período</div>
              </div>
            </div>

            <!-- filtros -->
            <div class="filters-bar">
              <div class="search-wrap">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
                </svg>
                <input v-model="busca" type="text" placeholder="Buscar ordem ou ativo..." class="search-input" />
              </div>

              <select v-model="filtroTipo" class="filter-select">
                <option value="">Todos os tipos</option>
                <option value="PREVENTIVA">Preventiva</option>
                <option value="CORRETIVA">Corretiva</option>
              </select>

              <select v-model="filtroStatus" class="filter-select">
                <option value="">Todos os status</option>
                <option value="PENDENTE">Pendente</option>
                <option value="EM_ANDAMENTO">Em andamento</option>
                <option value="CONCLUIDA">Concluída</option>
                <option value="CANCELADA">Cancelada</option>
              </select>
            </div>

            <!-- mensagem de erro global -->
            <div v-if="errorMessage" class="error-banner">
              {{ errorMessage }}
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Nome da ordem</th>
                    <th>Tipo</th>
                    <th>Ativo</th>
                    <th>Início</th>
                    <th>Prazo</th>
                    <th>Status</th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  <tr v-if="isLoading">
                    <td colspan="8" class="table-empty">Carregando ordens...</td>
                  </tr>

                  <tr v-else-if="ordensFiltradas.length === 0">
                    <td colspan="8" class="table-empty">
                      Nenhuma ordem encontrada. Crie a primeira com o botão
                      <strong>Criar Ordem</strong>.
                    </td>
                  </tr>

                  <template v-else>
                    <tr v-for="o in ordensFiltradas" :key="o.id" :class="{ 'row-atrasada': isAtrasada(o) }">
                      <td class="col-id">{{ String(o.id).padStart(4, '0') }}</td>
                      <td>
                        <span class="order-name">{{ o.nome }}</span>
                      </td>
                      <td>
                        <span class="badge" :class="classTipo(o.tipoManutencao)">
                          {{ labelTipo(o.tipoManutencao) }}
                        </span>
                      </td>
                      <td class="col-ativo">{{ labelAtivo(o) }}</td>
                      <td>{{ formatarData(o.dataInicio) }}</td>
                      <td :class="{ 'text-red': isAtrasada(o), 'text-bold': isAtrasada(o) }">
                        {{ formatarData(o.dataFim) }}
                      </td>
                      <td>
                        <span class="badge" :class="classStatus(o.status)">
                          {{ labelStatus(o.status) }}
                        </span>
                      </td>
                      <td>
                        <button class="link-button" type="button" @click="abrirDetalhe(o)">→</button>
                      </td>
                    </tr>
                  </template>
                </tbody>
              </table>
            </div>
          </section>

        </div><!-- /pl-content -->
      </main>
    </div>

    <!-- ── backdrop ── -->
    <div
      class="drawer-backdrop"
      :class="{ open: detalheAberto || criarAberto }"
      @click="detalheAberto ? fecharDetalhe() : fecharCriar()"
    />

    <!-- ── DRAWER: Detalhes da ordem ── -->
    <aside class="detail-overlay" :class="{ open: detalheAberto }">
      <div class="detail-header" v-if="ordemSelecionada">
        <button class="detail-close" type="button" @click="fecharDetalhe">✕</button>
        <div class="detail-header-badges">
          <span class="badge" :class="classTipo(ordemSelecionada.tipoManutencao)">
            {{ labelTipo(ordemSelecionada.tipoManutencao) }}
          </span>
          <span class="badge" :class="classStatus(ordemSelecionada.status)">
            {{ labelStatus(ordemSelecionada.status) }}
          </span>
        </div>
        <strong class="detail-title">#{{ String(ordemSelecionada.id).padStart(4, '0') }} — {{ ordemSelecionada.nome }}</strong>
      </div>

      <div class="detail-body detail-view" v-if="ordemSelecionada">
        <div class="detail-section">
          <div class="detail-row">
            <span class="detail-label">Descrição</span>
            <span class="detail-value">{{ ordemSelecionada.descricao || '—' }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Planta</span>
            <span class="detail-value">{{ ordemSelecionada.planta || '—' }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Prédio</span>
            <span class="detail-value">{{ ordemSelecionada.predio || '—' }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Ativo</span>
            <span class="detail-value">{{ labelAtivo(ordemSelecionada) }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Início previsto</span>
            <span class="detail-value">{{ formatarData(ordemSelecionada.dataInicio) }}</span>
          </div>
          <div class="detail-row">
            <span class="detail-label">Prazo</span>
            <span class="detail-value" :class="{ 'text-red text-bold': isAtrasada(ordemSelecionada) }">
              {{ formatarData(ordemSelecionada.dataFim) }}
              <span v-if="isAtrasada(ordemSelecionada)" class="atrasada-tag">Atrasada</span>
            </span>
          </div>
          <div class="detail-row" v-if="ordemSelecionada.requisito">
            <span class="detail-label">Requisito</span>
            <span class="detail-value"><span class="tag-requisito">{{ ordemSelecionada.requisito }}</span></span>
          </div>
          <div class="detail-row" v-if="ordemSelecionada.comentario">
            <span class="detail-label">Comentário</span>
            <span class="detail-value">{{ ordemSelecionada.comentario }}</span>
          </div>
        </div>

        <div class="detail-section" v-if="ordemSelecionada.usuarios && ordemSelecionada.usuarios.length > 0">
          <div class="detail-section-title">Técnicos Responsáveis</div>
          <div class="tecnicos-list">
            <div v-for="u in ordemSelecionada.usuarios" :key="u.id" class="tecnico-item">
              <div class="avatar-sm" :style="{ background: avatarColor(u.id) }">
                {{ iniciaisNome(u.nomeCompleto) }}
              </div>
              <div>
                <div class="tecnico-nome">{{ u.nomeCompleto }}</div>
                <div class="tecnico-cargo">{{ u.cargo || u.funcao || 'Técnico' }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="detail-section" v-else>
          <div class="detail-section-title">Técnicos Responsáveis</div>
          <p class="detail-empty">Nenhum técnico atribuído.</p>
        </div>
      </div>
    </aside>

    <!-- ── DRAWER: Criar ordem ── -->
    <aside class="detail-overlay" :class="{ open: criarAberto }">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="fecharCriar">✕</button>
        <strong>Criar Ordem de Manutenção</strong>
        <p>Preencha os dados da nova ordem.</p>
      </div>

      <form class="detail-body order-form" @submit.prevent="salvar">
        <div v-if="formError" class="form-error-banner">{{ formError }}</div>

        <label>
          Nome da ordem *
          <input v-model="form.nome" type="text" placeholder="Ex: PM mensal — Câmera P-63" />
        </label>

        <label>
          Descrição *
          <textarea v-model="form.descricao" rows="3" placeholder="Descreva o escopo da manutenção..." />
        </label>

        <div class="form-grid">
          <label>
            Planta *
            <input v-model="form.planta" type="text" placeholder="Ex: Planta 1" />
          </label>
          <label>
            Prédio *
            <input v-model="form.predio" type="text" placeholder="Ex: Prédio A" />
          </label>
        </div>

        <div class="form-grid">
          <label>
            Data prevista de início
            <input v-model="form.dataInicio" type="date" />
          </label>
          <label>
            Data prevista de final
            <input v-model="form.dataFim" type="date" />
          </label>
        </div>

        <label>
          Ativo *
          <input v-model.number="form.ativoId" type="number" min="1" placeholder="ID do ativo (ex: 1)" />
          <span class="field-hint">Informe o ID do ativo registrado no sistema.</span>
        </label>

        <label class="label-block">
          Técnicos Responsáveis
          <div class="tecnicos-picker">
            <div
              v-for="u in usuarios"
              :key="u.id"
              class="tecnico-option"
              :class="{ selected: tecnicosSelecionados.includes(u.id) }"
              @click="toggleTecnico(u.id)"
            >
              <div class="avatar-sm" :style="{ background: avatarColor(u.id) }">
                {{ iniciaisNome(u.nomeCompleto) }}
              </div>
              <div class="tecnico-option-info">
                <span class="tecnico-nome">{{ u.nomeCompleto }}</span>
                <span class="tecnico-cargo">{{ u.funcao || u.cargo || 'Técnico' }}</span>
              </div>
              <svg v-if="tecnicosSelecionados.includes(u.id)" class="check-icon" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                <polyline points="20 6 9 17 4 12" />
              </svg>
            </div>
            <p v-if="usuarios.length === 0" class="detail-empty">Nenhum usuário encontrado.</p>
          </div>
        </label>

        <label>
          Tipo de manutenção
          <select v-model="form.tipoManutencao">
            <option value="PREVENTIVA">Preventiva</option>
            <option value="CORRETIVA">Corretiva</option>
            <option value="INSTALACAO">Instalação</option>
            <option value="REMOCAO">Remoção</option>
          </select>
        </label>

        <label>
          Requisito
          <input v-model="form.requisito" type="text" placeholder="Ex: NR12, NR10..." />
        </label>

        <div class="drawer-actions">
          <button class="btn btn-secondary" type="button" :disabled="isSubmitting" @click="fecharCriar">
            Cancelar
          </button>
          <button class="btn btn-primary" type="submit" :disabled="isSubmitting">
            {{ isSubmitting ? 'Salvando...' : 'Salvar ordem' }}
          </button>
        </div>
      </form>
    </aside>
  </div>
</template>

<style scoped>
/* ── layout ── */
.pl-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--gray-50);
}
.pl-workspace {
  display: flex;
  flex: 1;
  min-height: calc(100vh - var(--app-header-roles-h));
}
.pl-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}
.pl-content {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

/* ── topbar ── */
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
  margin: 0;
}
.topbar-actions { display: flex; gap: 10px; }

/* ── summary cards ── */
.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}
.summary-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-lg);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  text-align: center;
  transition: box-shadow 0.15s;
}
.summary-create {
  cursor: pointer;
  border-color: var(--blue-200);
  background: var(--blue-50);
}
.summary-create:hover { box-shadow: 0 2px 8px rgba(59,130,246,.15); }
.summary-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.summary-icon--blue { background: var(--blue-100); color: var(--blue-600); }
.summary-icon--gray { background: var(--gray-100); color: var(--gray-500); }
.summary-icon--red  { background: var(--red-50);   color: var(--red-500);  }
.summary-label {
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-800);
}
.summary-create .summary-label { color: var(--blue-600); }
.summary-sub {
  font-size: 12px;
  color: var(--gray-500);
}

/* ── orders card ── */
.orders-card { display: flex; flex-direction: column; }
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  border-bottom: 1px solid var(--gray-100);
}
.card-title { font-size: 15px; font-weight: 600; color: var(--gray-900); }
.card-sub   { font-size: 12px; color: var(--gray-500); margin-top: 2px; }

/* ── filtros ── */
.filters-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 20px;
  border-bottom: 1px solid var(--gray-100);
  flex-wrap: wrap;
}
.search-wrap {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  min-width: 200px;
  background: var(--gray-50);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 0 12px;
  color: var(--gray-400);
}
.search-input {
  flex: 1;
  border: none;
  background: transparent;
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  padding: 8px 0;
}
.filter-select {
  padding: 7px 10px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  font: inherit;
  font-size: 12px;
  color: var(--gray-700);
  background: var(--white);
  cursor: pointer;
  outline: none;
}
.filter-select:focus { border-color: var(--blue-400); }

/* ── tabela ── */
.table-wrap { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; }
thead th {
  padding: 10px 16px;
  text-align: left;
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: .04em;
  color: var(--gray-500);
  border-bottom: 1px solid var(--gray-100);
  white-space: nowrap;
}
tbody td {
  padding: 13px 16px;
  font-size: 13px;
  color: var(--gray-700);
  border-bottom: 1px solid var(--gray-50);
  vertical-align: middle;
}
tbody tr:last-child td { border-bottom: none; }
tbody tr:hover td { background: var(--gray-50); }
.row-atrasada td { background: #fff9f9; }
.col-id   { font-variant-numeric: tabular-nums; color: var(--gray-400); font-size: 12px; }
.col-ativo { color: var(--gray-500); font-size: 12px; font-family: var(--font-mono, monospace); }
.order-name { font-weight: 500; color: var(--gray-900); }
.table-empty {
  padding: 40px 20px;
  text-align: center;
  color: var(--gray-400);
  font-size: 13px;
}

/* ── badges ── */
.badge {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 99px;
  font-size: 11px;
  font-weight: 600;
  white-space: nowrap;
}
.badge-preventiva  { background: #dbeafe; color: #1d4ed8; }
.badge-corretiva   { background: #fee2e2; color: #b91c1c; }
.badge-instalacao  { background: #f3e8ff; color: #7e22ce; }
.badge-remocao     { background: #ffedd5; color: #c2410c; }
.badge-pendente    { background: #fef9c3; color: #92400e; }
.badge-em-andamento{ background: #dbeafe; color: #1e40af; }
.badge-concluida   { background: #d1fae5; color: #065f46; }
.badge-cancelada   { background: var(--gray-100); color: var(--gray-500); }

/* ── textos utilitários ── */
.text-red  { color: #ef4444; }
.text-bold { font-weight: 600; }

/* ── link botão ── */
.link-button {
  border: 0;
  background: transparent;
  color: var(--blue-600);
  font-weight: 600;
  cursor: pointer;
  font-family: var(--font);
  font-size: 14px;
  padding: 0 4px;
}
.link-button:hover { color: var(--blue-700); }

/* ── erro global ── */
.error-banner {
  margin: 12px 20px;
  padding: 10px 14px;
  background: #fee2e2;
  border: 1px solid #fca5a5;
  border-radius: var(--radius-md);
  color: #b91c1c;
  font-size: 13px;
}

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
  width: 420px;
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
.detail-header strong { font-size: 15px; color: var(--gray-900); }
.detail-header p { margin-top: 4px; font-size: 13px; color: var(--gray-500); }
.detail-close {
  position: absolute;
  top: 16px;
  right: 16px;
  border: none;
  background: none;
  font-size: 16px;
  cursor: pointer;
  color: var(--gray-400);
  line-height: 1;
}
.detail-close:hover { color: var(--gray-700); }
.detail-body { padding: 20px; overflow-y: auto; flex: 1; }

/* ── form ── */
.order-form { display: flex; flex-direction: column; gap: 14px; }
.order-form label {
  display: flex;
  flex-direction: column;
  gap: 5px;
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-700);
}
.order-form input,
.order-form select,
.order-form textarea {
  padding: 9px 11px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  background: var(--white);
}
.order-form input:focus,
.order-form select:focus,
.order-form textarea:focus {
  border-color: var(--blue-400);
  box-shadow: 0 0 0 3px var(--blue-50);
}
.field-hint { font-size: 11px; font-weight: 400; color: var(--gray-400); }
.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.form-error-banner {
  padding: 9px 12px;
  background: #fee2e2;
  border: 1px solid #fca5a5;
  border-radius: var(--radius-md);
  color: #b91c1c;
  font-size: 12px;
  font-weight: 500;
}
.drawer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 8px;
  padding-top: 14px;
  border-top: 1px solid var(--gray-200);
}
.drawer-actions .btn:disabled { opacity: .6; cursor: not-allowed; }

/* ── detalhe: view ── */
.detail-header-badges {
  display: flex;
  gap: 6px;
  margin-bottom: 8px;
}
.detail-title {
  font-size: 15px;
  color: var(--gray-900);
  display: block;
  line-height: 1.4;
}
.detail-view { display: flex; flex-direction: column; gap: 20px; }
.detail-section {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.detail-section-title {
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .05em;
  color: var(--gray-400);
  margin-bottom: 4px;
}
.detail-row {
  display: flex;
  flex-direction: column;
  gap: 3px;
}
.detail-label {
  font-size: 11px;
  font-weight: 600;
  color: var(--gray-400);
  text-transform: uppercase;
  letter-spacing: .04em;
}
.detail-value {
  font-size: 13px;
  color: var(--gray-800);
  display: flex;
  align-items: center;
  gap: 6px;
}
.detail-empty {
  font-size: 13px;
  color: var(--gray-400);
  margin: 0;
}
.atrasada-tag {
  display: inline-block;
  padding: 1px 7px;
  background: #fee2e2;
  color: #b91c1c;
  border-radius: 99px;
  font-size: 10px;
  font-weight: 700;
}
.tag-requisito {
  display: inline-block;
  padding: 2px 8px;
  background: #f0fdf4;
  color: #15803d;
  border: 1px solid #bbf7d0;
  border-radius: var(--radius-md);
  font-size: 12px;
  font-weight: 600;
}

/* ── técnicos ── */
.tecnicos-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.tecnico-item {
  display: flex;
  align-items: center;
  gap: 10px;
}
.avatar-sm {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  color: white;
  flex-shrink: 0;
}
.tecnico-nome {
  font-size: 13px;
  font-weight: 500;
  color: var(--gray-900);
}
.tecnico-cargo {
  font-size: 11px;
  color: var(--gray-400);
}

/* ── picker de técnicos no form ── */
.label-block { display: flex; flex-direction: column; gap: 6px; }
.tecnicos-picker {
  display: flex;
  flex-direction: column;
  gap: 6px;
  max-height: 200px;
  overflow-y: auto;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 6px;
}
.tecnico-option {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 6px;
  cursor: pointer;
  transition: background .12s;
}
.tecnico-option:hover { background: var(--gray-50); }
.tecnico-option.selected { background: var(--blue-50); }
.tecnico-option-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 1px;
}
.check-icon { color: var(--blue-600); flex-shrink: 0; }

/* ── responsivo ── */
@media (max-width: 1024px) {
  .summary-grid { grid-template-columns: 1fr 1fr; }
}
@media (max-width: 768px) {
  .pl-workspace { flex-direction: column; }
  .summary-grid { grid-template-columns: 1fr 1fr; }
  .detail-overlay { width: 100vw; }
  .form-grid { grid-template-columns: 1fr; }
}
</style>
