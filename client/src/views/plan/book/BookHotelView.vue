<template>
  <div>호텔 예약</div>
</template>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useBookHotelStore } from "../../../stores/plan/book/hotel";

const router = useRouter();
const route = useRoute();

const bookHotelStore = useBookHotelStore();

const hotelList = computed(() => bookHotelStore.bookHotelList);

watch(
  () => hotelList.value,
  () => {
    hotelList.value.forEach((item) => {
      console.log("list", item);
    });
  }
);

onMounted(() => {
  console.log(route.params.id);
  const planId = route.params.id;
  bookHotelStore.getBookHotel(planId);
});
</script>
