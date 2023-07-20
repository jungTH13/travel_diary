import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../../composable/api";
import { useTravelStore } from "../travel";
import { convertTimeFormat } from "../../composable/util";

export const useChecklistStore = defineStore("checklist", () => {
  const travelStore = useTravelStore()

  const checklistList = ref([]);
  const searchChecklistList = ref([]);
  const dailyChecklistList = ref([]); // [[checklist, ], ] [날짜별:[체크리스트, ], ]
  const preparationChecklistList = ref([]) // [checklist, ] [체크리스트, ]
  const checklist = ref({});

  const resetChecklist = ()=>{
    checklist.value = {}
  }
  
  async function getChecklistList(travelId) {
    const {data} = await API.post(`/travel/${travelId}/plan/checkList/checkListList`,{});

    data.results.planCheckListTitleList.forEach((checklist)=>{
        convertTimeFormat(checklist)
    })

    checklistList.value = data.results.planCheckListTitleList
    searchChecklistList.value = checklistList.value

    _setDailyChecklistList()
  }

  async function getChecklist(travelId,checklistId) {
    const {data} = await API.post(`/travel/${travelId}/plan/checkList/checkListOne`,{id:checklistId});

    convertTimeFormat(data.results.planCheckListTitle)
    return {
        ...data.results.planCheckListTitle,
        detailList:data.results.planCheckListDetailList
    }
  }

  function _setDailyChecklistList(){
    const travel = travelStore.travel
    const startDate = new Date(travel.startDate.split('T')[0])
    const dayList = travelStore.dayList
    
    const dailyList = {}
    for(const checklist of searchChecklistList.value||[]){
        //여행일자별로 정보 분류
        const date = checklist['requireDate'].split('T')[0]
        
        if(!dailyList[date])dailyList[date] = []
        dailyList[date].push(checklist)
    }

    //여행의 일자정보를 가지는 리스트의 위치와 맵핑하여 정보 정렬
    dailyChecklistList.value = dayList.map((date)=>{
      const nowDate = startDate.getDate()
      const now = startDate.toJSON().split('T')[0]
      if(date !== nowDate) return console.log('일정 정렬에 실패했습니다.')

      startDate.setDate(startDate.getDate()+1)
      if(dailyList[now]) return dailyList[now]
      else return []
    })
  }

  const _createForm = (target = checklist.value)=>{
    const form = {};

    Object.keys(target).forEach((key)=>form[key] = target[key]);

    if(form.detailList && form.detailList.length) {
        form.planCheckListDetail =  form.detailList.map((detail)=>detail)
    }
    else form.planCheckListDetail = []

    delete form.deleted
    delete form.createdDate
    delete form.modifiedDate
    delete form.detailList
    
    return form
  }

  async function postChecklist(travelId){
    const form = _createForm()

    const { data } = await API.post(`/travel/${travelId}/plan/checkList/checkListInsert`, form);
    
    return data
  }

  async function putChecklist(travelId,checklist){
    const form = _createForm(checklist)

    const { data } = await API.put(`/travel/${travelId}/plan/checkList/checkListUpdate`, form);
    
    return data
  }

  async function delChecklist(travelId,checklistId){
    const { data } = await API.delete(`/travel/${travelId}/plan/checkList/checkListDelete/${checklistId}`);
    
    return data
  }

  return {
    checklistList,
    searchChecklistList,
    dailyChecklistList,
    checklist,
    getChecklistList,
    getChecklist,
    postChecklist,
    putChecklist,
    delChecklist,
    resetChecklist,
  };
});
