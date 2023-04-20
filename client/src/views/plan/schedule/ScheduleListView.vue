<template>
  <div class="schedule-container">
    <div class="schedule-day-container">
      <h1 :class="{active: !dailyScheduleVisibleList.length || !dailyScheduleVisibleList.includes(false)}" @click="setAllDailyVisible">전체</h1>
      <div class="schedule-date" :class="{active: dailyScheduleVisibleList[index]}"  v-for="date,index in dayList" @click="setDailyVisible(index)">{{ date }}</div>
    </div>
    <div class="schdule-contents-container">
      <div class="contents" v-for="day,index in dayList" v-show="dailyScheduleVisibleList.length===0 || dailyScheduleVisibleList[index]">
        <h1 class="date">DAY {{ index+1 }} </h1> <p class="description"> {{ DateToStringFormat1(setDate(startDate,index)) }}</p>
        <PlanList v-model="dailyScheduleList[index]" />
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>

.schedule-container{
  padding: 1vh 0vh 1vh 1vh;
  display: flex;
  flex-direction: row;
  .schedule-day-container{
    max-width:100px;
    width:10%;
    height: 100%;
    margin:5px;
    text-align: center;

    h1{
      cursor: pointer;
      font-size:2.5vh;
      margin-bottom: 5vh;
    }

    .schedule-date{
      cursor: pointer;
      width: 5vh;
      height: 5vh;
      position:relative;
      padding-top: 0px;
      margin: auto;
      margin-bottom: 3vh;
      border: 3px solid rgba(0, 0, 0, 0.404);
      border-radius: 50%;
      font-size:3vh;
    }

    h1.active{
      color: $orange;
    }
    div.active{
      border: 3px solid $orange;
      color: $orange;
    }
  }

  .schdule-contents-container{
    width:100%;
    margin:5px;

    .contents{
      padding:2%;
      padding-right:0px;
      padding-top:0px;

      .date{
        margin-left:4%;
        margin-right:1%;
      }
      h1{
        font-size:3vh;
        display: inline;
      }
      p{
        font-size: 1.5vh;
        display: inline;
      }
    }

  }
}

.description{
  color:gray;
}

</style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useScheduleStore } from "../../../stores/plan/schedule";
import { useTravelStore } from "../../../stores/travel";
import { DateToStringFormat1 } from "../../../composable/util";
import PlanList from "../../../components/PlanList.vue";

const route = useRoute()
const router = useRouter();
const scheduleStore = useScheduleStore();
const travelStore = useTravelStore();


//contents
const schedulelList = computed(()=>scheduleStore.searchscheduleList);
const dailyScheduleVisibleList = ref([])
const dailyScheduleList = computed(()=>scheduleStore.dailyScheduleList)
const startDate  = computed(()=>new Date(travelStore.travel.startDate.split('T')[0]))
const travelId = computed(()=>route.params.id)
const dayList = computed(()=>travelStore.dayList)


const setDailyVisible = (dailyScheduleIndex)=>{
  if(dailyScheduleVisibleList[dailyScheduleIndex]==undefined) {
    while(dailyScheduleVisibleList.value.length < dayList.value.length)dailyScheduleVisibleList.value.push(false)
  }

  dailyScheduleVisibleList.value[dailyScheduleIndex] = !dailyScheduleVisibleList.value[dailyScheduleIndex]
  
  for(const state of dailyScheduleVisibleList.value){
    if(state == true) return
  }
  dailyScheduleVisibleList.value = []
}
const setAllDailyVisible = ()=>{
  dailyScheduleVisibleList.value = []
}

const setDate = (date,index)=>{
  const newDate = new Date(date)
  newDate.setDate(date.getDate()+index)
  
  return newDate
}

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(() => {
  scheduleStore.getscheduleList(travelId.value)
});
</script>
