<template>
  <div class="login-page">
    <div class="login-box">
      <h1 class="title">사주</h1>
      <p class="desc">SNS 계정으로 간편 로그인</p>

      <button class="btn kakao" @click="loginWithKakao">
        카카오로 로그인
      </button>

      <button class="btn google">
        구글로 로그인
      </button>
    </div>
  </div>
</template>

<script setup>
const loginWithKakao = () => {
  if (!window.Kakao || !window.Kakao.isInitialized()) {
    alert('카카오 SDK가 초기화되지 않았습니다.')
    return
  }

  // 기존 카카오 세션 확인 및 로그아웃
  if (window.Kakao.Auth.getAccessToken()) {
    window.Kakao.Auth.logout(() => {
      // 로그아웃 후 로그인 진행
      performKakaoLogin()
    })
  } else {
    performKakaoLogin()
  }
}

const performKakaoLogin = () => {
  // prompt: 'login'을 사용하여 항상 로그인 화면 표시
  window.Kakao.Auth.authorize({
    redirectUri: 'http://localhost:5173/oauth/kakao/callback'
  })
}

// const loginWithGoogle = () => {
//   window.location.href = `${API_BASE_URL}/oauth/login/google`
// }
</script>

<style scoped>
.login-page {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f6f8;
}

.login-box {
  width: 320px;
  padding: 32px 24px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.08);
  text-align: center;
}

.title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 8px;
}

.desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
}

.btn {
  width: 100%;
  height: 44px;
  margin-bottom: 12px;
  border: none;
  border-radius: 6px;
  font-size: 15px;
  cursor: pointer;
}

.kakao {
  background: #fee500;
}

.google {
  background: #fff;
  border: 1px solid #ddd;
}
</style>
