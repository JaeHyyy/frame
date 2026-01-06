<script setup>
import { RouterView } from 'vue-router'
import { onMounted } from 'vue'

onMounted(() => {
  // 카카오 SDK 초기화 (한 번만 실행)
  if (window.Kakao && !window.Kakao.isInitialized()) {
    window.Kakao.init('2e74f9db664874295572697d3cbe951f') // saju JavaScript 키
  }
})
</script>

<template>
  <RouterView />
</template>

<style scoped>

</style>
