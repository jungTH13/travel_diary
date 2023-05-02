<template>
  <div id="budget-container" class="full-hidden">
    <div id="budget-contents-container" class="full-hidden col">
      <v-container id="category" class="pa-0">
        <v-slide-group id="category-list" show-arrows>
          <div v-for="(cate, index) in categories" :key="index">
            <input
              type="radio"
              :id="cate"
              name="category"
              :value="cate"
              v-model="budget.categoryType"
            />
            <label :for="cate">{{ cate }}</label>
          </div>
        </v-slide-group>
      </v-container>

      <div id="budget-title">
        <div>
          <font-awesome-icon icon="fa-solid fa-tag" class="icon"/>
        </div>
        <input
          type="text"
          placeholder="내용을 입력하세요"
          v-model="budget.title"
        />
      </div>

      <div id="plan-dates">

        <div id="date-title">
          <div>
            <font-awesome-icon icon="fa-regular fa-calendar-check" class="icon" />
          </div>
          <h3>날짜 선택</h3>
        </div>

        <div id="plan-budget-date-picker">
          <DateTime v-model="budget.paymentDate" placeholder="날짜를 선택해주세요" />
        </div>

        <v-container id="select-date" class="pa-0">
          <v-slide-group show-arrows>
            
            <div class="item">
              <input
                type="radio"
                id="pre"
                name="dates"
                :value="preparationDateTime"
                v-model="budget.paymentDate"
              />
              <label for="pre">여행 준비</label>
            </div>

            <div class="item">
              <input
                type="radio"
                id="now"
                name="dates"
                :value="nowDateTime"
                v-model="budget.paymentDate"
              />
              <label for="now"> 현재 </label>
            </div>

          </v-slide-group>
        </v-container>
      </div>

      <div id="payment-method">
        <div id="title">
          <div>
            <font-awesome-icon icon="fa-solid fa-wallet" style="color: #000000" class="icon" />
          </div>
          <h3>결제 수단</h3>
        </div>
        <div id="select-date">
          <div>
            <input
              type="radio"
              id="card"
              name="payment"
              value="card"
              v-model="budget.paymentType"
            />
            <label for="card">카드</label>
          </div>
          <div>
            <input
              type="radio"
              id="cash"
              name="payment"
              value="cash"
              v-model="budget.paymentType"
            />
            <label for="cash">현금</label>
          </div>
        </div>
      </div>

      <div id="amount">
        <div>
          <font-awesome-icon icon="fa-solid fa-won-sign" class="icon" />
          <h3>결제 금액</h3>
        </div>
        <input
          type="text"
          placeholder="금액 입력"
          v-model="budget.amountOfPayment"
        />

        <div id="amount-type">
          <div>
            <input
              type="radio"
              id="amount-type-minus"
              name="payment-type-value"
              value="-"
              v-model="paymentTypeValue"
            />
            <label for="amount-type-minus">지출</label>
          </div>
          <div>
            <input
              type="radio"
              id="amount-type-plus"
              name="payment-type-value"
              value=""
              v-model="paymentTypeValue"
            />
            <label for="amount-type-plus">수입</label>
          </div>
        </div>

      </div>

      <div id="plan-select">
        <div>
          <font-awesome-icon icon="fa-solid fa-hand-pointer" class="icon" />
          <h3>일정과 연결하기</h3>
        </div>

        <select id="plan-select-box" v-model="selectPlan" >
          <option :value="{}">연결하고 싶은 일정과 선택하세요</option>
          <option v-for="plan of scheduleList" :value="plan">{{ plan.title }} | {{ plan.name }}</option>
        </select>
      </div>

      <div id="budget-memo">
        <div>
          <h3>메모</h3>
        </div>
        <div>
          <textarea placeholder="메모" v-model="budget.memo" style="border:0px;"></textarea>
        </div>
      </div>

    </div>


    
    <div class="summit-footer">
      <button v-if="budget.id" class="font-weight-600" @click="delBudget">삭제</button>
      <button v-if="budget.id" class="font-weight-600" @click="putBudget">수정</button>
      <button v-else class="font-weight-600" @click="postBudget">등록</button>
    </div>
    <!-- <button class="submit-button" @click="handleBudget">저장</button> -->
  </div>
</template>

<style lang="scss" scoped>
.icon{
  width: 2.5rem;
  height: 2.5rem;
}

#budget-container {
  padding-top: 3rem;
  display: flex;
  flex-direction: column;

  #budget-contents-container{
    overflow-y: auto;
    overflow-x: hidden;
  }

  #budget-title {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    margin-top: 3rem;
    input {
      font-size:1.8rem;
      margin-left: 1rem;
      width: 50%;
      height: 3rem;
      outline: none;
      border: none;
      border-bottom: 1px solid #ddd;
    }
  }

  #plan-dates,
  #payment-method {
    margin-top: 3rem;
    font-size: 1.8rem;
    width: 100%;

    #plan-budget-date-picker{
      margin-left:1rem;
    }

    #select-date{
      margin-left:1rem;
    }

    > div {
      display: flex;
      flex-wrap: wrap;
      margin-top: 1rem;
      width: 100%;

      h3 {
        font-weight: bold;
      }

      div,.item {
        margin-right:1rem;
        input {
          display: none;
        }
        label {
          margin-right: 1.8rem;
          color: #aaa;
          cursor: pointer;
        }
        input:checked ~ label {
          color: #74b83e;
          font-weight: bold;
        }
      }
    }
  }

  #amount {
    margin-top: 28px;
    > div {
      display: flex;
      flex-wrap: wrap;
      // align-items: center;
      margin-bottom: 10px;
    }
    h3 {
      font-weight: bold;
      margin-left: 14px;
    }
    input {
      width: 40%;
      height: 2rem;
      font-size: 1.8rem;
      text-align: right;
      outline: none;
      border: none;
      border-bottom: 1px solid #ddd;
    }

    #amount-type{
      margin-left:1rem;
      margin-top: 1rem;
      margin-right:1rem;
      font-size: 1.8rem;

      > div{
        margin-right:1rem;
        input {
        display: none;
        }
        label {
          margin-right: 1.8rem;
          color: #aaa;
          cursor: pointer;
        }
        input:checked ~ label {
          color: #74b83e;
          font-weight: bold;
        }
      }
      
    }
  }

  #plan-select{
    margin-top: 28px;
    > div {
      display: flex;
      flex-wrap: wrap;
      // align-items: center;
      margin-bottom: 10px;
    }
    h3 {
      font-weight: bold;
      margin-left: 14px;
    }

    #plan-select-box{
      // background-color: $gray;
      font-size: 1.8rem;
      border-bottom: 1px solid $gray;
      width: 100%;
    }
  }

  #category {
    // margin-top: 2rem;
    margin: 0 0 0 0;
    width: 100%;

    #category-list{
      font-size: 1.8rem;

      > div {
        // margin-right: 1.5rem;
        width: auto;

        input {
          display: none;
        }
        label {
          color: #aaa;
          white-space: nowrap;
          cursor: pointer;
          margin-right: 1.8rem;
        }
        input:checked ~ label {
          color: #74b83e;
          font-weight: bold;
        }
      }
    }
  }

  #budget-memo{
    margin-top: 28px;
    > div {
      display: flex;
      flex-wrap: wrap;
      // align-items: center;
      margin-bottom: 10px;
    }
    h3 {
      font-weight: bold;
    }

    textarea{
      border:0;
      width: 100%;
      height:20rem;
    }
  }

  .space{
    height:100%;
  }

}
</style>

<script setup>
import { ref, reactive, computed, watch, onBeforeMount, onUnmounted } from "vue";
import { useBudgetStore } from "../../../stores/budget";
import { useTravelStore } from "../../../stores/travel";
import { toComaNumberString,toKoreaTimeString } from "../../../composable/util";
import { useRoute, useRouter } from "vue-router";
import DateTime from "../../../components/layouts/DateTime.vue";
import { useScheduleStore } from "../../../stores/plan/schedule";

const budgetStore = useBudgetStore();
const travelStore = useTravelStore()
const scheduleStore = useScheduleStore()
const router = useRouter()
const route = useRoute()

const travelId = computed(()=>route.params.id)
const budgetId = computed(()=>route.params.budgetId)
const budget = computed(()=>budgetStore.budget)
const scheduleList = computed(()=>scheduleStore.scheduleList||[])
const selectPlan = ref({})
const paymentTypeValue = ref(budget.value.amountOfPayment[0]==='-'?'-':'') 
const nowDateTime = computed(()=>{
  const endDate = travelStore.travel.endDate
  const now = toKoreaTimeString(new Date()).split('.')[0]
  if(endDate<now) return endDate
  return now
})
const preparationDateTime = computed(()=>travelStore.travel.startDate)

const categories = ref([
  "Food Expense",
  "Airfare",
  "Shopping",
  "Transportation",
  "Tourism",
  "Room Charge",
  "etc",
]);

//가계부에 맵핑된 일정을 검색
const initSelectPlan = ()=>{
  if(budget.value.planType && budget.value.planTypeId) {
    selectPlan.value = scheduleList.value.filter((plan)=>plan.type===budget.value.planType && plan.id === budget.value.planTypeId)[0]
    if(!selectPlan.value) selectPlan.value = {}
  }
}

watch(()=>budget.value,()=>{
  initSelectPlan()
})

watch(()=>budget.value.amountOfPayment,(newValue,oldValue)=>{
  newValue = newValue.replace(/,/g, "")
  if(newValue.length===1 && newValue==='-') return

  if(isNaN(newValue)){
    budget.value.amountOfPayment = oldValue
  }
  else{
    budget.value.amountOfPayment = toComaNumberString(newValue)
  }
  paymentTypeValue.value = budget.value.amountOfPayment[0]==='-'?'-':''
})

watch(()=>paymentTypeValue.value,()=>{
  if(paymentTypeValue.value ==='' && budget.value.amountOfPayment[0] === '-') budget.value.amountOfPayment = budget.value.amountOfPayment.replace('-','') 
  if(paymentTypeValue.value ==='-' && budget.value.amountOfPayment[0] !== '-') budget.value.amountOfPayment = '-' + budget.value.amountOfPayment 
})

const validate = ()=>{
  const keyList = ['title','amountOfPayment','paymentDate','paymentType','categoryType']
  for(const key of keyList){
    if(!budget.value[key] || budget.value[key] === ""){
      console.log(key,budget.value[key])
      alert("내용을 채워주세요!")
      return false
    }
  }
  return true
}

const goBudget = ()=>{
    // scheduleStore.getscheduleList(travelId.value)
    router.push({name:"budget"})
}

const pretreatment = ()=>{
  if(selectPlan.value.id && selectPlan.value.type){
    budget.value.planTypeId = selectPlan.value.id
    budget.value.planType = selectPlan.value.type
  }
  else{
    budget.value.planTypeId = null
    budget.value.planType = null
  }
}

const postBudget = async()=>{
  if(validate()){
    pretreatment()

    const response = await budgetStore.postBudget(travelId.value)

    if(response.code !== 200) alert('등록에 실패 했습니다.')
    else goBudget()
  }
}

const putBudget = async()=>{
  if(validate()){
    pretreatment()

    const response = await budgetStore.putBudget(travelId.value)

    if(response.code !== 200) alert('수정에 실패 했습니다.')
    else goBudget()
  }
}

const delBudget = async()=>{
  if(validate()){
    const response = await budgetStore.delBudget(travelId.value)

    if(response.code !== 200) alert('삭제에 실패 했습니다.')
    else goBudget()
  }
}

onBeforeMount(async()=>{
  if(budgetId.value && !isNaN(budgetId.value)) {
    await budgetStore.getbudget(travelId.value,budgetId.value)
  }
  //스케쥴 리스트를 불러와 일정 맵핑정보로 사용
  if(!scheduleStore.scheduleList?.length) await scheduleStore.getscheduleList(travelId.value)
  
  initSelectPlan()
})

onUnmounted(()=>{
  budgetStore.resetBudget()
})

</script>
