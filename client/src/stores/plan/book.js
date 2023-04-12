import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../../utils/api";

export const useBookStore = defineStore("plan", () => {
  const bookFlightList = ref([]);

  const getBookFlight = async (planId) => {
    console.log("id", planId);

    const data = API.post(`/travel/${planId}/plan/airPlane/airPlaneList`, {});

    console.log("data", data);
  };

  // const getPlanCountries = computed(() => planCountries.value);

  return { getBookFlight };
});
