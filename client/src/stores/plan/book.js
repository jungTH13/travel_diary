import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../../composable/api";

export const useBookStore = defineStore("book", () => {
  const book = ref({});

  const getBook = (travelId,planId,type) => {
    if(type === 'pa') return _getBookAirPlan(travelId,planId)
    if(type === 'ph') return _getBookHotel(travelId,planId)
    if(type === 'pr') return _getBookRestaurant(travelId,planId)
    if(type === 'pt') return _getBookTransPort(travelId,planId)
    if(type === 'pe') return _getBookEtc(travelId,planId)
  }

  const resetBook = ()=>{
    book.value= {}
  }

  const _getBookAirPlan = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/airPlane/airPlaneOne`, {id:planId});
    
    convertTime(data.results.planAirPlaneOne)
    book.value = data.results.planAirPlaneOne
    return data
  };

  const _getBookHotel = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/hotel/hotelOne`, {id:planId});

    convertTime(data.results.planHotelOne)
    book.value = data.results.planHotelOne
    return data
  };

  const _getBookRestaurant = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/restaurant/restaurantOne`, {id:planId});

    convertTime(data.results.planRestaurantOne)
    book.value = data.results.planRestaurantOne
    return data
  }

  const _getBookTransPort = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/transPort/transPortOne`, {id:planId});

    convertTime(data.results.planTransPortOne)
    book.value = data.results.planTransPortOne
    return data
  };

  const _getBookEtc = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/etc/etcOne`, {id:planId});
    
    convertTime(data.results.planEtcOne)
    book.value = data.results.planEtcOne
    return data
  };

  const convertTime = (obj)=>{
    //시간 정보 전처리
    for(const key of Object.keys(obj)){
        if(key.includes("Date") || key.includes("Time") ||key.includes("date")){
          if(!obj[key]) continue  
          obj[key] = obj[key].split('.')[0]
        }
    }
  }



  // const getPlanCountries = computed(() => planCountries.value);

  return { 
    book,
    getBook,
    resetBook
  };
});
