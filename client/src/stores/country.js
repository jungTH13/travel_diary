import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

export const useCountryStore = defineStore('country', () => {
  const countryList = ref([])
  const searchCountryList = ref([])
  
  const getCountryList = async()=>{
    const { data } = await axios.get(
        "https://develop.life-traveldiary.net:8080/common/countryList",
        {
            withCredentials: true,
        }
    );

    if(data.code === 200){
        countryList.value = data.results.countryList
    }
  }

  const getCountryListLike = async(searchData)=>{


    const { data } = await axios.post(
        "https://develop.life-traveldiary.net:8080/common/countryLike",
        {
            name:searchData
        },
        {
            withCredentials: true,
            headers: {
                "Content-Type": "application/json",
            },
        }
    );

    if(data.code === 200){
        searchCountryList.value = data.results.countryList
    }
  }

  return { countryList, searchCountryList, getCountryList,getCountryListLike }
})
