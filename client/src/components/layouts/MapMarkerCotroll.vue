<template>
<div id="interface2">
    <div class="schedule-day-container row">
        <v-container id="select-date" class="pa-0">
            <v-slide-group show-arrows class="pa-0">
                <h1 style="cursor: pointer;" :class="{active: !dailyScheduleVisibleList.length || !dailyScheduleVisibleList.includes(false)}" @click="setDailyVisible(-1)">전체</h1>
                <div class="schedule-date" :class="{active: dailyScheduleVisibleList[index]}"  v-for="date,index in dayList" @click="setDailyVisible(index)">{{ date }}day</div>
            </v-slide-group>
        </v-container>
    </div>
</div>

<div id="interface1">
    <div class="schedule-container1 row" style="margin-bottom: 1rem;">
        <div class="marker-trace-contrainer">
            <div class="trace-box" @click="isTrace=!isTrace" :class="{active:isTrace}">
                <font-awesome-icon icon="fa-solid fa-crosshairs" class="icon" />
            </div>
        </div>
        <div class="marker-trace-contrainer">
            <div class="trace-box" @click="isCurrentPositionTrace=!isCurrentPositionTrace" :class="{active:isCurrentPositionTrace}">
                <font-awesome-icon icon="fa-solid fa-person" class="icon" />
            </div>
        </div>
    </div>

    <div class="schedule-container2 col">
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
</div>
</template>

<style lang="scss" scoped>

#interface1{
    position: inherit;
    top:6rem;
    right:0rem;
    width: 7rem;
    // top:4rem;
}

#interface2{
    position: absolute;
    top:0rem;
    width: 100%;
    // margin-left: 5%;
    // margin-right:5%;
    left:0rem;

    .schedule-day-container{
        width: 100%;
        z-index: 10000;
        overflow-y: auto;
        overflow-x: clip;
        text-align: center;
        border-radius: 15px;
        padding:0.3rem;


        h1{
            font-size: 1.8rem;
            margin-right: 0.8rem;
            background-color: rgb(230, 230, 230);
            padding: 0 1rem 0 1rem;
            border-radius: 50px;
        }

        .schedule-date{
            cursor: pointer;
            // width: 3rem;
            height: 3rem;
            position:relative;
            padding: 0 0.5rem 0 0.5rem;
            margin: auto;
            // margin-top: 2rem;
            margin-right: 1rem;
            margin-left:0rem;
            // border: 0.1rem solid rgba(128, 128, 128, 0.384);
            background-color: rgb(230, 230, 230);
            color:black;
            border-radius: 50px;
            font-size:1.8rem;
            // text-shadow: -1px -1px 0 white, 1px -1px 0 white, -1px 1px 0 white, 1px 1px 0 white;
            // text-shadow: -1px -1px 0 white, 1px -1px 0 white, -1px 1px 0 white, 1px 1px 0 white;
        }

        h1.active{
            color: white;
            background-color: $green;
        }
        div.active{
            // border: 0.25rem solid $orange;
            // text-shadow: -1px -1px 0 white, 1px -1px 0 white, -1px 1px 0 white, 1px 1px 0 white;
            color: white;
            background-color: $green;
        }
    }
}

.schedule-container1{
    // padding: 0.5rem 0vh 0vh 0vh;
    display: flex;
    flex-direction: row;
    overflow: hidden;
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 15px 0px 0px 15px;
    padding:0.5rem;
    position: absolute;
    right:0;

    .marker-trace-contrainer{
        background-color: rgba(128, 128, 128, 0.3);
        width: 4rem;
        height: 4rem;
        right:0rem;
        border-radius: 15px;
        position:relative;
        overflow: hidden;
        display: flex;
        margin-right:1rem;

        .trace-box{
            text-align: center;
            margin:auto;
            cursor: pointer;

            &.active{
                color:$green;
            }
        }

        .icon{
            width: 3rem;
            height: 3rem;
        }
    }
}

.schedule-container2{
    display: flex;
    flex-direction: column;
    // height: 100%;
    overflow: hidden;
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 15px 0px 0px 15px;
    padding:0.5rem;
    position: absolute;
    top:6rem;
    right:0;
    .schedule-type-container{
        max-width:100px;
        max-height: 50vh;
        // margin-top:1rem;
        background-color: rgba(128, 128, 128, 0.3);
        border-radius: 15px;
        padding:0.3rem;
        overflow-y: auto;
        overflow-x: clip;


        .schedule-type-expense{
            transition: all ease 1s 0s;
            // max-height: 65rem;
            
            .icon{
                height:3rem;
                width: 3rem;
                transition: all ease 1s 0s;
            }
        }
        .schedule-type-expense:hover{
            // max-height: 60vh;
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

.schedule-plan-container{
    
}
</style>

<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { useMapStore } from '../../stores/map';
import { useScheduleStore } from '../../stores/plan/schedule';
import { useTravelStore } from '../../stores/travel';
import { useBookStore } from '../../stores/plan/book';
import { useGoogleMapApi } from '../../composable/useGoogleMapApi';
import { findPlanofDate } from '../../composable/util';

const emits = defineEmits(['currentPosition']) // 파라미터(x,y)

const mapStore = useMapStore()
const scheduleStore = useScheduleStore()
const travelStore = useTravelStore()
const bookStore = useBookStore()

const googleAPi = useGoogleMapApi()

const dailyScheduleVisibleList = ref(mapStore.dailyScheduleVisibleList)
const dailyScheduleList = computed(()=>scheduleStore.dailyScheduleList)
const dailyImageGroupList = computed(()=>scheduleStore.dailyImageGroupList)
const dayList = computed(()=>travelStore.dayList)
const nav = computed(()=>bookStore.nav)
const nowTap = ref(mapStore.nowTap)
const isTrace = ref(mapStore.isTrace)
const isCurrentPositionTrace = ref(mapStore.isCurrentPositionTrace)
let isCurrentPositionTraceFirstTime = true // 좌표 추적 첫시도시 정보
let watchPositionObj = null

let currentPositionMarker = null
const currentPositionInfo = ref({
    croods : {latitude:0,longitude:0},
    timestamp : null
})

const moveMarker = ({coords,timestamp})=>{
    if(!coords) return
    currentPositionInfo.value = {coords,timestamp}
    const lat = coords.latitude
    const lng = coords.longitude
    googleAPi.moveMarker(currentPositionMarker,lat,lng,isCurrentPositionTraceFirstTime )
    isCurrentPositionTraceFirstTime = false

    emits('currentPosition',coords.latitude,coords.longitude)
}


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
    // const dailyList = []

    
    // for(let index=0;index<dayList.value.length;index++){
    //     const scheduleList = dailyScheduleList.value[index] || []
    //     const imageList = dailyImageGroupList.value[index] || []
    //     const list = [...scheduleList,...imageList]
    //     list.sort((a,b)=>{
    //         const aDate = findPlanofDate(a)
    //         const bDate = findPlanofDate(b)
    //         if(aDate > bDate) return 1
    //         if(aDate < bDate) return -1
    //         return 0
    //     })
    //     dailyList.push(list)
    // }

    mapStore.setDailyMarkerList(
        dailyScheduleList.value,
        dailyImageGroupList.value,
        dailyScheduleVisibleList.value,
        nowTap.value.type,
        isTrace.value
    )
}

const setCurrentPositionTrace = ()=>{
    if(isCurrentPositionTrace.value === true) {
        if(isTrace.value === true) isTrace.value = false
        
        isCurrentPositionTraceFirstTime = true

        //디바이스의 위치정보를 수신
        if(navigator.geolocation){
            watchPositionObj = navigator.geolocation.watchPosition(moveMarker)
            moveMarker(currentPositionInfo.value)
        }
    }
    else {
        //디바이스의 위치정보 수신 중지
        if(watchPositionObj) {
            navigator.geolocation.clearWatch(watchPositionObj)
            watchPositionObj = null
        }
        googleAPi.moveMarker(currentPositionMarker,0,0 )

        emits('currentPosition',null,null)
    }
}

watch(()=>scheduleStore.scheduleList,()=>{
    setDailyMarkerList()
})


watch(()=>isTrace.value,(newValue,oldValue)=>{
    if(newValue === true) {
        if(isCurrentPositionTrace.value === true)
            isCurrentPositionTrace.value =false
        setDailyMarkerList()
    }
})
watch(()=>isCurrentPositionTrace.value,(newValue,oldValue)=>{
    setCurrentPositionTrace()
})


onMounted(async()=>{
    await googleAPi.init()
    await googleAPi.getMap()
    setTimeout(setDailyMarkerList,0)
    
    currentPositionMarker = googleAPi.setPositionMarker(0,0)
    setCurrentPositionTrace()
})

onUnmounted(()=>{
    currentPositionMarker.setMap(null)
    mapStore.isTrace = isTrace.value
    mapStore.isCurrentPositionTrace = isCurrentPositionTrace.value
    if(watchPositionObj) navigator.geolocation.clearWatch(watchPositionObj)
})

</script>