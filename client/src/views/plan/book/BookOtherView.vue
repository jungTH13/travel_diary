<template>
  <div>기타 예약</div>
</template>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useBookOtherStore } from "../../../stores/plan/book/other";

const router = useRouter();
const route = useRoute();

const bookOtherStore = useBookOtherStore();

const otherList = computed(() => bookOtherStore.bookOtherList);

watch(
  () => otherList.value,
  () => {
    otherList.value.forEach((item) => {
      console.log("list", item);
    });
  }
);

onMounted(() => {
  console.log(route.params.id);
  const planId = route.params.id;
  bookOtherStore.getBookOther(planId);
});
</script>
