import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const useTravelStore = defineStore("travel", () => {
  const travelList = reactive({
    plan: [],
    end: [],
  });

  const newTravel = ref({
    countryList: [], // country[]
    startDate: "", // YYYY-MM-DD
    endDate:"" // YYYY-MM-DD
  })

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

  const post = async()=>{
    const { data } = await axios.post(
      "https://develop.life-traveldiary.net:8080/travel/travelInsert",
      newTravel.value,
      {
        withCredentials: true,
      }
    );
 
    if(data.code === 200){
      return data
    }
  }

  return { 
    travelList,
    newTravel, 
    getTravelList, 
    post 
  };
});
