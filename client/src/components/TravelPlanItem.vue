<template>
  <div id="travel-plan-item">
    <div id="item-info">
      <div id="flag-img"></div>
      <div id="item-detail">
        <p id="item-title">{{ planItem.title }}</p>
        <p id="item-date" class="text-sm">
          {{ planItem.fromDate }} - {{ planItem.endDate }}
        </p>
      </div>
    </div>

    <router-link to="/" id="edit-item">
      <font-awesome-icon icon="fa-solid fa-pen" id="edit-item-img" />
    </router-link>
  </div>
</template>

<style lang="scss"></style>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  planItem: Object,
});
</script>
