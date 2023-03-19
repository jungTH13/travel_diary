<template>
  <div>
    <div id="main-page">
      <nuxt-link to="/search" id="add-plan-button">
        <span class="plus-button orange"
          ><font-awesome-icon icon="fa-solid fa-plus" id="plus-button-img"
        /></span>
        <span class="text-lg">여행일정 만들기</span>
      </nuxt-link>

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

<script>
import axios from "axios";
import { ref, reactive } from "vue";
import TravelPlanItem from "../components/TravelPlanItem.vue";

export default {
  components: {
    TravelPlanItem,
  },
  setup() {
    const travelList = reactive({
      plan: [],
      end: [],
    });
    onBeforeMount(() => {
      console.log("Before Mount!");
    });
    onMounted(async () => {
      await axios.get(
        "https://develop.life-traveldiary.net:8080/user/examCookie",
        { withCredentials: true }
      );

      const { data } = await axios.get(
        "https://develop.life-traveldiary.net:8080/travel/userTravelList",
        { withCredentials: true }
      );

      console.log("Mounted!", data.results);

      travelList.plan = data.results.planTravel;
      travelList.end = data.results.endTravel;
    });

    return { travelList };
  },
};
</script>
