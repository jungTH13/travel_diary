<template>
<div>
    <div class="plan-contents-wrapper">
        <div class="plan-wrapper" v-for="plan in planList">
            
            <div class="plan">

                <div class="progress-line">
                    <div class="spot"></div>
                    <div class="v-line" v-if="secondType.includes(plan['type'])"></div>
                </div>

                <div class="plan-contents" v-if="plan['type'] !=='pig'">
                    <p class="time">{{ toAMPMString(getFirstDateString(plan)) }}</p>
                    <p class="description">{{ getDescription(plan,'first') }} </p>
                    <p class="consumption-amount">{{ plan.sumAmount?`소비금액: ${ toComaNumberString(plan.sumAmount) } 원`:'' }}</p>

                    <div class="plan-memo" @click="selectPlan=plan">
                        <div class="title">{{ plan['title'] }}</div>
                        <div class="memo" v-html="plan['memo']?.replaceAll('\n','<br/>')"></div> 
                        <div class="image-view" v-if="plan.thumbNailList"><ImagesViewer :thumb-nail-list="plan.thumbNailList" image-size="5rem" :disabled="true" :no-detail="true" /></div>
                        <div class="checklist" v-if="plan.checkList?.length">
                            <p v-for="checklist in plan.checkList" :class="{completed:checklist.isCompleted===true}">
                                <font-awesome-icon icon="fa-solid fa-list-check" class="icon"/>
                                {{ checklist.title }}
                            </p>
                        </div>
                    </div>
                </div>
                <div class="plan-contents-image-group" v-else>
                    <p class="time">{{ toAMPMString(getFirstDateString(plan)) }}</p>
                    <p class="description">{{ plan['title'] }} </p>

                    <font-awesome-icon icon="fa-solid fa-ellipsis" id="edit-item-img" class="icon" @click="editPlan(plan)" />

                    <div class="plan-memo" @click="selectPlan=plan">
                        <!-- <div class="title">{{ plan['title'] }}</div> -->
                        <!-- <div class="memo" v-html="plan['memo']?.replaceAll('\n','<br/>')"></div>  -->
                        <div class="image-view" v-if="plan.thumbNailList"><ImagesViewer :thumb-nail-list="plan.thumbNailList" image-size="5rem" :disabled="true" :no-detail="true" /></div>
                        <!-- <div class="checklist" v-if="plan.checkList?.length">
                            <p v-for="checklist in plan.checkList" :class="{completed:checklist.isCompleted===true}">
                                <font-awesome-icon icon="fa-solid fa-list-check" class="icon"/>
                                {{ checklist.title }}
                            </p>
                        </div> -->
                    </div>
                </div>

            </div>

            <div class="plan" v-if="secondType.includes(plan['type'])">
                <div class="progress-line">
                    <div class="spot"></div>
                </div>
                <div class="plan-contents">
                    <p class="time">{{ DateToStringFormat1(new Date(plan.arriveDate || plan.checkoutDate)) }} {{ toAMPMString(getSecondDateString(plan)) }}</p>
                    <p class="description">{{  getDescription(plan,'second') }} </p>
                </div>
            </div>

        </div>
    </div>

    <PlanListOverlay v-model="selectPlan" />
</div>
</template>

<style lang="scss" scoped>
.plan-contents-wrapper{
    padding:0.5rem;
    padding-left: 0rem;
    // border-radius: 15px;
    // background-color: rgba(214, 214, 214, 0.301);
    
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

                .consumption-amount{
                    display: inline;
                    float: right;
                    font-size: 1.2rem;
                    font-weight: 600;
                    color:red;
                }

                .plan-memo{
                    // background-color: $gray;
                    // border-radius: 10px;
                    cursor: pointer;
                    box-shadow: 0.05rem 0.05rem 0.5rem 0rem gray;
                    min-height:4rem;
                    max-height: 40rem;
                    padding:8px;
                    overflow: auto;

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
                        
                    }

                    .image-view{
                        background-color: white;
                        z-index: 10000;
                    }

                }

                .checklist{
                    border-top: 0.3rem dashed $gray;
                    p{
                        font-size:1.2rem;
                        display: inline;
                        margin-right: 1rem;
                        color:gray;

                        &.completed{
                            color:$green;
                        }
                    }
                }
            }

            .plan-contents-image-group{
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

                .icon{
                    display: inline;
                    float: right;
                    cursor: pointer;
                    margin-right:1rem;
                    font-weight: 600;
                    color:rgb(0, 0, 0);
                }
                .plan-memo{
                    cursor: pointer;
                    // background-color: $gray;
                    background-color: white;
                    border-radius: 15px;
                    box-shadow: 0.05rem 0.05rem 0.5rem 0rem rgb(129, 163, 125);
                    min-height:4rem;
                    max-height: 40rem;
                    padding:8px;
                    overflow: auto;

                    .title{
                        font-size:1.4rem;
                        font-weight: 600;
                        margin-bottom: 0.5vh;
                    }

                    .image-view{
                        // background-color: white;
                        z-index: 10000;
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
import {DateToStringFormat1, toAMPMString, toComaNumberString, urlParse, getMapSearchInfo, findPlanofDate} from "../composable/util"
import PlanListOverlay from "./layouts/PlanListOverlay.vue";
import ImagesViewer from "./layouts/ImagesViewer.vue";
import { useRouter } from "vue-router";

const props = defineProps({
    modelValue: Array,
    imageGroupList:Array,
});
const emit = defineEmits(['update:modelValue'])

const router = useRouter()

const schduleList = computed(()=>props.modelValue||[])
const imageGroupList = computed(()=>props.imageGroupList||[])
const planList = computed(()=>{
    const allList = [...schduleList.value,...imageGroupList.value] 
    allList.sort((a,b)=>{
        const aDate = findPlanofDate(a)
        const bDate = findPlanofDate(b)
        if(aDate > bDate) return 1
        if(aDate < bDate) return -1
        return 0
    })
    return allList
})
const selectPlan = ref(null)

const editPlan = (plan)=>{
    if(plan.type ==='pig'){
        router.push({
            name:'imageGroup',
            params:{
                planId:plan.id
            }
        })
    }
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
  