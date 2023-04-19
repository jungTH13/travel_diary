import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../composable/useApi";

export const useCountryStore = defineStore("country", () => {
  const API = useApi();

  const countryList = ref([]);
  const searchCountryList = ref([]);

  // const travelCountries = ref([]);
  // const getTravelCountries = computed(() => travelCountries.value);

  async function getCountryList(searchData) {
    if (countryList.value?.length === 0) {
      const { data } = await API.get("/common/countryList");
      countryList.value = data.results.countryList;
    }

    if (searchData) {
      const { data } = await API.post("/common/countryLike", searchData);
      searchCountryList.value = data.results.countryList;
    } else {
      console.log("countryList", countryList.value);
      searchCountryList.value = countryList.value;
    }
  }

  return {
    countryList,
    searchCountryList,
    getCountryList,
  };
});
