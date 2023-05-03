<template>
  <div class="full-hidden col">

    <div class="payment-state-container">
      <div class="payment-state-box">
        <span>잔액 </span>
        <span :class="{plus:amountOfPaymentState.account>=0,minus:amountOfPaymentState.account<0 }">{{ toComaNumberString(amountOfPaymentState.account) }}원</span>
      </div>
    </div>

    <div class="budget-container">

      <div class="budget-day-container">
        <h1 :class="{active: !dailybudgetVisibleList.length || !dailybudgetVisibleList.includes(false)}" @click="setAllDailyVisible">전체</h1>
        <div class="budget-date" :class="{active: dailybudgetVisibleList[index]}"  v-for="date,index in dayList" @click="setDailyVisible(index)">{{ date }}</div>
      </div>

      <div class="budget-contents-container">
        <div class="contents" v-for="day,index in dayList" v-show="dailybudgetVisibleList.length===0 || dailybudgetVisibleList[index]">
          <h1 class="date">{{(`DAY ${index}`) }} </h1> <p class="description"> {{ (index ? DateToStringFormat1(setDate(startDate,index-1)) : '여행준비')}}</p>
          <BudgetList v-model="dailyBudgetList[index]"  />
        </div>
      </div>

    </div>

    <div class="create-container">
      <div class="create-botton" @click="createBudget">
        <div class="text">
        <span class="payment">{{ `${ toComaNumberString(amountOfPaymentState.payment*-1)}원 지출` }}</span>
          <span>
            <div class="plus-gray"><font-awesome-icon icon="fa-solid fa-plus" id="plus-button-img" /></div>
          </span>
        </div>
      </div>
    </div>

  </div>
</template>

<style lang="scss" scoped>

.payment-state-container{
  position:relative;
  width: 100%;
  .payment-state-box{
    position:absolute;
    padding-right:1rem;
    padding-left: 1rem;
    z-index:1000;
    top:1rem;
    right:1rem;
    border-radius: 25px;
    background-color: $gray;

    span{
      font-size: 1.8rem;
      font-weight: 600;
      
      &.minus{
        color:red;
        text-shadow: 10px;
      }
      &.plus{
        color:blue;
        text-shadow: 10px;
      }
    }
  }
}

.budget-container{
  padding: 1rem 0vh 0vh 0vh;
  display: flex;
  flex-direction: row;
  height: 100%;
  overflow: hidden;
  .budget-day-container{
    max-width:100px;
    width: 5rem;
    height: 100%;
    overflow-y: auto;
    overflow-x: clip;
    padding:1rem;
    text-align: center;

    h1{
      cursor: pointer;
      font-size:2rem;
      margin-bottom: 3rem;
    }

    .budget-date{
      cursor: pointer;
      width: 3rem;
      height: 3rem;
      position:relative;
      padding-top: 0px;
      margin: auto;
      margin-bottom: 2rem;
      border: 0.25rem solid rgba(0, 0, 0, 0.404);
      border-radius: 50%;
      font-size:1.8rem;
    }

    h1.active{
      color: $orange;
    }
    div.active{
      border: 0.25rem solid $orange;
      color: $orange;
    }
  }

  .budget-contents-container{
    width: 100%;
    height: 100%;
    overflow: auto;
    margin:0rem;

    .contents{
      padding:2%;
      padding-right:0px;
      padding-top:0px;

      .date{
        margin-left:4%;
        margin-right:1%;
      }
      h1{
        font-size:2.3rem;
        display: inline;
      }
      p{
        font-size: 1.3rem;
        display: inline;
      }
    }

  }
}

.create-container{
  position:relative;
  width: 100%;
  bottom:0rem;
  left: 0px;
  margin: auto;
  text-align: center;
  display: flex;
  justify-content: center;
  

  .create-botton{
    height:4rem;
    width:100%;
    background-color: $green;
    position:relative;
    bottom:0rem;
    cursor: pointer;
    display: flex;
    flex-direction: row;
    margin:auto;
    border-radius: 5px;

    .text{
      position: relative;
      display: flex;
      flex-direction: row;
      bottom:0rem;
      margin:auto;

      .payment{
        font-size:1.8rem;
        font-weight: 600;
        margin-right: 1rem;
      }
    }
  }
}
.plus-gray{
  border-radius: 50%;
  background-color: rgba(177, 177, 177, 0.521);
  width:2.5rem;
  height:2.5rem;
  overflow: hidden;
  display: flex;
  box-shadow: 1px 1px 1px 1px gray;

  #plus-button-img{
    width:2rem;
    height:2rem;
    margin:auto;
    color:white;
    
  }
}


.description{
  color:gray;
}

</style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useBudgetStore } from "../../../stores/budget";
import { useTravelStore } from "../../../stores/travel";
import { DateToStringFormat1,toComaNumberString } from "../../../composable/util";
import PlanList from "../../../components/PlanList.vue";
import BudgetList from "../../../components/BudgetList.vue";

const route = useRoute()
const router = useRouter();
const budgetStore = useBudgetStore();
const travelStore = useTravelStore();


//contents
const budgetlList = computed(()=>budgetStore.searchbudgetList);
const dailybudgetVisibleList = ref([])
const dailyBudgetList = computed(()=>[budgetStore.preparationBudgetList,...budgetStore.dailyBudgetList]||[])
const startDate  = computed(()=>new Date(travelStore.travel.startDate.split('T')[0]))
const travelId = computed(()=>route.params.id)
const dayList = computed(()=>[0,...travelStore.dayList])
const amountOfPaymentState = computed(()=>{
  let payment = 0
  let account = 0
  for(let index=0;index<dailyBudgetList.value.length;index++){
    if(dailybudgetVisibleList.value.length && !dailybudgetVisibleList.value[index]) continue

    for(const budget of dailyBudgetList.value[index]){
      if(budget['amountOfPayment']<0)payment +=budget['amountOfPayment']
      account +=budget['amountOfPayment']
    }
  }
  return {payment,account}
})


const setDailyVisible = (dailybudgetIndex)=>{
  if(dailybudgetVisibleList[dailybudgetIndex]==undefined) {
    while(dailybudgetVisibleList.value.length < dayList.value.length)dailybudgetVisibleList.value.push(false)
  }

  dailybudgetVisibleList.value[dailybudgetIndex] = !dailybudgetVisibleList.value[dailybudgetIndex]
  
  for(const state of dailybudgetVisibleList.value){
    if(state == true) return
  }
  dailybudgetVisibleList.value = []
}

const setAllDailyVisible = ()=>{
  dailybudgetVisibleList.value = []
}

const setDate = (date,index)=>{
  const newDate = new Date(date)
  newDate.setDate(date.getDate()+index)
  
  return newDate
}

const createBudget = ()=>{
  router.push({
    name:'budget-detail',
    params:{
      id:travelId.value,
      budgetId:'register'
    }
  })
}

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(() => {
  budgetStore.getbudgetList(travelId.value)
});
</script>
