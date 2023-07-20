<template>
<div>
    <div id="check-list-create-container" class="col">
        <div class="create-container-title-container"  @click="disabled=!disabled">
            <div class="create-container-title">체크리스트 생성하기</div>
        </div>

    </div>


    <div :class="{active:(!disabled)}" id="overlay" class="col"> 
            <div id="overlay-empty" @click="disabled=!disabled"></div>
            
            <div class="create-container-contents default-shadow" :class="{active:!disabled}">
                <div id="checklist-title" >
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
                        <DateTime v-model="checklist.requireDate" placeholder="날짜를 선택해주세요" />
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
                <div style="height:100%;"></div>
                <div>
                    <div class="summit-footer white-style">
                        <button class="font-weight-600" @click="disabled=!disabled">취소</button>
                        <button class="font-weight-600" @click="postChecklist">등록</button>
                    </div>
                </div>
                
            </div>
        </div>
</div>
</template>

<style lang="scss" scoped>
div{
    #check-list-create-container{
        // background-color: $gray;
        border-radius: 0;
        min-height:4rem;
        overflow: hidden;
        padding:1rem;
        
    }

    .create-container-title-container{
        height:4rem;
        background-color: $green;
        border-radius: 50px;
        overflow: hidden;
        display: flex;
        cursor: pointer;
        .create-container-title{
        
            font-size: 1.5rem;
            font-weight: 600;
            
            text-align: center;
            margin:auto;
            color:white;
            
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


#overlay{
    position: absolute;
    z-index: 10000;
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

    .create-container-contents{
        display: flex;
        flex-direction: column;
        // background-color: $gray;
        // overflow: hidden;
        max-height: 0px;
        position: absolute;
        left:50%;
        top:50%;
        transform: translate(-50%, -50%);
        background-color: white;
        height:50rem;
        width:40rem;
        padding:1rem;
        

        .icon{
            width: 1.5rem;
            height: 1.5rem;
        }

        &.active{
            max-height: 100vh;
        }

        #checklist-title {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            margin-bottom: 1rem;

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
            margin-bottom: 1rem;

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
}
</style>

<script setup>
import { computed, onBeforeMount, ref, watch } from 'vue';
import { useScheduleStore } from '../stores/plan/schedule';
import { useTravelStore } from '../stores/travel';
import { useRoute } from 'vue-router';
import DateTimeInline from './layouts/DateTimeInline.vue';
import { useChecklistStore } from '../stores/plan/checklist';
import DateTime from './layouts/DateTime.vue';

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

const disabled =ref(true)

watch(()=>disabled.value,()=>{
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
        checklist.value['planTypeId'] = selectPlan.value.id
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
            disabled.value = true
        }
    }
}

onBeforeMount(async()=>{
    //스케쥴 리스트를 불러와 일정 맵핑정보로 사용
  if(!scheduleStore.scheduleList?.length) scheduleStore.getscheduleList(travelId.value)
  if(!travelStore.travel.startDate) travelStore.getTravel(travelId.value)
})
</script>