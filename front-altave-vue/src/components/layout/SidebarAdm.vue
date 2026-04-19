<template>
  <div id="screen-adm" class="screen active">
    <div class="app-shell">
      <!-- SIDEBAR -->
      <aside class="sidebar">
        <div class="sidebar-logo">
          <div class="logo-icon">
            <svg viewBox="0 0 24 24">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" />
            </svg>
          </div>
          <div>
            <div class="logo-text">Altave</div>
            <div class="logo-sub">Gestão</div>
          </div>
        </div>
        <div class="sidebar-section">
          <div class="sidebar-section-label">Menu</div>
          <RouterLink to="/adm/index" class="nav-item active">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="3" width="7" height="7" />
              <rect x="14" y="3" width="7" height="7" />
              <rect x="3" y="14" width="7" height="7" />
              <rect x="14" y="14" width="7" height="7" />
            </svg>
           Dashboard
          </RouterLink>
          <div class="nav-item">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M9 12h6M9 16h6M9 8h6M5 20h14a2 2 0 002-2V8l-6-6H5a2 2 0 00-2 2v14a2 2 0 002 2z" />
            </svg>
            Contratos
            <span class="nav-badge">8</span>
          </div>
          <div class="nav-item">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="5" r="3" />
              <path d="M12 8v4M8 12H4a8 8 0 0016 0h-4" />
            </svg>
            Ativos
            <span class="nav-badge">312</span>
          </div>
          <div class="nav-item">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2" />
              <circle cx="9" cy="7" r="4" />
              <path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75" />
            </svg>
            Usuários
            <span class="nav-badge">47</span>
          </div>
          <div class="nav-item">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M14 2H6a2 2 0 00-2 2v16a2 2 0 002 2h12a2 2 0 002-2V8z" />
              <polyline points="14,2 14,8 20,8" />
              <line x1="16" y1="13" x2="8" y2="13" />
              <line x1="16" y1="17" x2="8" y2="17" />
            </svg>
            Ordens
            <span class="nav-badge red">14</span>
          </div>
          <div class="nav-item">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <line x1="18" y1="20" x2="18" y2="10" />
              <line x1="12" y1="20" x2="12" y2="4" />
              <line x1="6" y1="20" x2="6" y2="14" />
            </svg>
            Relatórios
          </div>
          <div class="nav-item">
            <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="3" />
              <path d="M19.07 4.93a10 10 0 010 14.14M4.93 4.93a10 10 0 000 14.14" />
            </svg>
            Configurações
          </div>
        </div>
        <div class="sidebar-footer">
          <div class="user-card">
            <div class="avatar avatar-blue">RG</div>
            <div class="user-info">
              <div class="user-name">Rafael Gomes</div>
              <div class="user-role">Administrador</div>
            </div>
            <div class="status-dot status-online"></div>
          </div>
        </div>
      </aside>
    </div>
  </div>

</template>

<style>
 .sidebar {
    width: var(--sidebar-w);
    background: var(--blue-700);
    display: flex;
    flex-direction: column;
    flex-shrink: 0;
    overflow-y: auto;
    height: 100%;
    position: fixed;
    left: 0;
  }
  .sidebar-logo {
    padding: 20px 20px 16px;
    display: flex;
    align-items: center;
    gap: 10px;
    border-bottom: 1px solid rgba(255,255,255,0.1);
  }
  .logo-icon {
    width: 36px; height: 36px;
    background: var(--blue-400);
    border-radius: 8px;
    display: flex; align-items: center; justify-content: center;
  }
  .logo-icon svg { width: 20px; height: 20px; fill: white; }
  .logo-text { font-size: 15px; font-weight: 600; color: white; letter-spacing: 0.04em; }
  .logo-sub { font-size: 10px; color: rgba(255,255,255,0.45); letter-spacing: 0.06em; text-transform: uppercase; }

  .sidebar-section { padding: 12px 12px 4px; }
  .sidebar-section-label {
    font-size: 10px;
    text-transform: uppercase;
    letter-spacing: 0.08em;
    color: rgba(255,255,255,0.35);
    padding: 0 8px;
    margin-bottom: 4px;
  }
  .nav-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 9px 12px;
    border-radius: 8px;
    color: rgba(255,255,255,0.65);
    cursor: pointer;
    transition: all 0.15s;
    font-size: 13.5px;
    font-weight: 400;
    margin-bottom: 1px;
    text-decoration: none;
  }
  .nav-item:hover { background: rgba(255,255,255,0.08); color: white; }
  .nav-item.active { background: rgba(255,255,255,0.15); color: white; font-weight: 500;  }
  .nav-item .nav-icon { width: 16px; height: 16px; opacity: 0.8; flex-shrink: 0; }
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
  .nav-badge.red { background: var(--red); }

  .sidebar-footer {
    margin-top: auto;
    padding: 12px;
    border-top: 1px solid rgba(255,255,255,0.1);
  }

  .user-card {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 8px 10px;
    border-radius: 8px;
    cursor: pointer;
  }
  .user-card:hover { background: rgba(255,255,255,0.08); }
  .avatar {
    width: 32px; height: 32px;
    border-radius: 50%;
    display: flex; align-items: center; justify-content: center;
    font-size: 12px; font-weight: 600;
    flex-shrink: 0;
  }
  .avatar-blue { background: var(--blue-400); color: white; }
  .avatar-green { background: #16A34A; color: white; }
  .avatar-amber { background: #D97706; color: white; }
  .avatar-purple { background: #7C3AED; color: white; }
  .user-info { flex: 1; min-width: 0; }
  .user-name { font-size: 12.5px; font-weight: 500; color: white; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
  .user-role { font-size: 10.5px; color: rgba(255,255,255,0.4); }
  .status-dot {
    width: 7px; height: 7px;
    border-radius: 50%;
    flex-shrink: 0;
  }
  .status-online { background: #22C55E; }
  .status-busy { background: var(--amber); }
</style>
