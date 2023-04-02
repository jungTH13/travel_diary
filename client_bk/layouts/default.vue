<template>
  <div>
    <the-header></the-header>
    <slot />
  </div>
</template>

<script>
import TheHeader from "../components/TheHeader.vue";

export default {
  components: { TheHeader },
};
</script>
