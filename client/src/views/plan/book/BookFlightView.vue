<template>
  <div>
    <div>항공권 예약</div>
    <div v-for="item in flightList" :key="item.id">
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
import { useBookFlightStore } from "../../../stores/plan/book/flight";

const router = useRouter();
const route = useRoute();

const bookFlightStore = useBookFlightStore();

const flightList = computed(() => bookFlightStore.bookFlightList);

watch(
  () => flightList.value,
  () => {
    flightList.value.forEach((item) => {
      console.log("list", item);
    });
  }
);

onMounted(() => {
  console.log(route.params.id);
  const planId = route.params.id;
  bookFlightStore.getBookFlight(planId);
});
</script>
