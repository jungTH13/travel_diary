import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../utils/api";

export const useTravelStore = defineStore("travel", () => {
  const travelList = reactive({
    plan: [],
    end: [],
  });

  const travelCountries = ref([]);
  const getTravelCountries = computed(() => travelCountries.value);

  function setTravelCountries(countries) {
    travelCountries.value = countries;
  }

  async function getCountrySearchResult(searchData) {
    const { data } = await API.post("/common/countryLike", searchData);
    return data;
  }

  async function getAllCountries() {
    const { data } = await API.get("/common/countryList");
    return data;
  }

  async function getTravelList() {
    const { data } = await API.get("/travel/userTravelList");

    travelList.plan = data.results.planTravel;
    travelList.end = data.results.endTravel;

    return data;
  }

  async function postTravel(travelData) {
    const { data } = await API.post("/travel/travelInsert", travelData);

    return data;
  }

  return {
    travelList,
    travelCountries,
    getTravelCountries,
    setTravelCountries,
    getAllCountries,
    getCountrySearchResult,
    getTravelList,
    postTravel,
  };
});
