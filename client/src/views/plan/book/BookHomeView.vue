<template>
  <div class="schedule-container">
    <div class="schedule-type-container">
      <div class="schedule-type" :class="{active: (nowTap === tap?true:false)}" v-for="tap in nav" @click="tapChange(tap)">{{ tap.name }}</div>
    </div>
    <div class="schdule-contents-container">
      <div class="contents" v-for="day,index in dayList">
        <h1 class="date">DAY {{ index+1 }} </h1> <p class="description"> {{ DateToStringFormat1(setDate(startDate,index)) }}</p>
        <div class="plan" v-for="plan,index2 in dailyScheduleList[index]" @click="goDetailPage(plan)" >
          <PlanItem v-if="nowTap['type']==='' || nowTap['type'] === dailyScheduleList[index][index2]['type'] " v-model="dailyScheduleList[index][index2]"/>
        </div>
      </div>
    </div>
  </div>
  <div class="create-container">
    <div class="create-botton">
      <span class="plus-button green">
        <font-awesome-icon icon="fa-solid fa-plus" id="plus-button-img" />
      </span>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.schedule-container{
  display: flex;
  flex-direction: row;
  height: 100%;
  overflow: hidden;
  .schedule-type-container{
    max-width:100px;
    height: 100%;
    margin:0.3rem;
    
    .schedule-type{
      cursor: pointer;
      padding-top: 1rem;
      margin: auto;
      margin-bottom: 1rem;
      height: 3.8rem;
      font-size:2rem;
    }

    div.active{
      color: $orange;
    }
  }

  .schdule-contents-container{
    width:100%;
    height: 100%;
    overflow-y: auto;
    margin:1rem;
    flex: 1;

    .contents{
      // padding:2vh;
      padding-right:0px;
      padding-top:0px;

      .date{
        margin-left:1rem;
        margin-right:0.8rem;
      }
      .plan{
        margin-bottom: 2rem;
      }

      h1{
        font-size:2.5rem;
        display: inline;
      }
      p{
        font-size: 1.4rem;
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
    position:absolute;
    bottom:-0.5rem;
    cursor: pointer;
    margin:auto;
  }
}

.description{
  color:gray;
}
</style>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useScheduleStore } from "../../../stores/plan/schedule";
import { useTravelStore } from "../../../stores/travel";
import { DateToStringFormat1 } from "../../../composable/util";
import { computed } from "@vue/reactivity";
import PlanItem from "../../../components/PlanItem.vue";


const route = useRoute();
const router = useRouter();
const scheduleStore = useScheduleStore();
const travelStore = useTravelStore();

//contents
const travelId = computed(()=>route.params.id)
const nowTap = ref({});
const tapChange = (tap)=>{
  nowTap.value = tap
}

const nav = ref([
  { type:'', name: "전체" },
  { type:'pa', name: "항공권" },
  { type:'ph', name: "호텔" },
  { type:'pr', name: "음식점" },
  { type:'pt', name: "교통" },
  { type:'pe', name: "기타" },
]);

const startDate  = computed(()=>new Date(travelStore.travel.startDate.split('T')[0]))
const dayList = computed(()=>travelStore.dayList)
const dailyScheduleList = computed(()=>scheduleStore.dailyScheduleList)

const setDate = (date,index)=>{
  const newDate = new Date(date)
  newDate.setDate(date.getDate()+index)
  
  return newDate
}

const goDetailPage = (plan)=>{
  router.push({
    name:'book-detail',
    params:{
      id : travelId.value,
      planId : plan.id,
      planType:plan.type
    }
  })
}

onMounted(() => {
  console.log("Mounted!");
  scheduleStore.getscheduleList(travelId.value)
  nowTap.value = nav.value[0]
});
</script>
