<template>
  <div class="plan-container">
    <section>
      <SelectedCountries :countries="countries" />
      <div class="plan-title">
        <input
          type="text"
          :value="planTitle"
          @input="(e) => (planTitle = e.target.value)"
        />
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
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../../stores/travel";
import SelectedCountries from "../../components/SelectedCountries.vue";
import axios from "axios";

const router = useRouter();

const countries = ref([]);
const planDate = ref([]);
const planTitle = ref("");

const store = useTravelStore();

async function postPlan() {
  if (planDate.value.length <= 0) {
    return alert("날짜를 선택해주세요");
  }

  const startDate = planDate.value[0].$d;
  const endDate = planDate.value[1].$d;
  countries.value = countries?.value?.map((country) => country.code);

  // console.log(countries?.value);
  // console.log(planDate.value);
  // console.log(startDate, endDate);

  const travelData = {
    title: planTitle.value,
    startDate,
    endDate,
    country: countries.value,
  };

  const data = await store.postTravel(travelData);
  console.log(data.results.travelId);
  router.push(`/plan/${data.results.travelId}`);
}

onMounted(() => {
  countries.value = store.travelCountries;

  if (countries.value.length <= 0) {
    alert("나라를 선택해주세요");
    return router.push("/travel/country");
  }

  const titleArr = countries.value.map((item) => {
    return item.name;
  });

  planTitle.value = titleArr.join(", ");
});
</script>
