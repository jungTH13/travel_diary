import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../composable/useApi";

export const useCountryStore = defineStore("country", () => {
  const API = useApi();

  const countryList = ref([]);
  const searchCountryList = ref([]);

  async function getCountryList(searchData) {
    if (countryList.value?.length === 0) {
      const { data } = await API.get("/common/countryList");
      countryList.value = data.results.countryList;
    }

    if (searchData) {
      searchCountryList.value = countryList.value.filter((country) =>
        country.name.includes(searchData)
      );
      // const { data } = await API.post("/common/countryLike", searchData);
      // searchCountryList.value = data.results.countryList;
    } else {
      searchCountryList.value = countryList.value;
    }
  }

  return {
    countryList,
    searchCountryList,
    getCountryList,
  };
});
