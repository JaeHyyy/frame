<template>
  <div>카카오 로그인 처리중...</div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import apiClient from '../api/axios'

const route = useRoute()
const router = useRouter()

onMounted(async () => {
  const code = route.query.code

  if (!code) {
    alert('카카오 로그인 실패')
    router.push('/')
    return
  }

  try {
    // 백엔드로 인가 코드 전달
    const res = await apiClient.post('/api/auth/kakao', {
      code
    })

    // JWT 저장
    localStorage.setItem('accessToken', res.data.accessToken)

    // 메인 페이지 이동
    router.push('/main')
  } catch (error) {
    console.error('로그인 실패:', error)
    alert('로그인에 실패했습니다. 다시 시도해주세요.')
    router.push('/')
  }
})
</script>
