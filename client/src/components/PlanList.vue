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
                    <div class="plan-memo">
                        <p class="title">{{ plan['title'] }}</p>
                        <p class="memo"> {{ plan['memo'] }}</p>
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
        margin-bottom: 15px;
        
        .plan{
            display: flex;
            flex-direction: row;

            .progress-line{
                max-width:50px;
                width:10%;
                margin-right:10px;
            }

            .plan-contents{
                width:100%;

                .time{
                    font-size:medium;
                    font-weight: 600;
                    display: inline;
                    margin-right:5px;
                }
                .description{
                    display: inline;
                    font-weight: 600;
                    color:gray
                }

                .plan-memo{
                    background-color: $gray;
                    border-radius: 10px;
                    min-height:80px;
                    padding:8px;

                    .title{
                        font-size:medium;
                        font-weight: 600;
                        margin-bottom: 5px;
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
    border-left: 3px solid $gray;
}

.spot{
    position: relative;
    z-index: 100;
    height:15px;
    width:15px;
    margin: auto;
    margin-top:4px;
    background-color: $green;
    border-radius: 50%;
}
</style>

<script setup>

import { computed, defineProps, ref, watch } from "vue";
import {toAMPMString} from "../composable/util"

const props = defineProps({
modelValue: Array,
});
const emit = defineEmits(['update:modelValue'])

const planList = computed(()=>props.modelValue||[])

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


</script>
  