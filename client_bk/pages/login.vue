<template>
  <div>
    <div id="login-page">
      <h1 class="text-lg">로그인</h1>
      <div id="login-button" @click="handleLogin">
        <button>간편 로그인</button>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
#login-page {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;

  h1 {
    margin-top: 80px;
  }
  #login-button {
    margin-top: 40px;
    button {
      cursor: pointer;
    }
  }
}
</style>

<script setup>
import axios from "axios";
import { useRouter } from "vue-router";
const router = useRouter();

async function handleLogin() {
  const exam = await axios.get(
    "https://develop.life-traveldiary.net:8080/user/examCookie",
    {
      withCredentials: true,
    }
  );

  router.replace("/");

  console.log("user", exam);
}

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(async () => {
  console.log("Mounted!");
});
</script>
