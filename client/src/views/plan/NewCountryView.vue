<template>
  <div class="plan-container">
    <section>
      <SelectedCountries :countries="countries" />

      <!-- <p class="plan-info">2025년 3월 10일 ~ 2025년 4월 2일</p>
      <div class="plan-date">
        <img src="@/assets/icons/cal.png" alt="calender" />
        <input type="date" />
      </div> -->
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

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { usePlanStore } from "../../stores/plan";
import SelectedCountries from "../../components/SelectedCountries.vue";
import axios from "axios";

const router = useRouter();

const countries = ref([]);
const planDate = ref([]);
const planTitle = ref("");

const store = usePlanStore();

async function postPlan() {
  const startDate = planDate.value[0].$d;
  const endDate = planDate.value[1].$d;

  console.log(countries?.value);
  console.log(planDate.value);
  console.log(startDate, endDate);

  const response = await axios.post(
    "https://develop.life-traveldiary.net:8080/travel/travelInsert",
    {
      title: planTitle.value,
      startDate,
      endDate,
    },
    {
      withCredentials: true,
    }
  );

  console.log("post res", response);

  router.push("/");
}

onMounted(() => {
  countries.value = store.planCountries;

  const titleArr = countries.value.map((item) => {
    return item.name;
  });

  planTitle.value = titleArr.join(", ");
});
</script>

<style lang="scss" scoped></style>
