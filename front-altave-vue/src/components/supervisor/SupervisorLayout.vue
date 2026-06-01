<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarSupervisor from '@/components/geral/layout/SidebarSupervisor.vue'

defineProps<{
  title?: string
  subtitle?: string
}>()
</script>

<template>
  <div class="sv-layout">
    <AppHeaderRoles />

    <div class="sv-workspace">
      <SidebarSupervisor />

      <main class="sv-main">
        <header v-if="title" class="sv-topbar">
          <div class="topbar-left">
            <span class="topbar-title">{{ title }}</span>
            <span v-if="subtitle" class="topbar-sub">{{ subtitle }}</span>
          </div>
        </header>

        <section class="content-area sv-content">
          <slot />
        </section>
      </main>
    </div>
  </div>
</template>

<style scoped>
.sv-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--gray-50);
}

.sv-workspace {
  display: flex;
  flex: 1;
  min-height: 0;
}

.sv-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
}

.sv-topbar {
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
  align-items: baseline;
  gap: 8px;
  flex: 1;
  min-width: 0;
}

.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
  white-space: nowrap;
}

.topbar-sub {
  font-size: 12px;
  color: var(--gray-500);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-content {
  padding: 24px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

@media (max-width: 860px) {
  .sv-workspace { flex-direction: column; }

  .sv-topbar {
    height: auto;
    flex-direction: column;
    align-items: flex-start;
    padding: 14px 16px;
    gap: 10px;
  }

  .sv-content { padding: 16px; }
}
</style>
