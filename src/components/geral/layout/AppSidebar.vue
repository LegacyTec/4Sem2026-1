<template>
  <aside class="sidebar">
    <div class="sidebar-logo">
      <div class="logo-icon">
        <svg viewBox="0 0 24 24" fill="none" stroke="white" stroke-width="2">
          <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
        </svg>
      </div>
      <div>
        <div class="logo-text">Altave</div>
        <div class="logo-sub">Técnico</div>
      </div>
    </div>

    <div class="sidebar-section">
      <div class="sidebar-section-label">Menu</div>

      <div class="nav-item active">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="3" width="5" height="7"/><rect x="11" y="3" width="5" height="5"/>
          <rect x="3" y="13" width="5" height="8"/><rect x="11" y="11" width="5" height="10"/>
        </svg>
        Minhas Ordens
        <span class="nav-badge">6</span>
      </div>

      <div class="nav-item">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="12" cy="5" r="3"/>
          <path d="M12 8v4M8 12H4a8 8 0 0016 0h-4"/>
        </svg>
        Ativos
      </div>

      <div class="nav-item">
        <svg class="nav-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <rect x="3" y="4" width="18" height="18" rx="2"/>
          <line x1="16" y1="2" x2="16" y2="6"/>
          <line x1="8" y1="2" x2="8" y2="6"/>
          <line x1="3" y1="10" x2="21" y2="10"/>
        </svg>
        Histórico
      </div>
    </div>

    <div class="sidebar-footer">
      <div class="user-card">
        <div class="avatar avatar-green">CS</div>
        <div class="user-info">
          <div class="user-name">Carlos Santos</div>
          <div class="user-role">Técnico</div>
        </div>
        <div class="status-dot"></div>
      </div>
    </div>

  </aside>
</template>

<style scoped>
.sidebar {
  width: var(--sidebar-w);
  background: var(--blue-700);
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  overflow-y: auto;
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
  flex-shrink: 0;
}
.logo-icon svg { width: 20px; height: 20px; }
.logo-text { font-size: 15px; font-weight: 600; color: white; letter-spacing: 0.04em; }
.logo-sub  { font-size: 10px; color: rgba(255,255,255,0.45); letter-spacing: 0.06em; text-transform: uppercase; }

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
  margin-bottom: 1px;
}
.nav-item:hover  { background: rgba(255,255,255,0.08); color: white; }
.nav-item.active { background: rgba(255,255,255,0.15); color: white; font-weight: 500; }

.nav-icon { width: 16px; height: 16px; opacity: 0.8; flex-shrink: 0; }

.nav-badge {
  margin-left: auto;
  background: var(--blue-400);
  color: white;
  font-size: 10px;
  font-weight: 600;
  padding: 1px 6px;
  border-radius: 20px;
}

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
.user-info { flex: 1; min-width: 0; }
.user-name { font-size: 12.5px; font-weight: 500; color: white; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.user-role { font-size: 10.5px; color: rgba(255,255,255,0.4); }

.status-dot {
  width: 7px; height: 7px;
  border-radius: 50%;
  background: #22C55E;
  flex-shrink: 0;
}
</style>