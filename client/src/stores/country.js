import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";

export const useCountryStore = defineStore("country", () => {
  const countryList = ref([]);
  const searchCountryList = ref([]);

  async function getCountryList(searchData) {
    if(countryList.value?.length === 0) {
      const {data} = await API.get("/common/countryList");
      // 코드에 붙은 1_ 정보 제거
      // data.results.countryList.forEach((country)=>country.code = country.code.split('_')[1])

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
