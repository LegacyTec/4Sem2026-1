import { createRouter, createWebHistory } from 'vue-router'
import MinhasOrdens from '@/views/tecnico/MinhasOrdens.vue'

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
    }
  ],
})

export default router
