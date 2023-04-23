<template>
  <div>
    <div>식당 예약</div>
    <div v-for="item in foodList" :key="item.id">
      <span>
        {{ item.title }}
      </span>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useBookFoodStore } from "../../../stores/plan/book/food";

const router = useRouter();
const route = useRoute();

const bookFoodStore = useBookFoodStore();

const foodList = computed(() => bookFoodStore.bookFoodList);

watch(
  () => foodList.value,
  () => {
    foodList.value.forEach((item) => {
      console.log("list", item);
    });
  }
);

onMounted(() => {
  console.log(route.params.id);
  const planId = route.params.id;
  bookFoodStore.getBookFood(planId);
});
</script>
