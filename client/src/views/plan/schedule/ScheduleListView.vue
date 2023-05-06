<template>
  <div class="col full-hidden">
    <div class="schedule-container">
      <div class="schedule-day-container">
        <h1 :class="{active: !dailyScheduleVisibleList.length || !dailyScheduleVisibleList.includes(false)}" @click="setAllDailyVisible">전체</h1>
        <div class="schedule-date" :class="{active: dailyScheduleVisibleList[index]}"  v-for="date,index in dayList" @click="setDailyVisible(index)">{{ date }}</div>
      </div>
      <div class="schdule-contents-container">
        <!-- <div class="extra-feature-box-position">
          <div class="extra-feature-box">
            <div class="create-image-marker">
              <font-awesome-icon icon="fa-regular fa-images" />
              이미지 마커 추가하기
            </div>
          </div>
          
        </div> -->

        <div class="contents" v-for="day,index in dayList" v-show="dailyScheduleVisibleList.length===0 || dailyScheduleVisibleList[index]">
          <h1 class="date">DAY {{ index+1 }} </h1> <p class="description"> {{ DateToStringFormat1(setDate(startDate,index)) }}</p>
          <PlanList v-model="dailyScheduleList[index]" />
        </div>
      </div>
    </div>
    <div class="image-group-creater-box row" @click="goCreateImageGroup">
      <div id="image-input-icon">
        <font-awesome-icon icon="fa-solid fa-plus" class="icon" />
      </div>
      <span >이미지 그룹 만들기</span>
    </div>
  </div>
 
</template>

<style lang="scss" scoped>


.schedule-container{
  padding: 1rem 0vh 0vh 0vh;
  display: flex;
  flex-direction: row;
  height: 100%;
  overflow: hidden;
  .schedule-day-container{
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

    .schedule-date{
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

  .schdule-contents-container{
    width: 100%;
    height: 100%;
    overflow: auto;
    margin:0rem;

    .extra-feature-box-position{
      position: relative;
      .extra-feature-box{
        position:absolute;
        top:0;
        right:0;
        margin-right: 1rem;
        
        .create-image-marker{
          // background-color: $gray;
          border-radius: 3px;
          // border: 1px solid rgba(128, 128, 128, 0.445);
          padding:0.5rem;
          font-size:1.5rem;
          font-weight: 600;
        }
      }
    }

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

.description{
  color:gray;
}
.image-group-creater-box{
  margin-right:auto;
  margin-left:auto;
  white-space: nowrap;
  margin-top: 0.5rem;
  cursor: pointer;

  #image-input-icon{
        // margin:auto;
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 50%;
        background-color: $green;
        margin-right: 1rem;
        text-align: center;
        margin:auto;
        margin-left: 0;
        margin-right: 1rem;
        

        
        .icon{
            width: 2rem;
            height: 2rem;
            text-align: center;
            margin-top:0.2rem;
            color:white;
        }

        
    }

    span{
        font-size: 2rem;
        font-weight: 600;
    }
}

</style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useScheduleStore } from "../../../stores/plan/schedule";
import { useMapStore } from "../../../stores/map";
import { useTravelStore } from "../../../stores/travel";
import { DateToStringFormat1 } from "../../../composable/util";
import PlanList from "../../../components/PlanList.vue";
// import ImageGroupCreater from "../../../components/ImageGroupCreater.vue";

const route = useRoute()
const router = useRouter();
const scheduleStore = useScheduleStore();
const travelStore = useTravelStore();
const mapStore = useMapStore()

//contents
const schedulelList = computed(()=>scheduleStore.searchscheduleList);
const dailyScheduleVisibleList = ref([])
const dailyScheduleList = computed(()=>scheduleStore.dailyScheduleList)
const startDate  = computed(()=>new Date(travelStore.travel.startDate.split('T')[0]))
const travelId = computed(()=>route.params.id)
const dayList = computed(()=>travelStore.dayList)

const setDailyMarkerList = ()=>{
  // mapStore.setDailyMarkerList(dailyScheduleList.value,dailyScheduleVisibleList.value)
}

const goCreateImageGroup = ()=>{
  router.push({
    name:'imageGroup'
  })
}

const setDailyVisible = (dailyScheduleIndex)=>{
  // dayList에 매칭되어 길이만큼 리스트를 추가
  if(dailyScheduleVisibleList[dailyScheduleIndex]==undefined) {
    while(dailyScheduleVisibleList.value.length < dayList.value.length)dailyScheduleVisibleList.value.push(false)
  }

  dailyScheduleVisibleList.value[dailyScheduleIndex] = !dailyScheduleVisibleList.value[dailyScheduleIndex]
  
  //선택된 일자가 없는 경우 dailyScheduleVisibleList 를 초기화 
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

onBeforeMount(async() => {
  console.log("Before Mount!");
  await scheduleStore.getscheduleList(travelId.value)
});

onMounted(() => {
  
});
</script>
