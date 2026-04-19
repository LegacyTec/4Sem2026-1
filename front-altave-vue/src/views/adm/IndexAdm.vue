<script setup lang="ts">
import SidebarAdm from '@/components/layout/SidebarAdm.vue';
import { ref } from 'vue';

const isOpen = ref(false);

function abrirPainel() {
  isOpen.value = true;
}

function fecharPainel() {
  isOpen.value = false;
}


</script>

<template>
  <SidebarAdm />
  <div class="main">

    <div class="topbar">
      <div>
        <span class="topbar-title">Dashboard</span>
        <span class="topbar-sub">Visão geral do sistema</span>
      </div>
      <div class="topbar-actions">
        <button class="btn btn-secondary btn-sm">
          <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" />
            <line x1="16" y1="2" x2="16" y2="6" />
            <line x1="8" y1="2" x2="8" y2="6" />
            <line x1="3" y1="10" x2="21" y2="10" />
          </svg>
          Abril 2025
        </button>
        <button class="btn btn-primary btn-sm" @click="abrirPainel">
          <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="12" y1="5" x2="12" y2="19" />
            <line x1="5" y1="12" x2="19" y2="12" />
          </svg>
          Contrato
        </button>

      </div>
    </div>

    <div id="order-detail" class="detail-overlay" :class="{ open: isOpen }">
      <div class="detail-header">
        <button class="detail-close" @click="fecharPainel">✕</button>
        <strong>Criar Contrato</strong>
      </div>
      <div class="detail-body">
        <form action="">
          <label for="empresa">Empresa</label>
          <input type="text" name="empresa">

          <label for="dataInicio">Data Início</label>
          <input type="date" name="dataInicio">

          <label for="dataFim">Data Fim</label>
          <input type="date" name="dataFim">

          <label for="quantidadePlantas">Quantidade de plantas</label>
          <input type="number" name="quantidadePlantas">

          <label for="ativos">Quantidade de Ativos</label>
          <input type="number" name="ativos">

          <label for="supervisoes">Quantidade de Supervisões</label>
          <input type="number" name="supervisoes">

          <label for="descricao">Descrição</label>
          <textarea name="descricao"></textarea>

          <label for="criador">Criador</label>
          <input type="text" name="criador">
        </form>
      </div>
    </div>
  </div>
</template>

<style>
.main {
  margin-left: var(--sidebar-w);
  width: calc(100% - var(--sidebar-w));
  /* ESSENCIAL */
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: fixed;
  top: 0;
}

.topbar {
  height: var(--topbar-h);
  background: var(--white);
  border-bottom: 1px solid var(--gray-200);
  display: flex;
  align-items: center;
  padding: 0 24px;
  gap: 12px;
  flex-shrink: 0;

}

.topbar-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-900);
  flex: 1;
}

.topbar-sub {
  font-size: 12px;
  color: var(--gray-500);
  font-weight: 400;
  margin-left: 8px;
}

.topbar-actions {
  display: flex;
  align-items: center;
  gap: 10px;
}

.btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 7px 14px;
  border-radius: var(--radius-sm);
  border: none;
  font-family: var(--font);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.15s;
  white-space: nowrap;
}

.btn-primary {
  background: var(--blue-600);
  color: white;
  text-decoration: none;
}

.btn-primary:hover {
  background: var(--blue-700);
}

.btn-secondary {
  background: var(--white);
  color: var(--gray-700);
  border: 1px solid var(--gray-300);
}

.btn-secondary:hover {
  background: var(--gray-50);
}

.btn-ghost {
  background: transparent;
  color: var(--gray-500);
}

.btn-ghost:hover {
  background: var(--gray-100);
  color: var(--gray-700);
}

.btn-sm {
  padding: 5px 10px;
  font-size: 12px;
}

.btn-danger {
  background: var(--red);
  color: white;
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: var(--gray-50);
}

form {
  gap: 10px;
  display: flex;
  flex-direction: column;
}

input{
  padding: 15px;
  border: 1px solid  var(--gray-300);
  border-radius: 10px;
}

textarea{
  padding: 15px;
  border: 1px solid  var(--gray-300);
  border-radius: 10px;
}

.detail-overlay {
  position: fixed;
  top: 5px;
  right: 0;
  width: 420px;
  height: calc(100vh);
  background: var(--white);
  border-left: 1px solid var(--gray-200);
  box-shadow: -4px 0 24px rgba(0, 0, 0, 0.08);
  z-index: 100;
  display: none;
  overflow-y: auto;
}

.detail-overlay.open {
  display: block;
}

.detail-header {
  padding: 20px;
  border-bottom: 1px solid var(--gray-200);
  position: sticky;
  top: 0;
  background: var(--white);
  z-index: 1;
}

.detail-body {
  padding: 20px;
}

.detail-close {
  position: absolute;
  top: 16px;
  right: 16px;
  background: none;
  border: none;
  cursor: pointer;
  color: var(--gray-400);
  font-size: 18px;
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
}

.detail-close:hover {
  background: var(--gray-100);
  color: var(--gray-700);
}

.detail-field {
  margin-bottom: 16px;
}

.detail-field-label {
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  color: var(--gray-400);
  font-weight: 600;
  margin-bottom: 4px;
}

.detail-field-value {
  font-size: 13.5px;
  color: var(--gray-800);
}
</style>
