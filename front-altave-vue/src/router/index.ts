import { createRouter, createWebHistory } from "vue-router";
import Adm from "@/views/adm.vue";
import Planejador from "@/views/planejador.vue";
import Tecnico from "@/views/tecnico.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      redirect: "/adm/index",
    },
    {
      path: "/adm/index",
      name: "adm-index",
      component: Adm,
    },
    {
      path: "/planejador/inicio",
      name: "planejador-inicio",
      component: Planejador,
    },
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

export default router;
