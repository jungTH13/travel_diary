import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../utils/api";

export const useCountryStore = defineStore("country", () => {
  const countryList = ref([]);
  const searchCountryList = ref([]);

  async function getCountryList(searchData) {
    if(countryList.value?.length === 0) {
      const {data} = await API.get("/common/countryList");
      countryList.value = data.results.countryList;
    }

    if(searchData){
      searchCountryList.value = countryList.value.filter((country)=>country.name.includes(searchData));
    }
    else{
      searchCountryList.value = countryList.value;
    }
  }

  return {
    countryList,
    searchCountryList,
    getCountryList,
  };
});
