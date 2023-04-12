import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../utils/api";

export const useCountryStore = defineStore("travel", () => {
  const countryList = ref([]);
  const searchCountryList = ref([]);

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

  return {
    countryList,
    searchCountryList,
    getAllCountries,
    getCountryListLike,
  };
});
