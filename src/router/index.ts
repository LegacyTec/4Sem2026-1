import { createRouter, createWebHistory } from 'vue-router'
import IndexAdm from '@/components/adm/IndexAdm.vue'
import Adm from '@/views/adm.vue'
import Planejador from '@/views/planejador.vue'
import Supervisor from '@/views/supervisor.vue'
import MinhasOrdens from '@/components/tecnico/MinhasOrdens.vue'
import Tecnico from '@/views/tecnico.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/adm/index'
    },
    {
      path: '/adm/inicio',
      name: 'adm-inicio',
      component: Adm
    },
    {
      path: '/supervisor/inicio',
      name: 'supervisor-inicio',
      component: Supervisor
    },
    {
      path: '/planejador/inicio',
      name: 'planejador-inicio',
      component: Planejador
    },
    {
      path: '/tecnico/inicio',
      name: 'tecnico-inicio',
      component: Tecnico
    },
    {
      path: '/tecnico/ordens',
      name: 'tecnico-ordens',
      component: MinhasOrdens
    },
    {
      path: '/adm/index',
      name: 'adm-index',
      component: IndexAdm
    }
  ],
})

export default router
