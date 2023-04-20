<template>
  <div class="schedule-container">
    <div class="schedule-type-container">
      <div class="schedule-type" :class="{active: (nowTap === tap?true:false)}" v-for="tap in nav" @click="tapChange(tap)">{{ tap.name }}</div>
    </div>
    <div class="schdule-contents-container">
      <div class="contents" v-for="day,index in dayList">
        <h1 class="date">DAY {{ index+1 }} </h1> <p class="description"> {{ DateToStringFormat1(setDate(startDate,index)) }}</p>
        <div class="plan" v-for="plan,index2 in dailyScheduleList[index]">
          <PlanItem v-if="nowTap['type']==='' || nowTap['type'] === dailyScheduleList[index][index2]['type'] " v-model="dailyScheduleList[index][index2]"/>
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.schedule-container{
  padding: 10px 10px 10px 10px;
  display: flex;
  flex-direction: row;
  .schedule-type-container{
    max-width:100px;
    width:15%;
    height: 100%;
    margin:5px;
    
    .schedule-type{
      cursor: pointer;
      padding-top: 3px;
      margin: auto;
      margin-bottom: 25px;
      width: 50px;
      height: 50px;
      font-size:x-large;
    }

    div.active{
      color: $orange;
    }
  }

  .schdule-contents-container{
    width:100%;
    margin:5px;

    .contents{
      padding:30px;
      padding-right:0px;
      padding-top:0px;

      .date{
        margin-left:20px;
        margin-right:5px;
      }
      .plan{
        margin-bottom: 30px;
      }

      h1{
        font-size:x-large;
        display: inline;
      }
      p{
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

onMounted(() => {
  console.log("Mounted!");
  scheduleStore.getscheduleList(travelId.value)
  nowTap.value = nav.value[0]
});
</script>
