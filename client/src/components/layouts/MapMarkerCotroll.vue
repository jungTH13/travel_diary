<template>
<div class="schedule-container col">
    <div class="schedule-day-container">
        <div class="schedule-date-expense">
            <h1 class="title">날짜</h1>
            <font-awesome-icon icon="fa-solid fa-caret-down" class="icon" />
            <h1 style="cursor: pointer;" :class="{active: !dailyScheduleVisibleList.length || !dailyScheduleVisibleList.includes(false)}" @click="setDailyVisible(-1)">전체</h1>
            <div class="schedule-date" :class="{active: dailyScheduleVisibleList[index]}"  v-for="date,index in dayList" @click="setDailyVisible(index)">{{ date }}</div>
        </div>
    </div>

    <div class="schedule-type-container">
        <div class="schedule-type-expense">
            <div style="margin:auto; text-align: center;">
                <h1 class="title">유형</h1>
                <font-awesome-icon icon="fa-solid fa-caret-down" class="icon" />
            </div>
            <div class="schedule-type" :class="{active: (nowTap === tap?true:false)}" v-for="tap in nav" @click="tapChange(tap)">{{ tap.name }}</div>
        </div>
    </div>
</div>
</template>

<style lang="scss" scoped>
.schedule-container{
    padding: 0.5rem 0vh 0vh 0vh;
    display: flex;
    flex-direction: column;
    height: 100%;
    overflow: hidden;
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 15px 0px 0px 15px;
    padding:0.5rem;

    
    .schedule-day-container{
        max-width:100px;
        width: 5rem;
        height: 100%;
        overflow-y: auto;
        overflow-x: clip;
        text-align: center;
        background-color: rgba(128, 128, 128, 0.199);
        border-radius: 15px;
        padding:0.3rem;

        .schedule-date-expense{
            transition: all ease 1s 0s;
            max-height: 6.5rem;
            
            .icon{
                height:3rem;
                width: 3rem;
                transition: all ease 1s 0s;
            }
        }
        .schedule-date-expense:hover{
            max-height: 60vh;
            font-size:2rem;
        }
        .schedule-date-expense:hover .icon{
            height: 1.5rem !important;
            width: 1.5rem !important;
        }

        h1{
            font-size: 2rem;
        }

        .schedule-date{
            cursor: pointer;
            width: 3rem;
            height: 3rem;
            position:relative;
            padding-top: 0px;
            margin: auto;
            margin-top: 2rem;
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

    .schedule-type-container{
        max-width:100px;
        height: 100%;
        margin-top:1rem;
        background-color: rgba(128, 128, 128, 0.199);
        border-radius: 15px;
        padding:0.3rem;
        overflow-y: auto;
        overflow-x: clip;


        .schedule-type-expense{
            transition: all ease 1s 0s;
            max-height: 6.5rem;
            
            .icon{
                height:3rem;
                width: 3rem;
                transition: all ease 1s 0s;
            }
        }
        .schedule-type-expense:hover{
            max-height: 60vh;
            font-size:2rem;
        }
        .schedule-type-expense:hover .icon{
            height: 1.5rem !important;
            width: 1.5rem !important;
        }

        h1{
            font-size: 2rem;
        }
        
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
}
</style>

<script setup>
import { computed, onMounted, ref, watch } from 'vue';
import { useMapStore } from '../../stores/map';
import { useScheduleStore } from '../../stores/plan/schedule';
import { useTravelStore } from '../../stores/travel';
import { useBookStore } from '../../stores/plan/book';
import { useGoogleMapApi } from '../../composable/useGoogleMapApi';

const mapStore = useMapStore()
const scheduleStore = useScheduleStore()
const travelStore = useTravelStore()
const bookStore = useBookStore()

const googleAPi = useGoogleMapApi()

const dailyScheduleVisibleList = ref([])
const dailyScheduleList = computed(()=>scheduleStore.dailyScheduleList)
const dayList = computed(()=>travelStore.dayList)
const nav = computed(()=>bookStore.nav)
const nowTap = ref({})


const tapChange = (tap)=>{
  nowTap.value = tap
  setDailyMarkerList()
}

const setDailyVisible = (dailyScheduleIndex)=>{
    try{
        if(dailyScheduleIndex === -1) return dailyScheduleVisibleList.value = []

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
    finally{
        setDailyMarkerList()
    }
}

const setDailyMarkerList = ()=>{
  mapStore.setDailyMarkerList(
    dailyScheduleList.value,
    dailyScheduleVisibleList.value,
    nowTap.value.type
    )
}

watch(()=>scheduleStore.scheduleList,()=>{
    setDailyMarkerList()
})


onMounted(async()=>{
    await googleAPi.init()
    
    setDailyMarkerList()
})

</script>