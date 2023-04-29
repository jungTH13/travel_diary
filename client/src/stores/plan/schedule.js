import { ref, computed, reactive, watch } from "vue";
import { defineStore } from "pinia";
import * as API from "../../composable/api";
import { useTravelStore } from "../travel";

export const useScheduleStore = defineStore("schedule", () => {
  const travelStore = useTravelStore()

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
    const travel = travelStore.travel
    const startDate = new Date(travel.startDate.split('T')[0])
    const dayList = travelStore.dayList
    
    const dailyList = {}
    for(const schedule of searchscheduleList.value||[]){
        const date = schedule['orderDate'].split('T')[0]
        
        if(!dailyList[date])dailyList[date] = []
        dailyList[date].push(schedule)
    }

    dailyScheduleList.value = dayList.map((date)=>{
      const nowDate = startDate.getDate()
      const now = startDate.toJSON().split('T')[0]
      if(date !== nowDate) return console.log('일정 정렬에 실패했습니다.')

      startDate.setDate(startDate.getDate()+1)
      if(dailyList[now]) return dailyList[now]
      else []
    })
  }


  function resetScheduleList (){
    scheduleList.value = []
    searchscheduleList.value = []
  }

  function _createForm (plan){
    const form = {};

    form['memo'] = plan['memo']
    form['planType'] = plan['type']
    form['planId'] = plan['id']
    
    return form
  }

  async function putPlanMemoOnly (travelId,plan){
    const form = _createForm(plan)

    const { data } = await API.put(`/travel/${travelId}/plan/memoUpdate`, form);
    
    return data
  }

  return {
    scheduleList,
    searchscheduleList,
    dailyScheduleList,
    getscheduleList,
    resetScheduleList,
    putPlanMemoOnly
  };
});
