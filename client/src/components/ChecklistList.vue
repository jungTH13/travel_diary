<template>
<div>
    <table>
        <tbody>
            <tr v-for="checklist in checklistList">
                <td style="width:100%;">
                    <div class="contents row">
                        <div class='contents-title row' @click="()=>{panels[checklist.id]= !panels[checklist.id];panelsEdit[checklist.id] = false }">
                            <span class="title">{{ checklist['title'] }}</span>
                            <span class="category">{{ checklist['categoryType'] }}</span>
                            
                        </div>
                        <div v-if="panels[checklist.id]" class="contents-icon">
                            <font-awesome-icon icon="fa-solid fa-gear" class="icon margin-right curser" :class="{active:panelsEdit[checklist.id]}" @click="()=>{panelsEdit[checklist.id] = !panelsEdit[checklist.id]}" />
                        </div>
                        <div v-else class="contents-icon">
                            <font-awesome-icon icon="fa-solid fa-angle-down" class="icon margin-right" />
                        </div>
                    </div>
                    <div class="checklist-item-expense" :class="{max : rendering[checklist.id]}">
                        <div>
                            <ChecklistItem v-if="panels[checklist.id]" id="content-checklist-detail" v-model="checklist.id" :edit-state="panelsEdit[checklist.id]" @is-updated="()=>{panelsEdit[checklist.id]= false; }" @rendering="(state)=>rendering[checklist.id]=state" />
                        </div>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</template>

<style lang="scss" scoped>

.checklist-item-expense{
    transition: all ease 1s 0s;
    overflow: hidden;
    max-height: 0px;
}
.max{
    max-height: 2000px;
}

table{
    width:100%;
    margin-bottom: 1rem;
    border-collapse: separate;
    border-spacing: 0 10px;
    // padding: 0 0rem 0 2rem;

    th{
        width:33%;
    }

    tr{
        
        display: flex;
        flex-direction: row;
        padding-left: 1rem;
        // padding-right:1rem;
    }

    td{
        width:auto;
        font-size: 1.4rem;
        padding-top: 0.8rem;
        padding-bottom: 0rem;
        border-bottom: 1px solid $gray;
        
        div{
            border:0px;
            box-shadow: none;

            &.contents{
                width: 100%;
                
                .contents-title{
                    width:100%;
                    cursor: pointer;

                    span{
                        &.title{
                            color:black;
                            font-size:2rem;
                            margin-right:1.5rem;
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
                }

                .contents-icon{
                    .icon{
                        // position:absolute;
                        // right:1rem;
                        // z-index: 100000;
                        width: 1.7rem;
                        height: 1.7rem;
                        padding:0.5rem;
                        color:gray;
                        
                        &.margin-right{
                            
                            margin-right:1rem;
                        }

                        &.curser{
                            cursor: pointer;
                        }
                    }
                    .icon:hover{
                        color: black;
                    }

                    .active{
                        color:$green;
                    }
                }
                
                
            }
        }
        
    }
}
</style>

<script setup>

import { computed, defineProps, ref, watch } from "vue";
import ChecklistItem from "./ChecklistItem.vue";

const props = defineProps({
modelValue: Array,
});
const emit = defineEmits(['update:modelValue'])

const checklistList = computed(()=>props.modelValue||[])
const panels = ref({})
const panelsEdit = ref({}) 
const rendering = ref({})

watch(()=>props.modelValue,()=>{
  panelsEdit.value ={}
})

</script>
  