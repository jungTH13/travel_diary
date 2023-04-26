<template>
<div>
    <div class="plan-contents-wrapper">
        <div class="plan-wrapper" v-for="plan in planList">
            <div class="plan">
                <div class="progress-line">
                    <div class="spot"></div>
                    <div class="v-line" v-if="secondType.includes(plan['type'])"></div>
                </div>
                <div class="plan-contents">
                    <p class="time">{{ toAMPMString(getFirstDateString(plan)) }}</p>
                    <p class="description">{{ getDescription(plan,'first') }} </p>
                    <div class="plan-memo col">
                        <div class="title">{{ plan['title'] }}</div>
                        <div v-if="!memoState[`${plan['type']}-${plan['id']}`]">
                            <div class="memo"> {{ plan['memo'] }}</div>
                            <div class="modify-box">
                                <font-awesome-icon icon="fa-solid fa-pen-to-square" class="icon" @click="memoState[`${plan['type']}-${plan['id']}`]=!memoState[`${plan['type']}-${plan['id']}`]" />
                            </div>
                        </div>
                        <div v-else>
                            <div class="memo"><textarea v-model="plan['memo']"></textarea></div>
                            <div class="modify-box">
                                <font-awesome-icon icon="fa-solid fa-pen-to-square" class="icon" style="color:green;" @click="putPlanMemo(plan)" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="plan" v-if="secondType.includes(plan['type'])">
                <div class="progress-line">
                    <div class="spot"></div>
                </div>
                <div class="plan-contents">
                    <p class="time">{{ toAMPMString(getSecondDateString(plan)) }}</p>
                    <p class="description">{{ getDescription(plan,'second') }} </p>
                    
                </div>
            </div>
        </div>
    </div>
</div>
</template>

<style lang="scss" scoped>
.plan-contents-wrapper{
    .plan-wrapper{
        margin-bottom: 1rem;
        
        .plan{
            display: flex;
            flex-direction: row;

            .progress-line{
                max-width:3rem;
                width:10%;
                margin-right:1rem;
            }

            .plan-contents{
                width:100%;

                .time{
                    font-size:1.5rem;
                    font-weight: 600;
                    display: inline;
                    margin-right:1rem;
                }
                .description{
                    display: inline;
                    font-size: 1.2rem;
                    font-weight: 600;
                    color:gray
                }

                .plan-memo{
                    background-color: $gray;
                    border-radius: 10px;
                    min-height:8rem;
                    padding:8px;
                    overflow: hidden;

                    .title{
                        font-size:1.4rem;
                        font-weight: 600;
                        margin-bottom: 0.5vh;
                    }
                    .memo{
                        font-size:1.2rem;
                        font-weight: 300;
                        min-height: 4rem;
                    }
                    textarea{
                        width: 100%;
                        background-color: white;
                        border-radius: 5px;
                    }

                    .modify-box{
                        position: relative;
                        .icon{
                            cursor: pointer;
                            position:absolute;
                            bottom:1rem;
                            right:0;
                            width:1.5rem;
                            height: 1.5rem;
                        }
                    }
                    

                }
            }
        }
    }
}
.v-line{
    z-index: 0;
    position: relative;
    height: 100%;
    left:50%;
    border-left: 0.3rem solid $gray;
}

.spot{
    position: relative;
    z-index: 100;
    height:1.3rem;
    width:1.3rem;
    margin: auto;
    margin-top:1rem;
    background-color: $green;
    border-radius: 50%;
}
</style>

<script setup>

import { computed, defineProps, ref, watch } from "vue";
import {toAMPMString} from "../composable/util"
import {useScheduleStore} from "../stores/plan/schedule"
import { useRoute } from "vue-router";

const props = defineProps({
modelValue: Array,
});
const emit = defineEmits(['update:modelValue'])

const scheduleStore = useScheduleStore()
const route = useRoute()

const planList = computed(()=>props.modelValue||[])
const travelId = computed(()=>route.params.id||null)
const memoState = ref({})

const getPlanType = (pl)=>{

}

const getDescription = (plan,type)=>{
    if(type === 'first'){
        const target = ['name','departLocation']
        for(const key of Object.keys(plan)){
            if(target.includes(key))return plan[key]
        }
    }
    if(type === 'second'){
        if(['ph'].includes(plan.type)) return "체크아웃"
        const target = ['arriveLocation']
        for(const key of Object.keys(plan)){
            if(target.includes(key))return plan[key]
        }
    }

    return ""
}

const getFirstDateString = (plan)=>{
    const target = ['date','departDate','reservationDate','checkinDate']
    
    for(const key of Object.keys(plan)){
        if(target.includes(key))return plan[key]
    }
}

const secondType = ['pa','ph','pt']
const getSecondDateString = (plan)=>{
    const target = ['arriveDate','checkoutDate']
    
    for(const key of Object.keys(plan)){
        if(target.includes(key))return plan[key]
    }
}

const putPlanMemo = async(plan)=>{
    const response = await scheduleStore.putPlanMemoOnly(travelId.value,plan)

    if(response.code !== 200) alert('수정에 실패 했습니다.')
    memoState.value={}
    scheduleStore.getscheduleList(travelId.value)
}

</script>
  