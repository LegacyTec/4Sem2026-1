<template>
    <aside class="adm-sidebar" aria-label="Navegação do administrador">
      <div class="sidebar-logo">
        <div class="logo-icon" aria-hidden="true">
          <svg viewBox="0 0 24 24">
            <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
          </svg>
        </div>
        <div>
          <div class="logo-text">Altave</div>
          <div class="logo-sub">Gestão</div>
        </div>
      </div>
  
      <nav class="sidebar-section">
        <div class="sidebar-section-label">Menu</div>
  
        <RouterLink to="/adm/inicio" class="nav-item" active-class="active">
          <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <rect x="3" y="3" width="7" height="7" />
            <rect x="14" y="3" width="7" height="7" />
            <rect x="3" y="14" width="7" height="7" />
            <rect x="14" y="14" width="7" height="7" />
          </svg>
          Dashboard
        </RouterLink>
      </nav>
  
      <div class="sidebar-footer">
        <div class="user-card">
          <div class="avatar avatar-blue" aria-hidden="true">AD</div>
          <div class="user-info">
            <div class="user-name">Usuário autenticado</div>
            <div class="user-role">Administrador</div>
          </div>
          <div class="status-dot" title="Status da sessão" />
        </div>
      </div>
    </aside>
  </template>
  
  <style scoped>
  .adm-sidebar {
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
    font-size: 10px;
    color: rgba(255, 255, 255, 0.45);
    letter-spacing: 0.06em;
    text-transform: uppercase;
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
  
  .nav-icon {
    width: 16px;
    height: 16px;
    opacity: 0.8;
    flex-shrink: 0;
  }
  
  .nav-badge {
    margin-left: auto;
    background: var(--blue-400);
    color: white;
    font-size: 10px;
    font-weight: 600;
    padding: 1px 6px;
    border-radius: 20px;
    min-width: 18px;
    text-align: center;
  }
  
  .nav-badge.danger {
    background: var(--red);
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
    .adm-sidebar {
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