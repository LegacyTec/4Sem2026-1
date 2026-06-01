<script setup lang="ts">
import { getCurrentUser } from '@/services/AuthService'
import { carregarContextoSupervisor } from '@/services/SupervisorContextService'
import { onMounted, ref } from 'vue'

const totalAtivos = ref(0)
const totalEquipe = ref(0)
const totalOrdensAbertas = ref(0)

const user = getCurrentUser()
const iniciais = user
  ? user.nomeCompleto
      .split(' ')
      .filter(Boolean)
      .slice(0, 2)
      .map((p) => p.charAt(0).toUpperCase())
      .join('')
  : '—'
const nomeUsuario = user?.nomeCompleto ?? 'Supervisor'

onMounted(async () => {
  try {
    const ctx = await carregarContextoSupervisor()
    totalAtivos.value = ctx.ativos.length
    totalEquipe.value = ctx.membros.length
    totalOrdensAbertas.value = ctx.ordensAbertas.length
  } catch {
    /* badges ficam em 0 */
  }
})
</script>

<template>
  <aside class="sv-sidebar" aria-label="Navegação do supervisor">
    <div class="sidebar-logo">
      <div class="logo-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
        </svg>
      </div>
      <div>
        <div class="logo-text">Altave</div>
        <div class="logo-sub">SGM · Supervisor</div>
      </div>
    </div>

    <nav class="sidebar-section">
      <div class="sidebar-section-label">Visão Geral</div>

      <RouterLink to="/supervisor/inicio" class="nav-item" active-class="active">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="3" width="7" height="7" />
          <rect x="14" y="3" width="7" height="7" />
          <rect x="3" y="14" width="7" height="7" />
          <rect x="14" y="14" width="7" height="7" />
        </svg>
        Dashboard
      </RouterLink>

      <RouterLink to="/supervisor/ativos" class="nav-item" active-class="active">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z" />
          <polyline points="3.27 6.96 12 12.01 20.73 6.96" />
          <line x1="12" y1="22.08" x2="12" y2="12" />
        </svg>
        Ativos
        <span v-if="totalAtivos > 0" class="nav-badge">{{ totalAtivos }}</span>
      </RouterLink>

      <RouterLink to="/supervisor/equipe" class="nav-item" active-class="active">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
          <circle cx="9" cy="7" r="4" />
          <path d="M23 21v-2a4 4 0 0 0-3-3.87" />
          <path d="M16 3.13a4 4 0 0 1 0 7.75" />
        </svg>
        Minha Equipe
        <span v-if="totalEquipe > 0" class="nav-badge">{{ totalEquipe }}</span>
      </RouterLink>

      <RouterLink to="/supervisor/ordens" class="nav-item" active-class="active">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M9 5H7a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7a2 2 0 0 0-2-2h-2" />
          <rect x="9" y="3" width="6" height="4" rx="2" />
          <line x1="9" y1="12" x2="15" y2="12" />
          <line x1="9" y1="16" x2="12" y2="16" />
        </svg>
        Ordens
        <span v-if="totalOrdensAbertas > 0" class="nav-badge danger">{{ totalOrdensAbertas }}</span>
      </RouterLink>
    </nav>

    <div class="sidebar-footer">
      <div class="user-card">
        <div class="avatar avatar-blue" aria-hidden="true">{{ iniciais }}</div>
        <div class="user-info">
          <div class="user-name">{{ nomeUsuario }}</div>
          <div class="user-role">Supervisor</div>
        </div>
        <div class="status-dot" title="Status da sessão" />
      </div>
    </div>
  </aside>
</template>

<style scoped>
.sv-sidebar {
  width: var(--sidebar-w);
  background: var(--blue-700);
  display: flex;
  flex-direction: column;
  overflow-y: auto;
  flex-shrink: 0;
  align-self: stretch;
  min-height: 100%;
}

.sidebar-logo {
  padding: 20px 20px 16px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo-icon {
  width: 36px;
  height: 36px;
  background: var(--blue-400);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.logo-icon svg {
  width: 20px;
  height: 20px;
  fill: white;
}

.logo-text {
  font-size: 15px;
  font-weight: 600;
  color: white;
  letter-spacing: 0.04em;
}

.logo-sub {
  font-size: 9px;
  color: rgba(255, 255, 255, 0.45);
  letter-spacing: 0.05em;
  text-transform: uppercase;
  line-height: 1.3;
}

.sidebar-section {
  padding: 12px 12px 4px;
}

.sidebar-section-label {
  font-size: 10px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: rgba(255, 255, 255, 0.35);
  padding: 0 8px;
  margin-bottom: 4px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.65);
  cursor: pointer;
  transition: all 0.15s;
  font-size: 13.5px;
  font-weight: 400;
  margin-bottom: 1px;
  text-decoration: none;
}

.nav-item:hover,
.nav-item.active {
  background: rgba(255, 255, 255, 0.15);
  color: white;
}

.nav-item.active {
  font-weight: 500;
}

.nav-icon {
  width: 16px;
  height: 16px;
  opacity: 0.8;
  flex-shrink: 0;
}

.nav-badge {
  margin-left: auto;
  background: var(--blue-400);
  color: white;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 20px;
  min-width: 18px;
  text-align: center;
}

.nav-badge.danger {
  background: var(--red);
}

.sidebar-footer {
  margin-top: auto;
  padding: 12px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.user-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 10px;
  border-radius: 8px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}

.avatar-blue {
  background: var(--blue-400);
  color: white;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-name {
  font-size: 12.5px;
  font-weight: 500;
  color: white;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.user-role {
  font-size: 10.5px;
  color: rgba(255, 255, 255, 0.4);
}

.status-dot {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  flex-shrink: 0;
  background: #22c55e;
}

@media (max-width: 860px) {
  .sv-sidebar {
    width: 100%;
    min-height: auto;
  }

  .sidebar-section {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 4px;
  }

  .sidebar-section-label {
    grid-column: 1 / -1;
  }
}
</style>
