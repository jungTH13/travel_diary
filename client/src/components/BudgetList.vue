<template>
<div>
    <table>
        <tbody>
            <tr v-for="budget in budgetList" @click="goDetailPage(budget)">
                <td style="width:60%;">
                    <div class='content'>
                        <span class="title">{{ budget['title'] }}</span>
                        <span class="category">{{ budget['categoryType'] }}</span>
                    </div>
                </td>
                <td style="width:40%;">
                    <div class='content'>
                        
                        <span class="payment" :style="{color: budget['amountOfPayment']>=0?'blue':'red'}">
                            <font-awesome-icon  icon="fa-solid fa-won-sign" class="icon margin-right" />
                            {{ toComaNumberString(budget['amountOfPayment']) }}
                        </span>
                    </div>
                </td>
            </tr>
        </tbody>
    </table>
</div>
</template>

<style lang="scss" scoped>
table{
    width:100%;
    margin-bottom: 1rem;
    border-collapse: separate;
    border-spacing: 0 10px;
    padding: 0 0rem 0 2rem;

    th{
        width:33%;
    }

    tr{
        cursor: pointer;
        display: flex;
        flex-direction: row;
        padding-left: 1rem;
        // padding-right:1rem;
    }

    td{
        width:auto;
        font-size: 1.4rem;
        padding-top: 0.8rem;
        padding-bottom: 0.2rem;
        border-bottom: 1px solid $gray;
        div{
            width:100%;

            span{
                left:auto;
                &.title{
    
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
            .icon{
                width: 1.7rem;
                height: 1.7rem;
                color:black;
                
                &.margin-right{
                    margin-right:0.3rem;
                }
            }
        }
        
    }
}
</style>

<script setup>

import { computed, defineProps, ref, watch } from "vue";
import {toAMPMString, toComaNumberString} from "../composable/util"
import { useRouter } from "vue-router";
import { useTravelStore } from "../stores/travel";

const props = defineProps({
modelValue: Array,
});
const emit = defineEmits(['update:modelValue'])

const router = useRouter()
const travelStore = useTravelStore()

const budgetList = computed(()=>props.modelValue||[])
const travelId = computed(()=>travelStore.travel.id)

const goDetailPage = (budget)=>{
  router.push({
    name:'budget-detail',
    params:{
      id : travelId.value,
      budgetId : budget.id,
    }
  })
}

</script>
  