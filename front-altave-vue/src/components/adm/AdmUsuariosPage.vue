<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { buscarUsuarios, type IUsuario } from '@/services/OrdemService'
import api from '@/config/axios'
import { computed, onMounted, reactive, ref } from 'vue'

const usuarios   = ref<IUsuario[]>([])
const isLoading  = ref(false)
const erro       = ref('')
const busca      = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    usuarios.value = await buscarUsuarios()
  } catch {
    erro.value = 'Erro ao carregar usuários.'
  } finally {
    isLoading.value = false
  }
})

const usuariosFiltrados = computed(() =>
  usuarios.value.filter(u =>
    !busca.value ||
    u.nomeCompleto.toLowerCase().includes(busca.value.toLowerCase()) ||
    (u.email ?? '').toLowerCase().includes(busca.value.toLowerCase()) ||
    (u.cargo  ?? '').toLowerCase().includes(busca.value.toLowerCase())
  )
)

function iniciais(nome: string) {
  return nome.split(' ').filter(Boolean).slice(0, 2).map(p => p.charAt(0).toUpperCase()).join('')
}

const CORES = ['#3b82f6','#8b5cf6','#ec4899','#f59e0b','#10b981','#ef4444','#06b6d4']
function cor(id: number) { return CORES[id % CORES.length] ?? '#3b82f6' }

const drawerAberto  = ref(false)
const isSaving      = ref(false)
const salvarErro    = ref('')
const novoUsuario   = reactive({
  nomeCompleto: '',
  email: '',
  senha: '',
  status: 'ATIVO',
  dataNascimento: '',
  cargo: '',
  funcao: '',
})

function abrirDrawer() { drawerAberto.value = true; salvarErro.value = '' }
function fecharDrawer() {
  drawerAberto.value = false
  salvarErro.value = ''
  Object.assign(novoUsuario, { nomeCompleto:'', email:'', senha:'', status:'ATIVO', dataNascimento:'', cargo:'', funcao:'' })
}

async function salvarUsuario() {
  salvarErro.value = ''
  if (
    !novoUsuario.nomeCompleto.trim() ||
    !novoUsuario.email.trim() ||
    !novoUsuario.senha.trim() ||
    !novoUsuario.cargo ||
    !novoUsuario.dataNascimento
  ) {
    salvarErro.value = 'Preencha todos os campos obrigatórios.'
    return
  }
  isSaving.value = true
  try {
    const response = await api.post('/usuario', {
      nomeCompleto: novoUsuario.nomeCompleto.trim(),
      email: novoUsuario.email.trim(),
      senha: novoUsuario.senha,
      status: novoUsuario.status,
      dataNascimento: novoUsuario.dataNascimento,
      cargo: novoUsuario.cargo.trim(),
      funcao: novoUsuario.funcao.trim(),
    })
    usuarios.value.unshift(response.data)
    fecharDrawer()
  } catch {
    salvarErro.value = 'Erro ao cadastrar usuário. Verifique os dados.'
  } finally {
    isSaving.value = false
  }
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
            <span class="topbar-title">Usuários</span>
            <span class="topbar-sub">{{ isLoading ? '...' : `${usuarios.length} usuário(s) cadastrado(s)` }}</span>
          </div>
          <div class="topbar-actions">
            <input v-model="busca" class="search-input" type="search" placeholder="Buscar por nome, e-mail ou cargo..." />
            <button class="btn btn-primary btn-sm" type="button" @click="abrirDrawer">
              <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>
              </svg>
              Cadastrar Usuário
            </button>
          </div>
        </header>

        <section class="content-area">
          <div v-if="isLoading" class="empty-state">Carregando usuários...</div>
          <div v-else-if="erro" class="empty-state error">{{ erro }}</div>
          <div v-else-if="usuariosFiltrados.length === 0" class="empty-state">Nenhum usuário encontrado.</div>

          <div v-else class="card">
            <div class="table-wrap">
              <table>
                <thead>
                  <tr>
                    <th>Usuário</th>
                    <th>E-mail</th>
                    <th>Cargo</th>
                    <th>Função</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="u in usuariosFiltrados" :key="u.id" class="row-hover">
                    <td>
                      <div class="user-cell">
                        <div class="avatar-sm" :style="{ background: cor(u.id) }">
                          {{ iniciais(u.nomeCompleto) }}
                        </div>
                        <span class="user-name">{{ u.nomeCompleto }}</span>
                      </div>
                    </td>
                    <td class="muted">{{ u.email || '—' }}</td>
                    <td>{{ u.cargo || '—' }}</td>
                    <td>{{ u.funcao || '—' }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </section>
      </main>
    </div>

    <!-- backdrop -->
    <div class="drawer-backdrop" :class="{ open: drawerAberto }" @click="fecharDrawer" />

    <!-- drawer -->
    <aside class="detail-overlay user-drawer" :class="{ open: drawerAberto }">
      <div class="detail-header">
        <button class="detail-close" type="button" @click="fecharDrawer">✕</button>
        <strong>Cadastrar Usuário</strong>
        <p>Preencha os dados do novo usuário.</p>
      </div>
      <form class="drawer-form" @submit.prevent="salvarUsuario">

        <div class="form-grid-2">
          <label>Nome completo *
            <input v-model="novoUsuario.nomeCompleto" type="text" placeholder="Ex: João Silva" />
          </label>

          <label>E-mail *
            <input v-model="novoUsuario.email" type="email" placeholder="joao@email.com" />
          </label>
        </div>

        <label>Senha *
          <input v-model="novoUsuario.senha" type="password" placeholder="Defina uma senha (ex: 12345)" />
          <small class="form-hint">
            Esta senha será usada no login do usuário.
          </small>
        </label>

        <div class="form-grid-2">
          <label>Cargo *
            <select v-model="novoUsuario.cargo" class="select-field">
              <option value="" disabled>Selecione o cargo...</option>
              <option value="ADM">ADM</option>
              <option value="Supervisor">Supervisor</option>
              <option value="Planejador">Planejador</option>
              <option value="Técnico">Técnico</option>
            </select>
          </label>

          <label>Status *
            <select v-model="novoUsuario.status" class="select-field">
              <option value="ATIVO">Ativo</option>
              <option value="INATIVO">Inativo</option>
            </select>
          </label>
        </div>

        <label>Função
          <input v-model="novoUsuario.funcao" type="text" placeholder="Ex: Inspeção, Manutenção..." />
        </label>

        <label>Data de nascimento *
          <input v-model="novoUsuario.dataNascimento" type="date" />
        </label>

        <p v-if="salvarErro" class="form-error">{{ salvarErro }}</p>

        <div class="drawer-actions">
          <button class="btn btn-secondary" type="button" @click="fecharDrawer">Cancelar</button>
          <button class="btn btn-primary" type="submit" :disabled="isSaving">
            {{ isSaving ? 'Salvando...' : 'Cadastrar' }}
          </button>
        </div>
      </form>
    </aside>
  </div>
</template>

<style scoped>
.adm-layout { min-height:100vh; display:flex; flex-direction:column; background:var(--gray-50); }
.adm-workspace { display:flex; flex:1; min-height:calc(100vh - var(--app-header-roles-h)); }
.adm-main { flex:1; min-width:0; display:flex; flex-direction:column; }

.topbar {
  height: var(--topbar-h);
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px; gap: 12px; flex-shrink: 0;
  position: sticky; top: 0; z-index: 10;
}
.topbar-title { font-size:16px; font-weight:600; color:var(--gray-900); }
.topbar-sub   { font-size:12px; color:var(--gray-500); margin-left:8px; }
.topbar-actions { display:flex; gap:10px; }

.search-input {
  padding: 8px 12px; border: 1px solid var(--gray-300);
  border-radius: var(--radius-md); font: inherit; font-size:13px;
  color: var(--gray-900); outline: none; width: 280px;
  background: var(--white);
}
.search-input:focus { border-color: var(--blue-400); box-shadow: 0 0 0 3px var(--blue-50); }

.content-area { padding:24px; }

.empty-state { padding:48px; text-align:center; color:var(--gray-500); font-size:14px; }
.empty-state.error { color:#ef4444; }

.user-cell { display:flex; align-items:center; gap:10px; }
.avatar-sm {
  width:32px; height:32px; border-radius:50%;
  color:white; display:flex; align-items:center; justify-content:center;
  font-size:12px; font-weight:600; flex-shrink:0;
}
.user-name { font-weight:500; color:var(--gray-900); }
.muted { color:var(--gray-500); }
.row-hover:hover { background:var(--gray-50); }

/* drawer */
.user-drawer {
  width: min(560px, 96vw);
}

.drawer-backdrop {
  position: fixed; inset: 0;
  background: rgba(11,31,51,.3);
  opacity: 0; visibility: hidden;
  transition: all .2s ease; z-index: 90;
}
.drawer-backdrop.open { opacity:1; visibility:visible; }

.drawer-form {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 20px;
}
.drawer-form label {
  display: flex; flex-direction: column; gap: 6px;
  font-size: 12px; font-weight: 600; color: var(--gray-700);
}
.drawer-form input, .drawer-form select {
  padding: 10px 12px;
  border: 1px solid var(--gray-300);
  border-radius: var(--radius-md);
  font: inherit; font-size: 13px;
  color: var(--gray-900); outline: none;
  background: var(--white);
}
.drawer-form input:focus, .drawer-form select:focus {
  border-color: var(--blue-400);
  box-shadow: 0 0 0 3px var(--blue-50);
}
.form-grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.select-field { width: 100%; cursor: pointer; }
.form-hint { font-size: 11px; color: var(--gray-500); font-weight: 500; }
.form-error { color: #ef4444; font-size: 12px; margin: 0; }
.drawer-actions {
  display: flex; justify-content: flex-end; gap: 10px;
  padding-top: 16px; border-top: 1px solid var(--gray-200);
}
.drawer-actions .btn:disabled { opacity: .65; cursor: not-allowed; }

@media (max-width: 540px) {
  .form-grid-2 { grid-template-columns: 1fr; }
}
</style>
