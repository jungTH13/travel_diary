<template>
  <div class="plan-container">
    <section>
      <SelectedCountries :countries="travel.countryList" />
      <div class="plan-title">
        <input type="text" v-model="travel.title" />
      </div>
      <div class="plan-date">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="planDate" />
        </a-space>
      </div>
    </section>
    <div class="plan-footer">
      <!-- <button class="font-weight-600">수정</button>
      <button class="font-weight-600">삭제</button> -->
      <button class="font-weight-600" @click="postPlan">등록</button>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../../stores/travel";
import SelectedCountries from "../../components/SelectedCountries.vue";
import { useUtils } from "../../composable/useUtils";
import axios from "axios";

const router = useRouter();
const travelStore = useTravelStore();
const utils = useUtils();

// contents
const travel = computed(() => travelStore.travel);
const planDate = ref([]);

watch(
  () => planDate.value,
  () => {
    travel.value.startDate = utils
      .toKoreaTimeString(planDate.value[0].$d)
      .split("T")[0];
    travel.value.endDate = utils
      .toKoreaTimeString(planDate.value[1].$d)
      .split("T")[0];
  }
);

async function postPlan() {
  if (planDate.value.length <= 0) {
    return alert("날짜를 선택해주세요");
  }

  const data = await travelStore.postTravel();
  console.log("results", data.results);
  router.push({
    name: "plan",
    params: { id: data.results.travelId },
  });
}

onMounted(() => {
  if (travel.value.country.length <= 0) {
    alert("나라를 선택해주세요");
    return router.push({ name: "new-country" });
  }

  travel.value.title = travel.value.country
    .map((country) => country.name)
    .join(",");
});
</script>
