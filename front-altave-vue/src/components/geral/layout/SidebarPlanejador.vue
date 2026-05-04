<template>
    <aside class="plnj-sidebar" aria-label="Navegação do planejador">
      <div class="sidebar-logo">
        <div class="logo-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24">
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
          </svg>
        </div>
        <div>
          <div class="logo-text">Altave</div>
          <div class="logo-sub">SGM · Planejamento</div>
        </div>
      </div>
  
      <nav class="sidebar-section">
        <div class="sidebar-section-label">Menu</div>
  
        <RouterLink to="/planejador/inicio" class="nav-item" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z" />
            <polyline points="14,2 14,8 20,8" />
            <line x1="16" y1="13" x2="8" y2="13" />
            <line x1="16" y1="17" x2="8" y2="17" />
          </svg>
          Ordens
        </RouterLink>
  
        <span class="nav-item nav-item--disabled" title="Disponível após integração">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <circle cx="12" cy="5" r="3" />
            <path d="M12 8v4M8 12H4a8 8 0 0 0 16 0h-4" />
          </svg>
          Ativos
        </span>
  
        <span class="nav-item nav-item--disabled" title="Disponível após integração">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" />
            <circle cx="9" cy="7" r="4" />
          </svg>
          Minha Equipe
        </span>
  
        <span class="nav-item nav-item--disabled" title="Disponível após integração">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="4" width="18" height="18" rx="2" />
            <line x1="16" y1="2" x2="16" y2="6" />
            <line x1="8" y1="2" x2="8" y2="6" />
            <line x1="3" y1="10" x2="21" y2="10" />
          </svg>
          Calendário
        </span>
  
        <span class="nav-item nav-item--disabled" title="Disponível após integração">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="18" y1="20" x2="18" y2="10" />
            <line x1="12" y1="20" x2="12" y2="4" />
            <line x1="6" y1="20" x2="6" y2="14" />
          </svg>
          Relatórios
        </span>
      </nav>
  
      <div class="sidebar-footer">
        <div class="user-card">
          <div class="avatar avatar-blue" aria-hidden="true">PL</div>
          <div class="user-info">
            <div class="user-name">Usuário autenticado</div>
            <div class="user-role">Planejador</div>
          </div>
          <div class="status-dot" title="Status da sessão" />
        </div>
      </div>
    </aside>
  </template>
  
  <style scoped>
  .plnj-sidebar {
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
  
  .nav-item--disabled {
    opacity: 0.45;
    cursor: not-allowed;
  }
  
  .nav-icon {
    width: 16px;
    height: 16px;
    opacity: 0.8;
    flex-shrink: 0;
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
    .plnj-sidebar {
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