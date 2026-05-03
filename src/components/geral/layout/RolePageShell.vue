<script setup lang="ts">
import PageTopbar from '@/components/geral/layout/PageTopbar.vue'

const props = withDefaults(
  defineProps<{
    topbarTitle: string
    topbarSubtitle?: string
    eyebrow: string
    pageTitle: string
    /** Sidebar fixa (ADM / supervisor / planejador). Falso quando a sidebar está no fluxo (ex.: técnico com AppSidebar). */
    fixedSidebar?: boolean
  }>(),
  { fixedSidebar: true },
)
</script>

<template>
  <div v-if="props.fixedSidebar" class="role-page">
    <slot name="sidebar" />
    <main class="role-main">
      <PageTopbar :title="topbarTitle" :subtitle="topbarSubtitle">
        <template v-if="$slots['topbar-actions']" #actions>
          <slot name="topbar-actions" />
        </template>
      </PageTopbar>

      <section class="role-body content-area">
        <div class="role-intro">
          <p class="role-eyebrow">{{ eyebrow }}</p>
          <h1 class="role-heading">{{ pageTitle }}</h1>
        </div>
        <slot />
      </section>
    </main>
  </div>

  <main v-else class="role-main role-main--inline">
    <PageTopbar :title="topbarTitle" :subtitle="topbarSubtitle">
      <template v-if="$slots['topbar-actions']" #actions>
        <slot name="topbar-actions" />
      </template>
    </PageTopbar>

    <section class="role-body content-area">
      <div class="role-intro">
        <p class="role-eyebrow">{{ eyebrow }}</p>
        <h1 class="role-heading">{{ pageTitle }}</h1>
      </div>
      <slot />
    </section>
  </main>
</template>

<style scoped>
.role-page {
  min-height: 100vh;
  background: var(--gray-50);
}

.role-main {
  min-height: 100vh;
  margin-left: var(--sidebar-w);
  display: flex;
  flex-direction: column;
}

.role-main--inline {
  margin-left: 0;
  flex: 1;
  min-height: 0;
  background: var(--gray-50);
}

.role-body {
  flex: 1;
}

.role-intro {
  margin-bottom: 24px;
}

.role-heading {
  font-size: 24px;
  line-height: 1.2;
  color: var(--gray-900);
  font-weight: 600;
}

.role-eyebrow {
  font-size: 11px;
  font-weight: 700;
  letter-spacing: 0.09em;
  color: var(--blue-600);
  text-transform: uppercase;
  margin-bottom: 6px;
}

@media (max-width: 860px) {
  .role-main:not(.role-main--inline) {
    margin-left: 0;
  }
}
</style>
