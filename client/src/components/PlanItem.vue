<template>
<div>
    <div class="plan-contents-wrapper">
        <PlanAirport v-if="plan.type==='pa'" v-model="plan" />
        <PlanHotel v-if="plan.type === 'ph'" v-model="plan" />
        <PlanRestaurant v-if="plan.type === 'pr'" v-model="plan" />
        <PlanTransport v-if="plan.type === 'pt'" v-model="plan" />
        <PlanEtc v-if="plan.type === 'pe'" v-model="plan" />
    </div>
</div>
</template>

<style lang="scss" scoped>
.plan-contents-wrapper{

}

</style>

<script setup>

import { computed, ref, watch } from "vue";
import { toAMPMString } from "../composable/util";
import PlanAirport from "./layouts/plan/PlanAirport.vue";
import PlanHotel from "./layouts/plan/PlanHotel.vue";
import PlanRestaurant from "./layouts/plan/PlanRestaurant.vue";
import PlanTransport from "./layouts/plan/PlanTransport.vue";
import PlanEtc from "./layouts/plan/PlanEtc.vue";

const props = defineProps({
    modelValue: Object,
});
const emit = defineEmits(['update:modelValue'])

const plan = computed(()=>props.modelValue||{})

const dateString = (plan)=>{
    
}

</script>
      