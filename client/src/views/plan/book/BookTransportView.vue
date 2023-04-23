<template>
  <div>교통 예약</div>
</template>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useBookTransportStore } from "../../../stores/plan/book/transport";

const router = useRouter();
const route = useRoute();

const bookTransportStore = useBookTransportStore();

const transportList = computed(() => bookTransportStore.bookTransportList);

watch(
  () => transportList.value,
  () => {
    transportList.value.forEach((item) => {
      console.log("list", item);
    });
  }
);

onMounted(() => {
  console.log(route.params.id);
  const planId = route.params.id;
  bookTransportStore.getBookTransport(planId);
});
</script>
