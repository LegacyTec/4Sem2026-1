<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
import { buscarUsuarios, type IUsuario } from '@/services/OrdemService'
import { computed, onMounted, ref } from 'vue'

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
</style>
