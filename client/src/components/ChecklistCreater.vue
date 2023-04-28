<template>
<div>
    <v-expansion-panels v-model="panel">
        <v-expansion-panel id="check-list-create-container" :class="{containerActive:panel===0}">
        <v-expansion-panel-title class="create-container-title" expand-icon="mdi-plus" collapse-icon="mdi-minus">체크리스트 생성하기</v-expansion-panel-title>
        <v-expansion-panel-text class="create-container-contents">
            <div id="checklist-title">
                <div>
                    <font-awesome-icon icon="fa-solid fa-tag" class="icon"/>
                </div>
                <input
                    type="text"
                    placeholder="제목을 입력하세요"
                    v-model="checklist.title"
                />
            </div>

            <div id="plan-select">
                <div>
                    <font-awesome-icon icon="fa-solid fa-hand-pointer" class="icon" />
                </div>

                <select id="plan-select-box" v-model="selectPlan" >
                    <option :value="{}">연결하고 싶은 일정과 선택하세요</option>
                    <option v-for="plan of scheduleList" :value="plan">{{ plan.title }} | {{ plan.name }}</option>
                </select>
            </div>

            <div id="plan-dates">
                <div>
                    <font-awesome-icon icon="fa-regular fa-calendar-check" class="icon" />
                </div>

                <div id="plan-checklist-date-picker">
                    <DateTimeInline v-model="checklist.requireDate" placeholder="날짜를 선택해주세요" />
                </div>

                <v-container id="select-date" class="pa-0">
                <v-slide-group show-arrows>
                    
                    <div class="item">
                        <input
                            type="radio"
                            id="pre"
                            name="dates"
                            :value="preparationDateTime"
                            v-model="checklist.requireDate"
                        />
                        <label for="pre">여행 준비</label>
                    </div>

                </v-slide-group>
                </v-container>
            </div>

            <div class="summit-footer">
                <button class="font-weight-600" @click="postChecklist">등록</button>
            </div>

        </v-expansion-panel-text>
        </v-expansion-panel>

    </v-expansion-panels>
    
    
    
</div>
</template>

<style lang="scss" scoped>
div{
    #check-list-create-container{
        border-radius: 2rem;
    }

    .create-container-title{
        background-color: $gray;
        font-size: 1.5rem;
        font-weight: 600;
        border-radius: 2rem;
    }

    .create-container-contents{
        display: flex;
        flex-direction: column;
        background-color: $gray;

        .icon{
        width: 1.5rem;
        height: 1.5rem;
        }

        #checklist-title {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            input {
                font-size:1.5rem;
                margin-left: 1rem;
                width: 50%;
                height: 3rem;
                outline: none;
                border: none;
                border-bottom: 1px solid #ddd;
            }
        }

        #plan-select{
            margin-top: 1rem;
            display: flex;
            flex-direction: row;
            align-items: center;
            > div {
                display: flex;
                flex-wrap: wrap;
                // align-items: center;
                margin-bottom: 10px;
            }

            #plan-select-box{
                // background-color: $gray;
                margin-left: 1rem;
                font-size: 1.5rem;
                border-bottom: 1px solid #ddd;
                width: 90%;
            }
        }

                
        #plan-dates {
            margin-top: 1rem;
            font-size: 1.5rem;
            width: 100%;
            display: flex;
            flex-direction: row;
            margin-bottom: 1rem;

            #plan-checklist-date-picker{
                margin-left:1rem;
                width:28rem;
                border-bottom: 1px solid #ddd;
            }

            #select-date{
                margin-left:1rem;
            }

            > div {
                display: flex;
                flex-wrap: wrap;

                div,.item {
                    margin-right:1rem;
                    
                    input {
                        display: none;
                    }
                    label {
                        margin-right: 1.5rem;
                        color: #aaa;
                        cursor: pointer;
                    }
                    input:checked ~ label {
                        color: #74b83e;
                        font-weight: bold;
                    }
                }
            }
        }
        


    }


    .containerActive{
        border-radius: 2rem !important;
        overflow: hidden;
        background-color: rgb(185, 185, 185);
    }

    #check-list-contents-container{
    height: 100%;
    }

}
</style>

<script setup>
import { computed, onBeforeMount, ref, watch } from 'vue';
import { useScheduleStore } from '../stores/plan/schedule';
import { useTravelStore } from '../stores/travel';
import { useRoute } from 'vue-router';
import DateTimeInline from './layouts/DateTimeInline.vue';
import { useChecklistStore } from '../stores/plan/checklist';

const scheduleStore = useScheduleStore()
const travelStore = useTravelStore()
const checklistStore = useChecklistStore()
const route = useRoute()

const travelId = computed(()=>route.params.id)
const panel = ref([])
const checklist = computed(()=>checklistStore.checklist)
const selectPlan = ref({})
const scheduleList = computed(()=>scheduleStore.scheduleList)
const preparationDateTime = computed(()=>travelStore.travel.startDate||null)

const disabled =ref(false)

watch(()=>panel.value,()=>{
    checklistStore.resetChecklist()
})

const validate = ()=>{
  const keyList = ['title','requireDate']
  for(const key of keyList){
    if(!checklist.value[key] || checklist.value[key] === ""){
      console.log(key,checklist.value[key])
      alert("내용을 채워주세요!")
      return false
    }
  }
  return true
}

const pretreatment = ()=>{
    if(selectPlan.value.id && selectPlan.value.type){
        checklist.value['planType'] = selectPlan.value.type
        checklist.value['planId'] = selectPlan.value.id
    }
    checklist.value.planCheckListDetail = []
}

const postChecklist = async ()=>{
    if(validate()){
        pretreatment()

        const response = await checklistStore.postChecklist(travelId.value)
        if(response.code !== 200) alert('등록에 실패 했습니다.')
        else {
            checklistStore.resetChecklist()
            checklistStore.getChecklistList(travelId.value)
            panel.value = false
        }
    }
}

onBeforeMount(async()=>{
    //스케쥴 리스트를 불러와 일정 맵핑정보로 사용
  if(!scheduleStore.scheduleList?.length) scheduleStore.getscheduleList(travelId.value)
  if(!travelStore.travel.startDate) travelStore.getTravel(travelId.value)
})
</script>