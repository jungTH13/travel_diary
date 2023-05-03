<template>
<div>
    <div class="plan-contents-wrapper">
        <div class="plan-wrapper" v-for="plan in planList" @click="showPlanOptions(plan)">
            <div class="plan">
                <div class="progress-line">
                    <div class="spot"></div>
                    <div class="v-line" v-if="secondType.includes(plan['type'])"></div>
                </div>
                <div class="plan-contents">
                    <p class="time">{{ toAMPMString(getFirstDateString(plan)) }}</p>
                    <p class="description">{{ getDescription(plan,'first') }} </p>
                    <p class="consumption-amount">{{ plan.sumAmount?`소비금액: ${ toComaNumberString(plan.sumAmount) } 원`:'' }}</p>

                    <div class="plan-memo">
                        <div class="title">{{ plan['title'] }}</div>
                        <div class="memo" v-html="plan['memo']?.replaceAll('\n','<br/>')"></div> 
                        <div class="checklist" v-if="plan.checkList?.length">
                            <p v-for="checklist in plan.checkList" :class="{completed:checklist.isCompleted===true}">
                                <font-awesome-icon icon="fa-solid fa-list-check" class="icon"/>
                                {{ checklist.title }}
                            </p>
                        </div>
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

    <!-- TO DO : 작은 컴포넌트로 분리  -->
    <div :class="{active:(selectPlan)}" id="overlay" class="col"> 
        <div id="overlay-empty" @click="closePlanOptions"></div>
        
        <div id="plan-options" :class="{active:optionsVisible}">
            <div style="padding:2rem;">
                <div class="select-plan" v-if="selectPlan" >
                    <div class="title">{{ selectPlan?.title }} <MapLocationIcon v-if="selectPlanSearchInfo" :is-registration="false" width="3rem" height="3rem" v-model="selectPlanSearchInfo" /></div>
                    <div class="type">{{ bookNavCodes[selectPlan?.type] }}</div>
                    <div class="memo" v-html="urlParse(selectPlan['memo']?.replaceAll('\n','<br/>'))"></div>
                </div>

                <div class="summit-footer">
                    <button   class="font-weight-600 " @click="memoModifyVisible= true;optionsVisible=false;">메모 작성</button>
                    <button class="font-weight-600 " >사진 추가</button>
                </div>
            </div>
        </div>

        <div id="memo-modify" :class="{active:memoModifyVisible}">
            <div class="full-hidden col" style="padding:2rem;">
                <div class="title">{{ selectPlan?.title }}</div>
                <textarea v-if="selectPlan" class="memo-modify-box" v-model="selectPlan.memo">
                </textarea>
                <div class="summit-footer white-style">
                    <button class="font-weight-600 " @click="closePlanOptions">취소</button>
                    <button class="font-weight-600 " @click="putPlanMemo(selectPlan)" >저장</button>
                </div>
            </div>
        </div>
    </div>

    

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
        cursor: pointer;
        
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
                    box-shadow: 0.05rem 0.05rem 0.5rem 0rem gray;
                    min-height:4rem;
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
                    
                    .options{
                        bottom: 0;
                        opacity: 1 !important;
                        position: absolute;
                        padding: 0.5rem;
                        height: 100%;
                        width: 100%;
                        background-color: white;
                        .button-box{
                            margin-left: auto;
                            .button{
                                font-size: 1rem;
                                font-weight: 600;
                                height:3rem;
                                
                                margin-left:1rem;
                                background-color: $green;
                                color:white;
                            }
                        }
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

#overlay{
    position: absolute;
    z-index: 110000;
    background-color: rgba(0, 0, 0, 0.164);
    width: 100%;
    height: 0;
    bottom:0;
    left:0;
    overflow: hidden;

    &.active{
        height: 100vh;
    }

    #overlay-empty{
        height: 100%;
    }

    #plan-options{
        background-color: white;
        bottom: 0px;
        border-radius: 15px 15px 0 0;
        width: 100%;
        max-height: 0px;
        transition: all ease 0.5s 0s;

        .select-plan{
            margin-bottom: 2.5rem;
            .title{
                font-size: 2rem;
                font-weight: 600;
            }
            .type{
                font-size:1.5rem;
                color: gray;
            }
            .memo{
                font-size:1.3rem;
            }
        }
        &.active{
            max-height: 70vh;
        }
    }

    #memo-modify{
        position:absolute;
        background-color: white;
        height:0%;
        max-height: 400px;
        max-width: 350px;
        width: 90%;
        left:50%;
        top:50%;
        transform: translate(-50%, -50%);
        overflow: hidden;
        display: flex;
        transition: all ease 0s 0.3s;

        &.active{
            height:90%;
        }

        .title{
            font-size: 2rem;
            font-weight: 600;
            margin-bottom: 1rem;
        }

        .memo-modify-box{
            width: 100%;
            height:100%;
            resize:none;
            border:none;
            border-bottom: 1px solid $gray;
        }
    }
}
</style>

<script setup>

import { computed, defineProps, ref, watch } from "vue";
import {DateToStringFormat1, toAMPMString, toComaNumberString, urlParse, getMapSearchInfo} from "../composable/util"
import {useScheduleStore} from "../stores/plan/schedule"
import { useRoute } from "vue-router";
import ExpantionComponent from "./layouts/ExpantionComponent.vue";
import { useBookStore } from "../stores/plan/book";
import MapLocationIcon from "./layouts/MapLocationIcon.vue";

const props = defineProps({
modelValue: Array,
});
const emit = defineEmits(['update:modelValue'])

const scheduleStore = useScheduleStore()
const bookStore = useBookStore()
const route = useRoute()

const planList = computed(()=>props.modelValue||[])
const travelId = computed(()=>route.params.id||null)
const bookNavCodes = computed(()=>bookStore.nav.reduce((codes,nav)=>{codes[nav.type]=nav.name;return codes},{}))
const memoState = ref({})
const selectPlan = ref(null)
const selectPlanSearchInfo = ref(null)
const optionsVisible = ref(false)
const memoModifyVisible = ref(false)

const showPlanOptions = (plan)=>{
    selectPlan.value =plan
    setSelectPlanSearchInfo(plan)
    optionsVisible.value=true
    memoModifyVisible.value = false
}
const closePlanOptions = ()=>{
    scheduleStore.getscheduleList(travelId.value)
    selectPlan.value =null
    selectPlanSearchInfo.value = null
    optionsVisible.value=false
    memoModifyVisible.value = false
}

const setSelectPlanSearchInfo = async(plan)=>{
    await bookStore.getBook(travelId.value,plan.id,plan.type)
    selectPlanSearchInfo.value = getMapSearchInfo(bookStore.book)
    bookStore.resetBook()
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
    closePlanOptions()
    scheduleStore.getscheduleList(travelId.value)
}


</script>
  