<template>
  <div id="selected-country-wrapper">
    <div
      v-for="item in countries"
      v-bind:key="item.id"
      class="selected-country"
    >
      <span>{{ item.name }}</span>
      <font-awesome-icon icon="fa-solid fa-jet-fighter" id="plane-img" />
    </div>
  </div>
</template>

<style lang="scss"></style>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  countries: Array,
});
</script>
