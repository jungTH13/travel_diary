import { ref, computed, reactive, watch } from "vue";
import { defineStore } from "pinia";
import * as API from "../../composable/api";
import { useTravelStore } from "../travel";
import { convertTimeFormat } from "../../composable/util";

export const useScheduleStore = defineStore("schedule", () => {
  const travelStore = useTravelStore()

  const scheduleList = ref([]);
  const searchscheduleList = ref([]);
  const dailyScheduleList = ref([]);

  const imageGoupList = ref([])
  const dailyImageGroupList = ref([])
  

  async function getscheduleList(travelId,searchData,isImageGorupList=false) {
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
      if(isImageGorupList) await _getImageGroupList(travelId)
      
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

  async function _getImageGroupList(travelId){
    const {data} = await API.post(`/travel/${travelId}/plan/imageGroup/imageGroupList`,{});

    data.results.planImageGroupList.forEach((imageGroup)=>{
      imageGroup.type = 'pig'
      convertTimeFormat(imageGroup)
    })

    imageGoupList.value = data.results.planImageGroupList
    
    _setDailyImageGroupList()
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
      else return []
    })
  }

  function _setDailyImageGroupList(){
    const travel = travelStore.travel
    const startDate = new Date(travel.startDate.split('T')[0])
    const dayList = travelStore.dayList
    
    const dailyList = {}
    for(const imageGroup of imageGoupList.value||[]){
        const date = imageGroup['date'].split('T')[0]
        
        if(!dailyList[date])dailyList[date] = []
        dailyList[date].push(imageGroup)
    }

    dailyImageGroupList.value = dayList.map((date)=>{
      const nowDate = startDate.getDate()
      const now = startDate.toJSON().split('T')[0]
      if(date !== nowDate) return console.log('일정 정렬에 실패했습니다.')

      startDate.setDate(startDate.getDate()+1)
      if(dailyList[now]) return dailyList[now]
      else return []
    })
  }


  function resetScheduleList (isImageGorupList=false){
    scheduleList.value = []
    searchscheduleList.value = []
    dailyScheduleList.value = []
    if(isImageGorupList){
      imageGoupList.value = []
      dailyImageGroupList.value = []
    }
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
    imageGoupList,
    dailyImageGroupList,
    getscheduleList,
    resetScheduleList,
    putPlanMemoOnly
  };
});
