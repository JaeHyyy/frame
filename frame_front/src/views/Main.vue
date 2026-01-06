<template>
  <div class="main-page">
    <div class="header">
      <h1 class="title">사주 보기</h1>
      <button class="logout-btn" @click="handleLogout">로그아웃</button>
    </div>
    
    <div class="content">
      <div class="welcome-box">
        <p class="welcome-text">환영합니다, {{ userNickname }}님!</p>
        <p class="sub-text">사주를 입력하고 운명을 확인해보세요.</p>
      </div>
      
      <div class="saju-form">
        <h2>사주 입력</h2>
        <p class="info-text">사주 기능은 준비 중입니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const userNickname = ref('사용자')

onMounted(() => {
  // 로그인 확인
  const token = localStorage.getItem('accessToken')
  if (!token) {
    router.push('/login')
    return
  }
  
  // 사용자 정보는 추후 API로 가져올 수 있음
  // const userInfo = await apiClient.get('/api/user/me')
  // userNickname.value = userInfo.data.nickname
})

const handleLogout = async () => {
  try {
    // 카카오 로그아웃 (토큰만 제거)
    if (window.Kakao?.Auth.getAccessToken()) {
      window.Kakao.Auth.logout(() => {
        console.log('카카오 로그아웃 완료')
      })
    }

  } catch (error) {
    console.error('카카오 로그아웃 처리 중 오류:', error)
  } finally {
    // 로컬 토큰 정리
    localStorage.removeItem('accessToken')

    // 로그인 페이지 이동
    router.push('/login')
  }
}
</script>

<style scoped>
.main-page {
  min-height: 100vh;
  background: #f5f6f8;
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto 30px;
  padding: 20px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.title {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  color: #333;
}

.logout-btn {
  padding: 10px 20px;
  background: #fee500;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  color: #000;
}

.logout-btn:hover {
  background: #fdd835;
}

.content {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-box {
  background: #fff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  margin-bottom: 30px;
  text-align: center;
}

.welcome-text {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.sub-text {
  font-size: 16px;
  color: #666;
}

.saju-form {
  background: #fff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.saju-form h2 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.info-text {
  color: #999;
  font-size: 14px;
}
</style>
