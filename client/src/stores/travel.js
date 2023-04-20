import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";

const defaultTravel = ()=> ({
  title:"",
  countryList: [],
  startDate:'',
  endDate:'',
});

export const useTravelStore = defineStore("travel", () => {
  const travelList = reactive({
    plan: [],
    end: [],
  });
  const travel = ref(defaultTravel()); 
  const dayList = ref([]);

  //methods
  function resetTravel(){
    travel.value = defaultTravel()
  }

  async function getTravelList() {
    const { data } = await API.get("/travel/userTravelList");

    travelList.plan = data.results.planTravel;
    travelList.end = data.results.endTravel;

    return data;
  }

  async function getTravel(travelId){

    const {data} = await API.post("/travel/userTravelOne",{id:travelId})

    travel.value = data.results.travelOne

    _setDayList()

    return data
  }

  function _setDayList(){
    const startDate  = new Date(travel.value.startDate.split('T')[0])
    const endDate = new  Date(travel.value.endDate.split('T')[0])
    const diff = (endDate - startDate) /(1000*60*60*24)
    
    const newDayList = [];

    for(let i=0;i<diff + 1;i++){
        const newDate = new Date(startDate)
        newDate.setDate(startDate.getDate()+i)
        newDayList.push(newDate.getDate())
    }

    dayList.value = newDayList
  }

  async function postTravel() {

    const form = {};

    Object.keys(travel.value).forEach((key)=>form[key] = travel.value[key]);

    form.country = form.countryList.map(country=>country.code);
    delete form.countryList

    const { data } = await API.post("/travel/travelInsert", form);

    return data;
  }

  return {
    travelList,
    travel,
    dayList,
    getTravelList,
    postTravel,
    resetTravel,
    getTravel,
  };
});
