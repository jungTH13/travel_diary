<template>
  <div class="plan-container full-hidden">
    <section class="col">
      <div id="travel-country">
        여행할 나라
      </div>
      <SelectedCountries v-model="travel.countryList" />
      <div class="plan-title row">
        <font-awesome-icon icon="fa-solid fa-book" class="icon" />
        <input
          type="text"
          v-model="travel.title"
        />
      </div>
      <div class="plan-date col">
        <div class="plan-date-range">
          <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
          <span >{{ planDate[0] }} ~ {{ planDate[1] }}</span>
        </div>
        <div class="plan-date-picker">
          <p v-if="travel.id" class="description"> ※ 여행일정이 존재하는 기간을 제외할 수 없습니다.</p>
          <p v-else class="description"> ※ 기간을 설정 후 Select를 클릭하여 확정 해 주세요</p>
          <VueDatePicker v-model="planDate" model-type="yyyy-MM-dd" :disabled-dates="disabledDates" range inline class="datePicker" />
        </div>
      </div>
      
    </section>
    <div class="summit-footer">
      <button v-if="travel.id" class="font-weight-600 " @click="delPlan">삭제</button>
      <button v-if="travel.id"  class="font-weight-600 " @click="putPlan">수정</button>
      <button v-else class="font-weight-600 " @click="postPlan">등록</button>
    </div>
  </div>

  
</template>

<style lang="scss" scoped >
.description{
  border-bottom:0px !important;
  color: blue;
  font-size: 1.5rem !important;
}
</style>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../../stores/travel";
import SelectedCountries from "../../components/SelectedCountries.vue";
import {toKoreaTimeString} from "../../composable/util"

const router = useRouter();
const travelStore = useTravelStore();

//콘텐츠
const travel = computed(()=>travelStore.travel)
const planDate = ref([travel.value.startDate.split('T')[0],travel.value.endDate.split('T')[0]]);
const minDate = computed(()=>travel.value.minDate?new Date(travel.value.minDate):null)
const maxDate = computed(()=>travel.value.maxDate?new Date(travel.value.maxDate):null)
const disabledDates = computed(()=>{
  if(!travel.value.id || !minDate.value || !maxDate.value) return []
  let disabledList = []
  const date = new Date(minDate.value)

  date.setDate(date.getDate())

  console.log(date.toJSON().split('T')[0],maxDate.value.toJSON().split('T')[0])
  while(date.toJSON().split('T')[0]<=maxDate.value.toJSON().split('T')[0]){
    disabledList.push(new Date(date))

    date.setDate(date.getDate()+1)
  }

  console.log(disabledList[0] , travel.value.startDate.split('T')[0])
  if(disabledList.length && disabledList[0]?.toJSON().split('T')[0] === travel.value.startDate.split('T')[0] ) disabledList = disabledList.slice(1)
  if(disabledList.length && disabledList[disabledList.length-1].toJSON().split('T')[0] === travel.value.endDate.split('T')[0]) disabledList = disabledList.slice(0,-1)
  return disabledList
})

// watch(()=>planDate.value,()=>{
//   travel.value.startDate = planDate.value[0];
//   travel.value.endDate = planDate.value[1]+`T23:59:59`;
// })

watch(()=>travel.value.countryList,()=>{
  if (travel.value.countryList.length <= 0) {
    alert("나라를 선택해주세요");
    return router.push({name:"new-country"});
  }
})

const validation = ()=>{
  if (!planDate.value[0] || planDate.value[0]==='' || !planDate.value[1] ||planDate.value[1]==='') {
    alert("날짜를 선택해주세요");
    return false
  }
  if(minDate.value && maxDate.value){
    console.log(planDate.value[0].split('T')[0] > minDate.value.toJSON().split('T')[0] || planDate.value[1].split('T')[0] < maxDate.value.toJSON().split('T')[0])
    if(planDate.value[0].split('T')[0] > minDate.value.toJSON().split('T')[0] || planDate.value[1].split('T')[0] < maxDate.value.toJSON().split('T')[0]){
      alert("등록된 일정의 기간이 포함되야 합니다!")
      return false
    }
  }
  return true
}

async function postPlan() {
  if(!validation()) return
  
  travel.value.startDate = planDate.value[0];
  travel.value.endDate = planDate.value[1]+`T23:59:59`;

  const data = await travelStore.postTravel();
  console.log(data.results.travelId);
  router.push({
    name: "plan",
    params:{id:data.results.travelId}
  });
}

async function putPlan(){
  if(!validation()) return

  travel.value.startDate = planDate.value[0];
  travel.value.endDate = planDate.value[1]+`T23:59:59`;
  
  const response = await travelStore.putTravel()
  if(response.code !== 200 ) alert("여행 수정에 실패했습니다.") 
  
  router.push({
    name: "home",
  });
}

async function delPlan(){
  if(minDate.value || maxDate.value) {
    if(!confirm("일정이 존재합니다!\n그래도 삭제할까요?")) return
  }
  const response = await travelStore.delTravel()
  if(response.code !== 200 ) alert("여행 삭제에 실패했습니다.") 
  
  router.push({
    name: "home",
  });
}

onMounted(() => {

  if (travel.value.countryList.length <= 0) {
    alert("나라를 선택해주세요");
    return router.push({name:"new-country"});
  }

  if(!travel.value.title || travel.value.title === '')travel.value.title = travel.value.countryList.map(country=>country.name).join(',');

});
</script>
