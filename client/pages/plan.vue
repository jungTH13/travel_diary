<template>
  <div class="plan-container">
    <section>
      <div class="plan-title">
        <span class="font-weight-600">캐나다</span>
        <img
          src="@/assets/icons/flight.png"
          alt="flight"
          style="width: 20px; height: 20px"
        />
        <span class="font-weight-600">미국</span>
      </div>
      <p class="plan-info">캐나다 & 미국 여행</p>
      <p class="plan-info">2025년 3월 10일 ~ 2025년 4월 2일</p>
      <div class="plan-date">
        <img src="@/assets/icons/cal.png" alt="calender" />
        <input type="date" />
      </div>
    </section>
    <div class="plan-footer">
      <button class="font-weight-600" @click="getRoute">수정</button>
      <button class="font-weight-600">삭제</button>
      <button class="font-weight-600">등록</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";
const router = useRouter();
const route = useRoute();

const codes = ref([]);
const countries = ref([]);

function getRoute() {
  console.log("route", route.query);

  codes.value = JSON.parse(route?.query?.codes);
  countries.value = JSON.parse(route?.query?.countries);

  console.log(codes.value);
  console.log(countries.value);
}

onMounted(() => {
  console.log(route);
  console.log("^mounted");
  getRoute();
});
</script>

<style lang="scss" scoped>
/* .plan-container{
  margin:60px 90px 0;

  .plan-title{
    font-size: 20px;
    line-height: 27px;
    padding-bottom: 13px;
    border-bottom: 1px solid #dddddd;
  }

  .plan-info{
    font-size:16px;
    line-height:22px;
    padding: 10px auto;
    border-bottom: 1px solid black;
  }
} */
</style>
