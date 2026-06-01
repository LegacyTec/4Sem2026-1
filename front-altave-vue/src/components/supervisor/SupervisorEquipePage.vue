<script setup lang="ts">
import SupervisorLayout from '@/components/supervisor/SupervisorLayout.vue'
import { carregarContextoSupervisor } from '@/services/SupervisorContextService'
import type { IMembroContrato, ISupervisorContext } from '@/services/SupervisorContextService'
import { computed, onMounted, ref } from 'vue'

const ctx = ref<ISupervisorContext | null>(null)
const isLoading = ref(false)
const busca = ref('')

onMounted(async () => {
  isLoading.value = true
  try {
    ctx.value = await carregarContextoSupervisor()
  } catch (e) {
    console.error(e)
  } finally {
    isLoading.value = false
  }
})

const nomeContrato = computed(() => ctx.value?.contrato?.nomeEmpresa ?? 'Contrato')

const membrosFiltrados = computed<IMembroContrato[]>(() => {
  const lista = ctx.value?.membros ?? []
  const q = busca.value.trim().toLowerCase()
  if (!q) return lista
  return lista.filter(
    (m) =>
      m.nomeCompleto.toLowerCase().includes(q) ||
      m.cargo?.toLowerCase().includes(q) ||
      m.funcao?.toLowerCase().includes(q),
  )
})

function iniciais(nome: string): string {
  return nome
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((p) => p.charAt(0).toUpperCase())
    .join('')
}
</script>

<template>
  <SupervisorLayout :title="nomeContrato" subtitle="Minha equipe">
    <section class="card">
      <div class="card-header">
        <div>
          <div class="card-title">Membros do Contrato</div>
          <div class="card-sub">
            {{
              isLoading
                ? 'Carregando...'
                : `${membrosFiltrados.length} membro(s) vinculado(s)`
            }}
          </div>
        </div>
        <div class="search-input">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input v-model="busca" type="text" placeholder="Buscar membro..." />
        </div>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Membro</th>
              <th>Cargo</th>
              <th>Função</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading">
              <td colspan="3" class="table-empty">Carregando equipe...</td>
            </tr>
            <tr v-else-if="membrosFiltrados.length === 0">
              <td colspan="3" class="table-empty">Nenhum membro vinculado a este contrato.</td>
            </tr>
            <tr v-for="m in membrosFiltrados" :key="m.id">
              <td>
                <div class="member-cell">
                  <div class="avatar">{{ iniciais(m.nomeCompleto) }}</div>
                  <span class="member-name">{{ m.nomeCompleto }}</span>
                </div>
              </td>
              <td>{{ m.cargo || '—' }}</td>
              <td>{{ m.funcao || '—' }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </SupervisorLayout>
</template>

<style scoped>
.card-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 16px;
  flex-wrap: wrap;
  padding: 16px 20px;
  border-bottom: 1px solid var(--gray-200);
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-900);
}

.card-sub {
  font-size: 12px;
  color: var(--gray-500);
  margin-top: 2px;
}

.search-input {
  display: flex;
  align-items: center;
  gap: 6px;
  background: var(--gray-50);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md, 6px);
  padding: 6px 10px;
  color: var(--gray-400);
}

.search-input input {
  border: none;
  background: transparent;
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  outline: none;
  min-width: 180px;
}

.member-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: var(--blue-400, #60a5fa);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 11px;
  font-weight: 700;
  flex-shrink: 0;
}

.member-name {
  font-weight: 500;
  color: var(--gray-900);
}

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
}
</style>
