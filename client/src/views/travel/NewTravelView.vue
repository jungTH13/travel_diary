<template>
  <div class="plan-container">
    <section>
      <SelectedCountries v-model="travel.countryList" />
      <div class="plan-title">
        <input
          type="text"
          v-model="travel.title"
        />
      </div>
      <div class="plan-date">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="planDate" />
        </a-space>
      </div>
    </section>
    <div class="plan-footer">
      <button v-if="travel.id" class="font-weight-600" @click="putPlan">수정</button>
      <button v-if="travel.id" class="font-weight-600" @click="delPlan">삭제</button>
      <button v-else class="font-weight-600" @click="postPlan">등록</button>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>

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
const planDate = ref([]);


watch(()=>planDate.value,()=>{
  travel.value.startDate = toKoreaTimeString(planDate.value[0].$d).split('T')[0];
  travel.value.endDate = toKoreaTimeString(planDate.value[1].$d).split('T')[0];
})


async function postPlan() {
  if (planDate.value.length <= 0) {
    return alert("날짜를 선택해주세요");
  }

  const data = await travelStore.postTravel();
  console.log(data.results.travelId);
  router.push({
    name: "plan",
    params:{id:data.results.travelId}
  });
}

async function putPlan(){
  const response = await travelStore.putTravel()
  if(response.code !== 200 ) alert("여행 수정에 실패했습니다.") 
  
  router.push({
    name: "home",
  });
}

async function delPlan(){
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

  travel.value.title = travel.value.countryList.map(country=>country.name).join(',');

});
</script>
