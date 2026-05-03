<script setup lang="ts">
import { RouterLink } from 'vue-router'

defineProps<{
  /** Texto abaixo do logo (ex.: Supervisor) */
  roleTag: string
  /** Rota ativa do item principal */
  homePath: string
}>()
</script>

<template>
  <aside class="stub-sidebar" :aria-label="`Navegação ${roleTag}`">
    <div class="stub-sidebar-logo">
      <div class="stub-logo-icon" aria-hidden="true">
        <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
        </svg>
      </div>
      <div>
        <div class="stub-logo-text">Altave</div>
        <div class="stub-logo-sub">{{ roleTag }}</div>
      </div>
    </div>

    <nav class="stub-sidebar-section">
      <div class="stub-sidebar-section-label">Menu</div>
      <RouterLink :to="homePath" class="stub-nav-item" active-class="active">
        <svg class="stub-nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="3" width="7" height="7" />
          <rect x="14" y="3" width="7" height="7" />
          <rect x="3" y="14" width="7" height="7" />
          <rect x="14" y="14" width="7" height="7" />
        </svg>
        Início
      </RouterLink>
    </nav>
  </aside>
</template>

<style scoped>
.stub-sidebar {
  width: var(--sidebar-w);
  background: var(--blue-700);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  overflow-y: auto;
  position: fixed;
  inset: 0 auto 0 0;
  z-index: 20;
}

.stub-sidebar-logo {
  padding: 20px 20px 16px;
  display: flex;
  align-items: center;
  gap: 10px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.stub-logo-icon {
  width: 36px;
  height: 36px;
  background: var(--blue-400);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.stub-logo-icon svg {
  width: 20px;
  height: 20px;
}

.stub-logo-text {
  font-size: 15px;
  font-weight: 600;
  color: white;
  letter-spacing: 0.04em;
}

.stub-logo-sub {
  font-size: 10px;
  color: rgba(255, 255, 255, 0.45);
  letter-spacing: 0.06em;
  text-transform: uppercase;
}

.stub-sidebar-section {
  padding: 12px 12px 4px;
}

.stub-sidebar-section-label {
  font-size: 10px;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: rgba(255, 255, 255, 0.35);
  padding: 0 8px;
  margin-bottom: 4px;
}

.stub-nav-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 9px 12px;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.65);
  cursor: pointer;
  transition: all 0.15s;
  font-size: 13.5px;
  margin-bottom: 1px;
  text-decoration: none;
}

.stub-nav-item:hover {
  background: rgba(255, 255, 255, 0.08);
  color: white;
}

.stub-nav-item.active {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  font-weight: 500;
}

.stub-nav-icon {
  width: 16px;
  height: 16px;
  opacity: 0.8;
  flex-shrink: 0;
}

@media (max-width: 860px) {
  .stub-sidebar {
    position: relative;
    width: 100%;
  }
}
</style>
