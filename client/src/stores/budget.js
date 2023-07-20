import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";
import { useTravelStore } from "./travel";
import {convertTimeFormat, toKoreaTimeString} from "../composable/util"

function defaultBudget (){
  return{
    memo: "",
    title: "",
    amountOfPayment: "",
    paymentDate: "",
    paymentType: "card",
    categoryType: "Food Expense",
    planType: null,
    planTypeId: null,
  }
}

export const useBudgetStore = defineStore("budget", () => {
  const travelStore = useTravelStore()

  const budgetList = ref([]);
  const searchbudgetList = ref([]);
  const dailyBudgetList = ref([]);
  const preparationBudgetList = ref([])
  const budget = ref(defaultBudget())
  
  function resetBudget(){
    budget.value = defaultBudget()
  }

  async function getbudgetList(travelId,searchData) {
    if(true) {
      const {data} = await API.post(`/travel/${travelId}/plan/accountBook/accountBookList`,{});
      //시간 정보 전처리
      data.results.accountBookList.forEach((plan)=>{
        for(const key of Object.keys(plan)){
            if(key.includes("Date") || key.includes("Time") || key.includes("date")){
              if(!plan[key]) continue  
              plan[key] = plan[key].split('.')[0]
            }
        }
      })
      
      budgetList.value = data.results.accountBookList;
    }

    if(searchData){
      searchbudgetList.value = budgetList.value.filter((budget)=>budget.title.includes(searchData));
    }
    else{
      searchbudgetList.value = budgetList.value;
    }

    _setDailyBudgetList()
  }

  async function getbudget(travelId,budgetId) {

    const {data} = await API.post(`/travel/${travelId}/plan/accountBook/accountBookOne`,{id:budgetId});
    
    convertTimeFormat(data.results.accountBook)
    if(data.results.accountBook.amountOfPayment)data.results.accountBook.amountOfPayment = data.results.accountBook.amountOfPayment.toString()
    budget.value = data.results.accountBook
    return data
  }

  function _setDailyBudgetList(){
    const travel = travelStore.travel
    const startDate = new Date(travel.startDate.split('T')[0])
    const dayList = travelStore.dayList
    
    const dailyList = {}
    const preparationList = []
    for(const budget of searchbudgetList.value||[]){
      //여행준비에 해당하는 정보를 따로 저장
      if(budget['paymentDate']===travel.startDate) {
        preparationList.push(budget)
        continue
      }
      
      //여행일자별로 정보 분류
      const date = budget['paymentDate'].split('T')[0]
        
      if(!dailyList[date])dailyList[date] = []
      dailyList[date].push(budget)
    }

    //여행의 일자정보를 가지는 리스트의 위치와 맵핑하여 정보 정렬
    preparationBudgetList.value = preparationList
    dailyBudgetList.value = dayList.map((date)=>{
      const nowDate = startDate.getDate()
      const now = startDate.toJSON().split('T')[0]
      if(date !== nowDate) return console.log('일정 정렬에 실패했습니다.')

      startDate.setDate(startDate.getDate()+1)
      if(dailyList[now]) return dailyList[now]
      else return []
    })

  }

  const _createForm = ()=>{
    const form = {};

    Object.keys(budget.value).forEach((key)=>form[key] = budget.value[key]);

    if(typeof form.amountOfPayment === 'string')form.amountOfPayment = parseFloat(form.amountOfPayment.replaceAll(',',''))
    delete form.deleted
    delete form.createdDate
    delete form.modifiedDate
    
    return form
  }

  const postBudget = async(travelId)=>{
    const form = _createForm()

    const { data } = await API.post(`/travel/${travelId}/plan/accountBook/accountBookInsert`, form);
    
    return data
  }

  const putBudget = async(travelId)=>{
    const form = _createForm()

    const { data } = await API.put(`/travel/${travelId}/plan/accountBook/accountBookUpdate`, form);
    
    return data
  }

  const delBudget = async(travelId)=>{
    const { data } = await API.delete(`/travel/${travelId}/plan/accountBook/accountBookDelete/${budget.value.id}`);
    
    return data
  }

  return { 
    budgetList,
    searchbudgetList,
    dailyBudgetList,
    preparationBudgetList,
    budget,
    getbudgetList,
    getbudget,
    resetBudget,
    postBudget,
    putBudget,
    delBudget
  };
});


