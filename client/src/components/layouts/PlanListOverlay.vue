<template>

<div :class="{active:(selectPlan)}" id="overlay" class="col"> 
    <div id="overlay-empty" @click="closePlanOptions"></div>
    
    <div id="plan-options" :class="{active:optionsVisible}">
        <div style="padding:2rem;">
            <div class="select-plan" v-if="selectPlan" >
                <div class="title">{{ selectPlan?.title }} <MapLocationIcon v-if="selectPlanSearchInfo" :is-registration="false" width="3rem" height="3rem" v-model="selectPlanSearchInfo" /></div>
                <div class="type">{{ bookNavCodes[selectPlan?.type] }}</div>
                <div class="memo" v-html="urlParse(selectPlan['memo']?.replaceAll('\n','<br/>'))"></div>
                <div class="images">
                    <BookImageController />
                </div>
            </div>
            

            <div class="summit-footer">
                <button class="font-weight-600 " @click="memoModifyVisible= true;optionsVisible=false;">메모 작성</button>
                <button class="font-weight-600 " :class="{deactive:(!uploadImageList.length && !delBookImageList.length)}" @click="postBookImages" >사진 수정 </button>
            </div>
        </div>
    </div>

    <div id="memo-modify" :class="{active:memoModifyVisible}">
        <div class="full-hidden col" style="padding:2rem;">
            <div class="title">{{ selectPlan?.title }}</div>
            <textarea v-if="selectPlan" class="memo-modify-box" v-model="memo">
            </textarea>
            <div class="summit-footer white-style">
                <button class="font-weight-600 " @click="closePlanOptions">취소</button>
                <button class="font-weight-600 " @click="putPlanMemo(selectPlan)" >저장</button>
            </div>
        </div>
    </div>
</div>

</template>

<style lang="scss" scoped>

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
                max-height: 30vh;
                font-size:1.3rem;
                overflow: auto;
            }

            .images{
                margin-top:1rem;
                .images-view{
                    max-height: 8rem;
                    overflow: hidden;
                }
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

.deactive{
    background-color: gray !important;
    cursor: default !important;
}
</style>

<script setup>
import { computed, onUnmounted, ref, watch } from 'vue';
import { useBookStore } from '../../stores/plan/book';
import { useScheduleStore } from '../../stores/plan/schedule';
import MapLocationIcon from './MapLocationIcon.vue';
import { getMapSearchInfo, urlParse } from '../../composable/util';
import { useRoute } from 'vue-router';
import ImagesUploader from './ImagesUploader.vue';
import ImagesViewer from './ImagesViewer.vue';
import BookImageController from './book/BookImageController.vue';


const props = defineProps({modelValue:Object})
const emits = defineEmits(["update:modelValue"])

const scheduleStore = useScheduleStore()
const bookStore = useBookStore()
const route = useRoute()

const travelId = computed(()=>route.params.id||null)
const selectPlan = computed(()=>props.modelValue)
const bookNavCodes = computed(()=>bookStore.nav.reduce((codes,nav)=>{codes[nav.type]=nav.name;return codes},{}))
const selectPlanSearchInfo = ref(null)
const optionsVisible = ref(false)
const memoModifyVisible = ref(false)
const memo = ref('')
const uploadImageList = computed(()=>bookStore.uploadImageList)
const delBookImageList = computed(()=>bookStore.delImageList)

watch(()=>selectPlan.value,()=>{
    if(!selectPlan.value) return 

    showPlanOptions(selectPlan.value)
})

const showPlanOptions = async(plan)=>{
    await setSelectPlanSearchInfo(plan)
    optionsVisible.value=true
    memoModifyVisible.value = false
    memo.value = plan.memo
}

const closePlanOptions = ()=>{
    scheduleStore.getscheduleList(travelId.value)
    emits('update:modelValue',null)
    selectPlanSearchInfo.value = null
    optionsVisible.value=false
    memoModifyVisible.value = false
    memo.value= ""
    bookStore.resetBook()
}

const setSelectPlanSearchInfo = async(plan)=>{
    await bookStore.getBook(travelId.value,plan.id,plan.type)
    selectPlanSearchInfo.value = getMapSearchInfo(bookStore.book)
}

const postBookImages = async()=>{
    if(!uploadImageList.length && !delBookImageList.length) return
    const response = await bookStore.postBookImages(travelId.value,selectPlan.value.id,selectPlan.value.type,"")
    if(response.code === 200){
        closePlanOptions()
    }
    else{
        alert("이미지 추가에 실패했습니다.")
    }
}

const putPlanMemo = async(plan)=>{
    plan = {...plan,memo:memo.value}
    const response = await scheduleStore.putPlanMemoOnly(travelId.value,plan)

    if(response.code !== 200) alert('수정에 실패 했습니다.')
    closePlanOptions()
    scheduleStore.getscheduleList(travelId.value)
}


onUnmounted(()=>{
    bookStore.resetBook()
})
</script>