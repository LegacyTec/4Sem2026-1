<script setup lang="ts">
import { computed } from 'vue'
import { RouterLink, useRoute } from 'vue-router'

const route = useRoute()

const tabs = [
  { id: 'adm' as const, label: 'ADM', to: '/adm/inicio' },
  { id: 'supervisor' as const, label: 'Supervisor', to: '/supervisor/inicio' },
  { id: 'planejador' as const, label: 'Planejador', to: '/planejador/inicio' },
  { id: 'tecnico' as const, label: 'Técnico', to: '/tecnico/inicio' },
]

const activeId = computed(() => {
  const p = route.path
  if (p.startsWith('/adm')) return 'adm'
  if (p.startsWith('/supervisor')) return 'supervisor'
  if (p.startsWith('/planejador')) return 'planejador'
  if (p.startsWith('/tecnico')) return 'tecnico'
  return 'adm'
})
</script>

<template>
  <header class="app-header-roles" role="banner">
    <div class="app-header-brand">
      <span class="app-header-logo">ALTAVE</span>
      <span class="app-header-sep" aria-hidden="true">—</span>
      <span class="app-header-product">SGM</span>
    </div>

    <nav class="app-header-tabs" aria-label="Perfis do sistema">
      <RouterLink
        v-for="tab in tabs"
        :key="tab.id"
        :to="tab.to"
        class="app-header-tab"
        :class="{ active: activeId === tab.id }"
      >
        {{ tab.label }}
      </RouterLink>
    </nav>
  </header>
</template>

<style scoped>
.app-header-roles {
  height: var(--app-header-roles-h);
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  gap: 16px;
  background: var(--blue-900);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  z-index: 30;
}

.app-header-brand {
  display: flex;
  align-items: baseline;
  gap: 8px;
  flex-shrink: 0;
}

.app-header-logo {
  font-size: 14px;
  font-weight: 700;
  letter-spacing: 0.06em;
  color: var(--white);
}

.app-header-sep {
  color: rgba(255, 255, 255, 0.35);
  font-weight: 400;
  font-size: 13px;
}

.app-header-product {
  font-size: 13px;
  font-weight: 600;
  color: rgba(255, 255, 255, 0.85);
  letter-spacing: 0.04em;
}

.app-header-tabs {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
  justify-content: center;
}

.app-header-tab {
  padding: 8px 14px;
  border-radius: var(--radius-sm);
  font-size: 13px;
  font-weight: 500;
  color: rgba(255, 255, 255, 0.55);
  text-decoration: none;
  transition: background 0.15s, color 0.15s;
}

.app-header-tab:hover {
  color: var(--white);
  background: rgba(255, 255, 255, 0.08);
}

.app-header-tab.active {
  color: var(--white);
  background: rgba(255, 255, 255, 0.18);
  font-weight: 600;
}

@media (max-width: 720px) {
  .app-header-roles {
    flex-direction: column;
    height: auto;
    padding: 10px 12px;
    gap: 10px;
  }

  .app-header-tabs {
    width: 100%;
    justify-content: flex-start;
  }
}
</style>
