import { createRouter, createWebHistory } from 'vue-router'
import MinhasOrdens from '@/views/tecnico/MinhasOrdens.vue'
import IndexAdm from '@/views/adm/IndexAdm.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/tecnico/ordens'
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
