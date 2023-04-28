<template>
<div id="check-list-container" class="full-hidden col">
  <div id="check-list-create-container">
    <ChecklistCreater />
  </div>
  
  <div id="check-list-contents-container">

    <div class="check-list-day">
      <h1 :class="{active: !dailyChecklistVisibleList.length || !dailyChecklistVisibleList.includes(false)}" @click="setAllDailyVisible">전체</h1>
      <div class="check-list-date" :class="{active: dailyChecklistVisibleList[index]}"  v-for="date,index in dayList" @click="setDailyVisible(index)">{{ date }}</div>
    </div>

    <div class="check-list-contents">
      <div class="contents" v-for="day,index in dayList" v-show="dailyChecklistVisibleList.length===0 || dailyChecklistVisibleList[index]">
        <h1 class="date">DAY {{ index+1 }} </h1> <p class="description"> {{ DateToStringFormat1(setDate(startDate,index)) }}</p>
        <div class="contents-list">
          <ChecklistList v-model="dailyChecklistList[index]" />
        </div>
      </div>
    </div>

  </div>
  
</div>
</template>

<style lang="scss" scoped>

#check-list-container{

  .containerActive{
      border-radius: 2rem !important;
      // background-color: rgb(255, 255, 255);
    }

  #check-list-contents-container{
    padding: 1rem 0vh 0vh 0vh;
      display: flex;
      flex-direction: row;
      height: 100%;
      overflow: hidden;

      .check-list-day{
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

        .check-list-date{
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

      .check-list-contents{
        width: 100%;
        height: 100%;
        overflow: auto;
        margin:0rem;

        .contents{
          padding:2%;
          padding-right:0px;
          padding-top:0px;

          .contents-list{
            overflow: hidden;
            margin-left: 1rem;
          }
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
}

.description{
  color:gray;
}

</style>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import ChecklistCreater from '../../../components/ChecklistCreater.vue';
import ChecklistList from '../../../components/ChecklistList.vue'
import {useChecklistStore} from '../../../stores/plan/checklist'
import { useTravelStore } from '../../../stores/travel';
import { useRoute } from 'vue-router';
import { DateToStringFormat1 } from '../../../composable/util';

const travelStore = useTravelStore()
const checklistStore = useChecklistStore()
const route = useRoute()

const dailyChecklistVisibleList = ref([])
const dailyChecklistList = computed(()=>checklistStore.dailyChecklistList||[])
const startDate  = computed(()=>new Date(travelStore.travel.startDate.split('T')[0]))
const travelId = computed(()=>route.params.id)
const dayList = computed(()=>travelStore.dayList)

const setDailyVisible = (dailyChecklistIndex)=>{
  if(dailyChecklistVisibleList[dailyChecklistIndex]==undefined) {
    while(dailyChecklistVisibleList.value.length < dayList.value.length)dailyChecklistVisibleList.value.push(false)
  }

  dailyChecklistVisibleList.value[dailyChecklistIndex] = !dailyChecklistVisibleList.value[dailyChecklistIndex]
  
  for(const state of dailyChecklistVisibleList.value){
    if(state == true) return
  }
  dailyChecklistVisibleList.value = []
}

const setAllDailyVisible = ()=>{
  dailyChecklistVisibleList.value = []
}

const setDate = (date,index)=>{
  const newDate = new Date(date)
  newDate.setDate(date.getDate()+index)
  
  return newDate
}

onMounted(()=>{
  checklistStore.getChecklistList(travelId.value)
})

</script>
