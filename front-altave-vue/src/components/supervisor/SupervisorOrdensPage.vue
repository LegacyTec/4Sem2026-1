<script setup lang="ts">
import SupervisorLayout from '@/components/supervisor/SupervisorLayout.vue'
import {
  formatarData,
  iniciaisNome,
  avatarColor,
  labelTipo,
  labelStatus,
} from '@/services/OrdemService'
import type { IOrdem } from '@/services/OrdemService'
import { carregarContextoSupervisor } from '@/services/SupervisorContextService'
import type { ISupervisorContext } from '@/services/SupervisorContextService'
import { computed, onMounted, ref } from 'vue'

const ctx = ref<ISupervisorContext | null>(null)
const isLoading = ref(false)
const filtroTipo = ref('')
const filtroStatus = ref('')

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

const ordensAbertas = computed<IOrdem[]>(() => ctx.value?.ordensAbertas ?? [])

const ordensFiltradas = computed(() =>
  ordensAbertas.value.filter((o) => {
    const tipoOk = !filtroTipo.value || o.tipoManutencao === filtroTipo.value
    const statusOk = !filtroStatus.value || o.status === filtroStatus.value
    return tipoOk && statusOk
  }),
)

function padId(id: number): string {
  return String(id).padStart(4, '0')
}

function isLate(o: IOrdem): boolean {
  if (!o.dataFim) return false
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
  <SupervisorLayout :title="nomeContrato" subtitle="Ordens abertas">
    <section class="card">
      <div class="card-header">
        <div>
          <div class="card-title">Ordens Abertas</div>
          <div class="card-sub">
            {{
              isLoading
                ? 'Carregando...'
                : `${ordensFiltradas.length} ordem(ns) pendente(s) ou em andamento`
            }}
          </div>
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
              <td colspan="7" class="table-empty">Nenhuma ordem aberta para os ativos deste contrato.</td>
            </tr>
            <tr v-for="o in ordensFiltradas" :key="o.id">
              <td><span class="order-id">#OM-{{ padId(o.id) }}</span></td>
              <td>
                <span class="badge" :class="tipoBadgeClass(o.tipoManutencao)">
                  {{ labelTipo(o.tipoManutencao) }}
                </span>
              </td>
              <td>{{ o.ativo?.tipo ?? o.ativo?.id ?? '—' }}</td>
              <td>
                <div v-if="o.usuarios && o.usuarios.length > 0" class="tech-cell">
                  <div
                    class="avatar-sm"
                    :style="{ background: avatarColor(o.usuarios.at(0)?.id ?? 0) }"
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

.header-filters {
  display: flex;
  align-items: center;
  gap: 8px;
}

.filter-select {
  padding: 6px 10px;
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md, 6px);
  font: inherit;
  font-size: 13px;
  color: var(--gray-900);
  background: var(--white);
  cursor: pointer;
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
.text-late { color: var(--red); font-weight: 500; }

.table-empty {
  padding: 28px 20px !important;
  text-align: center;
  color: var(--gray-500);
  font-size: 14px;
}

.tipo-prev { background: var(--blue-50); color: var(--blue-700); }
.tipo-corr { background: #fff7ed; color: #c2410c; }
.tipo-inst { background: #ede9fe; color: #5b21b6; }
.tipo-rem  { background: var(--gray-100); color: var(--gray-500); }
</style>
