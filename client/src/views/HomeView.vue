<template>
  <div>
    <div id="main-page">
      <router-link to="/travel/country" id="add-plan-button">
        <span class="plus-button orange"
          ><font-awesome-icon icon="fa-solid fa-plus" id="plus-button-img"
        /></span>
        <span class="text-lg">여행일정 만들기</span>
      </router-link>

      <div id="plan-travel">
        <h3>떠날 여행</h3>
        <div v-for="item in travelList.plan" v-bind:key="item.id">
          <TravelPlanItem :planItem="item" />
        </div>
      </div>
      <div id="end-travel">
        <h3>떠난 여행</h3>
        <div v-for="item in travelList.end" v-bind:key="item.id">
          <TravelPlanItem :planItem="item" />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted } from "vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import TravelPlanItem from "../components/TravelPlanItem.vue";
import { useTravelStore } from "../stores/travel";

const router = useRouter();
const store = useTravelStore();
const travelList = store.travelList;

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(() => {
  store.getTravelList();
});
</script>
