import { createRouter, createWebHistory } from "vue-router";
import Adm from "@/views/adm.vue";
import Planejador from "@/views/planejador.vue";
import Tecnico from "@/views/tecnico.vue";
import Supervisor from "@/views/supervisor.vue";
import Login from "@/views/login.vue";
import { getCurrentUser, rolePrefix, roleHome } from "@/services/AuthService";

import AdmContratoDetalhe from "@/components/adm/AdmContratoDetalhe.vue";
import AdmContratosPage from "@/components/adm/AdmContratosPage.vue";
import AdmUsuariosPage from "@/components/adm/AdmUsuariosPage.vue";
import AdmAtivosPage from "@/components/adm/AdmAtivosPage.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/adm/index",
    },
    {
      path: "/login",
      name: "login",
      component: Login,
    },
    // ADM
    {
      path: "/adm/index",
      name: "adm-dashboard",
      component: Adm,
    },

    {
      path: "/adm/contratos",
      name: "adm-contratos",
      component: AdmContratosPage,
    },
    {
      path: "/adm/contratos/:id",
      name: "adm-contrato-detalhe",
      component: AdmContratoDetalhe,
    },
    {
      path: "/adm/usuarios",
      name: "adm-usuarios",
      component: AdmUsuariosPage,
    },
    {
      path: "/adm/ativos",
      name: "adm-ativos",
      component: AdmAtivosPage,
    },
    // Supervisor
    {
      path: "/supervisor/inicio",
      name: "supervisor-inicio",
      component: Supervisor,
    },
    // Planejador
    {
      path: "/planejador/inicio",
      name: "planejador-inicio",
      component: Planejador,
    },
    // Técnico
    {
      path: "/tecnico/inicio",
      name: "tecnico-inicio",
      component: Tecnico,
    },
    {
      path: "/tecnico/ordens",
      redirect: "/tecnico/inicio",
    },
  ],
});

// Guard de autenticação e autorização por cargo
router.beforeEach((to) => {
  if (to.path === "/login") return true;

  const user = getCurrentUser();
  if (!user) return "/login";

  const prefixo = rolePrefix(user.cargo);
  // Se a rota pertence a um perfil que não é o do usuário, redireciona para a home dele
  const perfisProtegidos = ["/adm", "/supervisor", "/planejador", "/tecnico"];
  const ehRotaDeOutroPerfil = perfisProtegidos.some((p) => to.path.startsWith(p) && p !== prefixo);
  if (ehRotaDeOutroPerfil) return roleHome(user.cargo);

  return true;
});

export default router;
