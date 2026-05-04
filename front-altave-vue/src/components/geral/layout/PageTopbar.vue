<script setup lang="ts">
defineProps<{
  title: string
  subtitle?: string
}>()
</script>

<template>
  <header class="page-topbar">
    <div>
      <span class="page-topbar-title">{{ title }}</span>
      <span v-if="subtitle" class="page-topbar-sub">{{ subtitle }}</span>
    </div>
    <div v-if="$slots.actions" class="page-topbar-actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<style scoped>
.page-topbar {
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

.page-topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
}

.page-topbar-sub {
  font-size: 12px;
  color: var(--gray-500);
  font-weight: 400;
  margin-left: 8px;
}

.page-topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}
</style>