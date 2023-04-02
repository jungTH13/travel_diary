<template>
  <div>
    <TheHeader />

    <div id="main-page">

      <TheCreateTravel />

      <div id="travel-plan">
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
  </div>
</template>

<style lang="scss" scoped>
#main-page {
  width: 100%;
  padding: 40px 40px;

  #travel-plan {
    padding-top:40px;
  }

  h3 {
    font-weight: bold;
  }

  #plan-travel {
    margin-bottom: 80px;
  }
}
</style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted } from "vue";
import { useRouter } from "vue-router";
import TravelPlanItem from "../components/TravelPlanItem.vue";
import { useTravelStore } from "../stores/travel";
import TheHeader from "../components/TheHeader.vue";
import TheCreateTravel from "../components/TheCreateTravel.vue";

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
