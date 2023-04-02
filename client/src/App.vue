<template>
  <TheHeader />
  <RouterView />
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import TheHeader from "../src/components/TheHeader.vue";
import HomeView from "./views/HomeView.vue";
</script>

<style scoped></style>
