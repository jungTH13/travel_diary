import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../../composable/api";

export const useScheduleStore = defineStore("schedule", () => {
  const scheduleList = ref([]);
  const searchscheduleList = ref([]);
  const dailyScheduleList = ref([]);
  

  async function getscheduleList(travelId,searchData) {
    if(true) {
      const {data} = await API.get(`/travel/${travelId}/plan/userPlaneList`);
      //시간 정보 전처리
      data.results.planList.forEach((plan)=>{
        for(const key of Object.keys(plan)){
            if(key.includes("Date") || key.includes("Time") || key.includes("date")){
              if(!plan[key]) continue  
              plan[key] = plan[key].split('.')[0]
            }
        }
      })
      
      scheduleList.value = data.results.planList;
    }

    if(searchData){
      searchscheduleList.value = scheduleList.value.filter((schedule)=>schedule.title.includes(searchData));
    }
    else{
      searchscheduleList.value = scheduleList.value;
    }

    _setDailyScheduleList()
  }

  

  function _setDailyScheduleList(){
    const dailyList = {}
    for(const schedule of searchscheduleList.value||[]){
        const date = schedule['orderDate'].split('T')[0]
        
        if(!dailyList[date])dailyList[date] = []
        dailyList[date].push(schedule)
    }
    dailyScheduleList.value = Object.keys(dailyList).map((key)=>dailyList[key])
  }


  function resetScheduleList (){
    scheduleList.value = []
    searchscheduleList.value = []
  }

  return {
    scheduleList,
    searchscheduleList,
    dailyScheduleList,
    getscheduleList,
    resetScheduleList,
  };
});
