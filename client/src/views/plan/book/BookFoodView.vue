<template>
  <div>식당 예약</div>
</template>

<script setup>
import { ref, reactive, onBeforeMount, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";

const router = useRouter();
const route = useRoute();

onMounted(() => {
  console.log(route.path);
});
</script>
