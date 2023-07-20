<template>

<div class="schedule-plan-container">
    <div class="schedule-image-show-box" :class="{active:isBoxContentsClick}" @click="isBoxContentsClick = !isBoxContentsClick">
        <div class="image-show">
            <ImagesViewer v-if="mappingScheduleList[nowShowPlan]?.thumbNailList" :thumb-nail-list="mappingScheduleList[nowShowPlan].thumbNailList" :disabled="true" image-size="23vh" :no-detail="true" />
        </div>
    </div>
    <v-carousel v-model="nowShowPlan" class="plan-list-box" :class="{deactive:isBoxContentsClick}" show-arrows="hover"  hide-delimiters>
    

        <v-carousel-item v-for="mappingData,index in mappingScheduleList" style="height:100%;">
                <div class="d-flex fill-height align-center plan-info-box col" v-if="index===0 || mappingScheduleList[index-1] !== mappingData">
                    <div class="contents-name row">
                        <div style="white-space:nowrap;">{{  mappingData.name || mappingData.departLocation || mappingData.title }}</div> 
                        <div style="width:100%;"></div>
                        <div style="white-space:nowrap;"> {{ toAMPMString(findPlanofDate(mappingData)) }} {{ mappingData.departDate?'출발':'' }}</div>
                    </div>

                    <div class="plan-box-contents" @click="isBoxContentsClick = !isBoxContentsClick">
                        <div :class="{mainDeactive:isBoxContentsClick}" class="plan-box-contents-main" >
                            <div class="contents-title">{{ mappingData.title }}</div>
                            <div class="contents-text"><div v-html="mappingData.memo?.replaceAll('\n','<br/>')"></div></div>
                        </div>
                    </div>
                </div>
                <!-- 두번째 시간 지역 정보 존재시 추가 카드 생성 -->
                <div v-else class="d-flex fill-height align-center plan-info-box col">
                    <div class="contents-name row">
                        <div style="white-space:nowrap;">{{ mappingData.arriveLocation }}</div> 
                        <div style="width:100%;"></div>
                        <div style="white-space:nowrap;"> {{ toAMPMString(mappingData.arriveDate) }} 도착</div>
                    </div>

                    <div class="plan-box-contents" @click="isBoxContentsClick = !isBoxContentsClick">
                        <div :class="{mainDeactive:isBoxContentsClick}" class="plan-box-contents-main" >
                            <div class="contents-title">{{ mappingData.title }}</div>
                            <div class="contents-text"><div v-html="mappingData.memo?.replaceAll('\n','<br/>')"></div></div>
                        </div>
                    </div>
                </div>
        </v-carousel-item>

    </v-carousel>
</div>

</template>

<style lang="scss">
.schedule-plan-container{
    height:100%;
    max-width: 100%;
    margin:auto;

    .schedule-image-show-box{
        transition:all ease 0.5s 0s;
        height: 0%;
        width:100%;
        background-color: $overlay;
        overflow: hidden;
        display: flex;

        .image-show{
            margin:auto;
        }

        &.active{
            height: 100%;
        }
    }

    .plan-list-box{
        transition:all ease 0.5s 0s;
        height: 100% !important;
        max-width: 700px;
        margin:auto;

        &.deactive{
            height: 0% !important;
        }

        .plan-info-box{
            height: 100%;
            // background-color: rgba(0, 0, 0, 0.103);
            padding-top: 1rem;

            .contents-name{
                min-height: 3rem;
                font-size: 1.7rem;
                font-weight: 600;
                color:black;
                padding-right:auto;
                width:70%;
                padding: 0 0.3rem 0 0.3rem;
                overflow: hidden;
                text-shadow: -1px -1px 0 white, 1px -1px 0 white, -1px 1px 0 white, 1px 1px 0 white;
            }

            .plan-box-contents{
                height:75%;
                width:70%;
                border-radius: 15px;
                opacity: 0.9;
                background-color: white;
                box-shadow: 0.05rem 0.05rem 1.5rem gray;
                overflow: hidden;
                padding:1rem;

                

                .plan-box-contents-main{
                    transition: all ease 1.5s 0s;
                    height: 100%;
                    max-height:100vh;
                    margin-bottom: 1rem;
                    overflow: auto;

                    .contents-title{
                        font-weight: 600;
                        font-size:1.5rem;
                    }
                    .contents-text{
                        font-size:1.2rem;
                    }
                }

                .mainDeactive{
                    transition: all ease 1s 0s;
                    max-height:0vh;
                    overflow: hidden;
                    margin-bottom: 0rem;
                }

                .plan-box-contents-options{
                    width:100%;
                    max-height:0vh;
                    border-radius: 15px;
                    transition: all ease 1.5s 0s;
                    overflow: hidden;
                }

                .optionsActive{
                    max-height:70vh;
                    width:100%;
                }

            }
            
        }
    }
}
</style>

<script setup>
import { computed, ref, watch } from 'vue';
import { useMapStore } from '../../stores/map';
import { useScheduleStore } from '../../stores/plan/schedule';
import { useGoogleMapApi } from '../../composable/useGoogleMapApi';
import { findPlanofDate, toAMPMString } from '../../composable/util';
import ImagesViewer from './ImagesViewer.vue';


const mapStore = useMapStore()
const scheduleStore = useScheduleStore()

const googlMapApi = useGoogleMapApi()

const markerList = ref([])
const dailyScheduleList = computed(()=>scheduleStore.dailyScheduleList)
const showDailyScheduleMarkerList = ref([])
const mappingScheduleList = computed(()=>mapStore.markerListMappingPlanList)
const nowShowPlan = ref(0)
const isBoxContentsClick = ref(false)
const optionsHeight = ref("0vh")
const lastClickMarkerIndex = computed(()=>mapStore.lastClickMarkerIndex)

watch(()=>isBoxContentsClick.value,()=>{
    if(isBoxContentsClick.value===true) optionsHeight.value = "30vh"
    else optionsHeight.value = "0vh"
})

watch(()=>mappingScheduleList.value,()=>{
    if(mappingScheduleList.value.length<=nowShowPlan.value)
        nowShowPlan.value = 0
})

watch(()=>lastClickMarkerIndex.value,()=>{
    nowShowPlan.value = lastClickMarkerIndex.value
})

watch(()=>nowShowPlan.value,(newValue,oldValue)=>{
    if(mappingScheduleList.value.length === 0 )return
    if(mappingScheduleList.value.length<=oldValue) return nowShowPlan.value = 0

    isBoxContentsClick.value = false
    const showIndex = nowShowPlan.value

    
    const plan = mappingScheduleList.value[showIndex]

    if(showIndex>0 && mappingScheduleList.value[showIndex]===mappingScheduleList.value[showIndex-1])
        googlMapApi.moveMap(plan.x2,plan.y2)
    else 
        googlMapApi.moveMap(plan.x,plan.y)
    
})
</script>