import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const useTravelStore = defineStore("travel", () => {
  const travelList = reactive({
    plan: [],
    end: [],
  });

  async function getTravelList() {
    const { data } = await axios.get(
      "https://develop.life-traveldiary.net:8080/travel/userTravelList",
      {
        withCredentials: true,
      }
    );

    if (data.code === 401) {
      router.push("/login");
    }

    travelList.plan = data.results.planTravel;
    travelList.end = data.results.endTravel;
  }

  return { travelList, getTravelList };
});
