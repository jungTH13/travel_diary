<template>
  <div>항공권 예약</div>
</template>

<script setup>
import { ref, reactive, onBeforeMount, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useBookStore } from "../../../stores/plan/book";

const router = useRouter();
const route = useRoute();
const bookStore = useBookStore();

onMounted(() => {
  console.log(route.params.id);
  const planId = route.params.id;
  bookStore.getBookFlight(planId);
});
</script>
