<template>
  <div id="header">
    <a @click="toHome">TRAVEL</a>
  </div>
</template>

<style lang="scss" scoped>
#header {
  width: 100%;
  height: 44px;
  display: flex;
  justify-content: center;
  align-items: center;

  a {
    font-size: $text-xl;
    font-weight: bold;
    letter-spacing: 3px;
    color: $green;
  }
}
</style>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter()

const toHome = ()=>{
  router.push({path: "/"})
}


</script>
