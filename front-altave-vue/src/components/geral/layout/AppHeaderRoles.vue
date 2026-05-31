<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { getCurrentUser, logout } from '@/services/AuthService'

const router = useRouter()

const user = computed(() => getCurrentUser())

const iniciais = computed(() => {
  const nome = user.value?.nomeCompleto ?? ''
  return nome
    .split(' ')
    .filter(Boolean)
    .slice(0, 2)
    .map((p) => p.charAt(0).toUpperCase())
    .join('')
})

function sair() {
  logout()
  router.push('/login')
}
</script>

<template>
  <header class="app-header-roles" role="banner">
    <div class="app-header-brand">
      <span class="app-header-logo">ALTAVE</span>
      <span class="app-header-sep" aria-hidden="true">—</span>
      <span class="app-header-product">SGM</span>
    </div>

    <div v-if="user" class="app-header-user">
      <div class="app-header-avatar" aria-hidden="true">{{ iniciais }}</div>
      <div class="app-header-user-info">
        <span class="app-header-user-name">{{ user.nomeCompleto }}</span>
        <span class="app-header-user-role">{{ user.cargo }}</span>
      </div>
      <button class="app-header-logout" type="button" @click="sair">
        <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
          <polyline points="16 17 21 12 16 7" />
          <line x1="21" y1="12" x2="9" y2="12" />
        </svg>
        Sair
      </button>
    </div>
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
  align-items: center;
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

.app-header-user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.app-header-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background: var(--blue-400, #60a5fa);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 600;
  flex-shrink: 0;
}

.app-header-user-info {
  display: flex;
  flex-direction: column;
  line-height: 1.2;
}

.app-header-user-name {
  font-size: 13px;
  font-weight: 600;
  color: var(--white);
}

.app-header-user-role {
  font-size: 11px;
  color: rgba(255, 255, 255, 0.5);
}

.app-header-logout {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-left: 8px;
  padding: 7px 12px;
  border: 1px solid rgba(255, 255, 255, 0.18);
  border-radius: var(--radius-sm, 6px);
  background: transparent;
  color: rgba(255, 255, 255, 0.75);
  font: inherit;
  font-size: 12.5px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
}

.app-header-logout:hover {
  background: rgba(255, 255, 255, 0.1);
  color: var(--white);
  border-color: rgba(255, 255, 255, 0.3);
}

@media (max-width: 720px) {
  .app-header-roles {
    padding: 10px 12px;
  }

  .app-header-user-info {
    display: none;
  }
}
</style>
