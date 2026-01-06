import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/LoginView.vue'
import KakaoCallback from '../views/KakaoCallback.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
    },
    {
      path: '/main',
      name: 'main',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/Main.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/oauth/kakao/callback',
      component: KakaoCallback
    }
  ],
})

// 라우터 가드: 인증이 필요한 페이지 접근 시 체크
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('accessToken')
  
  if (to.meta.requiresAuth && !token) {
    // 인증이 필요한데 토큰이 없으면 로그인 페이지로
    next('/login')
  } else if (to.path === '/login' && token) {
    // 로그인 페이지에 토큰이 있으면 메인으로
    next('/main')
  } else {
    next()
  }
})

export default router
