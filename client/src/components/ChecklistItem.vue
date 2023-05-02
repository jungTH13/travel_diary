<template>
<div style="margin-bottom: 1rem;" class="checklistItem-container">
    <table>

        <tbody>
            <tr v-if="!checklist.detailList || !checklist.detailList.length"><p>확인 사항 추가해주세요.</p></tr>
            <tr v-for="detail in checklist.detailList">
                <td style="width:100%;">
                    <label>
                        <div class='content row'>
                            <font-awesome-icon v-if="!detail.checked" icon="fa-regular fa-square" class="icon" />
                            <font-awesome-icon v-else icon="fa-regular fa-square-check" class="icon" />
                            
                            <input type="checkbox" v-model="detail.checked">
                            <span class="title" v-if="!editState">{{ detail['detail'] }}</span>
                            <input type="text" v-else class="title" v-model="detail['detail']">
                            
                        </div>
                    </label>
                </td>
                <td>
                    <div v-if="editState" class='icon-content' @click="delChecklistDetail(detail)">
                        <font-awesome-icon icon="fa-solid fa-trash" class="icon" />
                    </div>
                </td>
            </tr>
            <tr v-if="editState">
                <td style="width:100%; cursor: pointer;" @click="addChecklistDetail">
                    <div class="full-hidden row">
                        <div class="content-add">
                            <font-awesome-icon icon="fa-solid fa-circle-plus" class="icon" />
                        </div>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
    <div class="summit-footer white-style">
        <button v-if="editState" class="font-weight-600" @click="delChecklist">체크리스트 삭제</button>
        <button v-if="(checklist.detailList && checklist.detailList.length) || editState" class="font-weight-600" @click="putChecklist">{{editState?'수정하기':'저장하기'}}</button>
    </div>
</div>
</template>

<style lang="scss" scoped>

.checklistItem-container{
    transition: 2s;
    max-height: 500px;
}

table{
    width:100%;
    // margin-bottom: 1rem;
    border-collapse: separate;
    border-spacing: 0 10px;
    padding: 0 0rem 0 1rem;


    th{
        width:33%;
    }

    tr{
        display: flex;
        flex-direction: row;
        // padding-left: 1rem;
        // padding-right:1rem;
    }

    td{
        width:auto;
        font-size: 1.4rem;
        padding-top: 1rem;
        padding-bottom: 0.2rem;
        border-bottom: 1px solid $gray;


        div{
            &.content-add{
                margin:auto;
            }

            input{
                display: none;
            }

            label{
                cursor: pointer;
                .icon{
                width: 1.8rem;
                height:1.8rem;
                color: $green;
            }
            }

            input,span{
                left:auto;
                &.title{
                    margin-left: 1rem;
                    font-size:1.5rem;
                    margin-right:1.5rem;
                    display: block;
                    width: 100%;
                }

                
                &.category{
            
                    color:gray;
                    font-size:1.2rem;
                }
                &.payment{
                    float: right;
                    font-size: 1.8rem;
                }
                
            }
            .icon{
                width: 1.8rem;
                height: 1.8rem;
                color:gray;
                cursor: pointer;
                
                &.margin-right{
                    margin-right:0.3rem;
                }
            }
            .icon:hover{
                color:black;
            }
        }
        
    }
}
</style>

<script setup>

import { computed, defineProps, onBeforeMount, onMounted, onUnmounted, ref, watch } from "vue";
import {toAMPMString, toComaNumberString} from "../composable/util"
import { useRoute, useRouter } from "vue-router";
import { useTravelStore } from "../stores/travel";
import { useChecklistStore } from "../stores/plan/checklist";

const props = defineProps({
    modelValue: Number,
    editState: Boolean
});

const emits = defineEmits(['isUpdated','rendering'])

const checklistStore = useChecklistStore()
const route = useRoute()


const checklistTitleId = computed(()=>props.modelValue)
const travelId = computed(()=>route.params.id)
const checklist = ref({})
const editState = computed(()=>props.editState)
const checklistHeight = ref(0)

const setChecklistHeight = ()=>{
    checklistHeight = document
}

const resetChecklist = ()=>{
    checklist.value = {}
}

const init = async()=>{
    checklist.value = await checklistStore.getChecklist(travelId.value,checklistTitleId.value)
    emits('rendering',true)
}

const delChecklistDetail = (targetDetail)=>{
    checklist.value.detailList = checklist.value.detailList.filter((detail)=>targetDetail !== detail)
}

const addChecklistDetail = ()=>{
    checklist.value.detailList.push({
        detail:"",
        checked: false,
    })
}

const pretreatment = ()=>{
    checklist.value.detailList = checklist.value.detailList.filter(detail=>detail.detail !== '')
}

const putChecklist = async()=>{
    pretreatment()

    const response = await checklistStore.putChecklist(travelId.value,checklist.value)
    if(response.code !== 200) alert('등록에 실패 했습니다.')
    else {
        checklistStore.resetChecklist()
        checklistStore.getChecklistList(travelId.value)
        emits('isUpdated')
        resetChecklist()
        init()
    }
}

const delChecklist = async()=>{
    const response = await checklistStore.delChecklist(travelId.value,checklist.value.id)
    if(response.code !== 200) alert('삭제에 실패 했습니다.')
    emits('isUpdated')
    checklistStore.getChecklistList(travelId.value)
}

watch(()=>checklistTitleId.value,()=>{
    init()
})

watch(()=>editState.value,()=>{
    init()
})

onMounted(()=>{
    init()
})

onUnmounted(()=>{
    emits('rendering',false)
})

</script>
  