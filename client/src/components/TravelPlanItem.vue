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

<style lang="scss">
#travel-plan-item {
  cursor: pointer;
  box-shadow: 0px 0px 5px #444;
  border-radius: 8px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px 10px 10px;
  margin: 10px 0;

  &:hover{
    border: 1px solid $green;
  }

  #item-info {
    display: flex;
    align-items: center;

    #flag-img {
      width: 58px;
      height: 58px;
      background-color: #ddd;
      border-radius: 50%;
      margin-right: 20px;
    }

    #item-title {
      font-weight: bold;
      margin-bottom: 3px;
    }

    #edit-item {
      display: block;
      width: 28px;
      height: 28px;
      background: #fff;
      cursor: pointer;
      #edit-item-img {
        width: 100%;
        height: 100%;
      }
    }
  }
}
</style>

<script setup>
import { defineProps } from "vue";

const props = defineProps({
  planItem: Object,
});
</script>
