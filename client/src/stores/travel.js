import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";
import { useCountryStore } from "./country";
import { convertTimeFormat } from "../composable/util";

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
    dayList.value = []
  }

  async function getTravelList() {
    const { data } = await API.get("/travel/userTravelList");

    for(const plan of data.results.planTravel) convertTimeFormat(plan)
    for(const plan of data.results.endTravel) convertTimeFormat(plan)

    travelList.plan = data.results.planTravel;
    travelList.end = data.results.endTravel;

    return data;
  }

  async function getTravel(travelId){

    const {data} = await API.post("/travel/userTravelOne",{id:travelId})
    
    const countryStore = useCountryStore()
    await countryStore.getCountryList()
    // countryList의 code 정보를 country 정보로 변경
    data.results.travelOne.countryList = data.results.travelCountryList.map((code)=>countryStore.countryList.filter((country)=>code===country.code.split('_')[1])[0])
    convertTimeFormat(data.results.travelOne)

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

  async function putTravel(){
    const form = {};

    Object.keys(travel.value).forEach((key)=>form[key] = travel.value[key]);

    form.country = form.countryList.map(country=>country.code);
    delete form.countryList

    const { data } = await API.put("/travel/travelUpdate", form);

    return data;
  }

  async function delTravel(){

    const { data } = await API.delete(`/travel/travelDelete/${travel.value.id}`);

    return data;
  }

  return {
    travelList,
    travel,
    dayList,
    getTravelList,
    postTravel,
    putTravel,
    delTravel,
    resetTravel,
    getTravel,
  };
});
