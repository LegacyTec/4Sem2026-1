<script setup lang="ts">
import AppHeaderRoles from '@/components/geral/layout/AppHeaderRoles.vue'
import SidebarAdm from '@/components/geral/layout/SidebarAdm.vue'
</script>

<template>
  <div class="adm-layout">
    <AppHeaderRoles />
    <div class="adm-workspace">
      <SidebarAdm />
      <main class="adm-main">
        <header class="topbar">
          <div>
            <span class="topbar-title">Ativos</span>
            <span class="topbar-sub">Gestão de ativos</span>
          </div>
        </header>

        <section class="content-area">
          <div class="card empty-card">
            <div class="empty-icon">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <rect x="2" y="7" width="20" height="14" rx="2"/>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"/>
              </svg>
            </div>
            <div class="empty-title">Módulo de Ativos</div>
            <p class="empty-desc">
              Os ativos são gerenciados dentro de cada contrato.<br>
              Acesse <strong>Contratos → Ver → Aba Ativos</strong> para visualizar e adicionar ativos.
            </p>
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
  height: var(--topbar-h); background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px; flex-shrink: 0; position: sticky; top: 0; z-index: 10;
}
.topbar-title { font-size:16px; font-weight:600; color:var(--gray-900); }
.topbar-sub   { font-size:12px; color:var(--gray-500); margin-left:8px; }
.content-area { padding:24px; }
.empty-card {
  display: flex; flex-direction: column;
  align-items: center; justify-content: center;
  padding: 64px 32px; text-align: center; gap: 16px;
}
.empty-icon { color: var(--gray-300); }
.empty-title { font-size:18px; font-weight:600; color:var(--gray-700); }
.empty-desc { font-size:14px; color:var(--gray-500); line-height:1.7; margin:0; }
</style>
