<script setup lang="ts">
import SupervisorLayout from '@/components/supervisor/SupervisorLayout.vue'
import type { IAtivo } from '@/services/AtivoService'
import {
  calcularMtbfAtivo,
  calcularMttrAtivo,
  formatarMtbf,
  formatarMttr,
  isEmManutencao,
  isInativo,
  isOperacional,
  labelStatusAtivo,
} from '@/services/AtivoMetricService'
import type { IOrdem } from '@/services/OrdemService'
import { carregarContextoSupervisor } from '@/services/SupervisorContextService'
import type { ISupervisorContext } from '@/services/SupervisorContextService'
import { computed, onMounted, ref } from 'vue'

type AtivoComMetricas = IAtivo & { mttr: number | null; mtbf: number | null }

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
const ordens = computed<IOrdem[]>(() => ctx.value?.ordensContrato ?? [])

const ativosComMetricas = computed<AtivoComMetricas[]>(() =>
  (ctx.value?.ativos ?? []).map((a) => ({
    ...a,
    mttr: calcularMttrAtivo(a.id, ordens.value),
    mtbf: calcularMtbfAtivo(a.id, ordens.value, a.periodicidadeManutencao),
  })),
)

const ativosFiltrados = computed(() => {
  const q = busca.value.trim().toLowerCase()
  if (!q) return ativosComMetricas.value
  return ativosComMetricas.value.filter(
    (a) =>
      a.nome?.toLowerCase().includes(q) ||
      a.tipo?.toLowerCase().includes(q) ||
      a.planta?.toLowerCase().includes(q),
  )
})

function mttrExibicao(a: AtivoComMetricas): string {
  if (a.mttr !== null) return formatarMttr(a.mttr)
  if (a.periodicidadeManutencao) return formatarMttr(a.periodicidadeManutencao * 0.25)
  return '—'
}

function mtbfExibicao(a: AtivoComMetricas): string {
  return formatarMtbf(a.mtbf)
}
</script>

<template>
  <SupervisorLayout :title="nomeContrato" subtitle="Ativos do contrato">
    <section class="card">
      <div class="card-header">
        <div>
          <div class="card-title">Ativos</div>
          <div class="card-sub">
            {{
              isLoading
                ? 'Carregando...'
                : `${ativosFiltrados.length} ativo(s) no contrato`
            }}
          </div>
        </div>
        <div class="search-input">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="11" cy="11" r="8" />
            <line x1="21" y1="21" x2="16.65" y2="16.65" />
          </svg>
          <input v-model="busca" type="text" placeholder="Buscar ativo..." />
        </div>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Nome</th>
              <th>Tipo</th>
              <th>Planta</th>
              <th>Status</th>
              <th>MTTR</th>
              <th>MTBF</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isLoading">
              <td colspan="6" class="table-empty">Carregando ativos...</td>
            </tr>
            <tr v-else-if="ativosFiltrados.length === 0">
              <td colspan="6" class="table-empty">Nenhum ativo encontrado para este contrato.</td>
            </tr>
            <tr v-for="a in ativosFiltrados" :key="a.id">
              <td><strong class="table-title">{{ a.nome || '—' }}</strong></td>
              <td>{{ a.tipo || '—' }}</td>
              <td>{{ a.planta || '—' }}</td>
              <td>
                <span
                  class="badge"
                  :class="{
                    'badge-active': isOperacional(a.status),
                    'badge-pending': isEmManutencao(a.status),
                    'badge-done': isInativo(a.status),
                  }"
                >
                  {{ labelStatusAtivo(a.status) }}
                </span>
              </td>
              <td class="cell-mono">{{ mttrExibicao(a) }}</td>
              <td class="cell-mono">{{ mtbfExibicao(a) }}</td>
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

.table-title {
  color: var(--gray-900);
  font-weight: 600;
}

.cell-mono {
  font-family: var(--mono);
  font-size: 12px;
}

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
}
</style>
