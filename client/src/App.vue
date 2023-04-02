<template>
  <div style="width:50%; min-width:800px; transform: translate(50%, 0%);">
    <RouterView />
  </div>
</template>

<script setup>
import { RouterView } from "vue-router";
</script>

<style lang="scss">
@import "./assets/scss/app.scss";
</style>
