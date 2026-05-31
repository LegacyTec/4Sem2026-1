<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login, roleHome } from '@/services/AuthService'

const router = useRouter()
const email = ref('')
const senha = ref('')
const erro = ref('')
const carregando = ref(false)

async function entrar() {
  erro.value = ''
  if (!email.value.trim() || !senha.value) {
    erro.value = 'Informe e-mail e senha.'
    return
  }
  carregando.value = true
  try {
    const user = await login(email.value.trim(), senha.value)
    router.push(roleHome(user.cargo))
  } catch {
    erro.value = 'E-mail ou senha inválidos.'
  } finally {
    carregando.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-brand">
        <span class="login-logo">ALTAVE</span>
        <span class="login-sep">—</span>
        <span class="login-product">SGM</span>
      </div>
      <h1 class="login-title">Entrar no sistema</h1>
      <p class="login-sub">Sistema de Gestão de Manutenções</p>

      <form class="login-form" @submit.prevent="entrar">
        <label class="login-label">
          E-mail
          <input
            v-model="email"
            type="email"
            class="login-input"
            placeholder="seu.nome@altave.com"
            autocomplete="username"
          />
        </label>

        <label class="login-label">
          Senha
          <input
            v-model="senha"
            type="password"
            class="login-input"
            placeholder="••••••"
            autocomplete="current-password"
          />
        </label>

        <p v-if="erro" class="login-error">{{ erro }}</p>

        <button class="login-btn" type="submit" :disabled="carregando">
          {{ carregando ? 'Entrando...' : 'Entrar' }}
        </button>
      </form>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--blue-900, #0b1f33);
  padding: 24px;
}

.login-card {
  background: var(--white);
  border-radius: 16px;
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.25);
  padding: 40px 36px;
  width: 100%;
  max-width: 400px;
}

.login-brand {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 28px;
}

.login-logo {
  font-size: 18px;
  font-weight: 700;
  letter-spacing: 0.06em;
  color: var(--blue-700, #1d4ed8);
}

.login-sep {
  color: var(--gray-300);
}

.login-product {
  font-size: 14px;
  font-weight: 600;
  color: var(--gray-500);
  letter-spacing: 0.04em;
}

.login-title {
  font-size: 22px;
  font-weight: 700;
  color: var(--gray-900);
  margin: 0 0 4px;
}

.login-sub {
  font-size: 13px;
  color: var(--gray-500);
  margin: 0 0 28px;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.login-label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-size: 12px;
  font-weight: 600;
  color: var(--gray-700);
}

.login-input {
  padding: 11px 13px;
  border: 1px solid var(--gray-300);
  border-radius: 8px;
  font: inherit;
  font-size: 14px;
  color: var(--gray-900);
  outline: none;
  background: var(--white);
  transition: all 0.15s;
}

.login-input:focus {
  border-color: var(--blue-400);
  box-shadow: 0 0 0 3px var(--blue-50);
}

.login-error {
  background: var(--red-bg, #fef2f2);
  color: #991b1b;
  border-radius: 8px;
  padding: 9px 12px;
  font-size: 13px;
  margin: 0;
}

.login-btn {
  margin-top: 6px;
  padding: 12px;
  border: none;
  border-radius: 8px;
  background: var(--blue-600, #2563eb);
  color: white;
  font: inherit;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.15s;
}

.login-btn:hover:not(:disabled) {
  background: var(--blue-700, #1d4ed8);
}

.login-btn:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}
</style>
