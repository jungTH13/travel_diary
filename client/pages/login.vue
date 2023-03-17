<template>
  <div>
    <div id="login-page">
      <h1 class="text-lg">로그인</h1>
      <div id="login-button" @click="handleLogin">간편 로그인</div>
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
    background-color: beige;
    cursor: pointer;
  }
}
</style>

<script>
export default {
  name: "login",
  methods: {
    handleLogin() {
      console.log("login");
    },
  },
};
</script>
