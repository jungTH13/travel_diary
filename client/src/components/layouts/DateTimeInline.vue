<template>
<div @click="visible=!visible">
    <input v-if="!visible" type="text" :value="dateTime?.slice(0,-3)" :placeholder="placeholder" disabled>
    <div v-else>
        <VueDatePicker v-if="!DateUnlimit && travel.id && minDate && maxDate" v-model="dateTime" :min-date="minDate" :max-date="maxDate" inline model-type="yyyy-MM-dd, HH:mm:ss" />
        <VueDatePicker v-else v-model="dateTime" inline model-type="yyyy-MM-dd, HH:mm:ss" />
    </div>
</div>
</template>

<style lang="scss" scoped>
input{
    width:100%;
}
.date-picker-back-ground{
    position: absolute;
    top:0;
    left:0;
    z-index: 1000;
    height: 100%;
    width: 100%;
    background-color: rgba(255, 255, 255, 0.50);
    .date-picker{
        position:absolute;
        z-index: 10000;
        left:50%;
        top:50%;
        transform: translate(-50%, -50%);
    }
}

</style>

<script setup>
import { computed, ref, watch } from "vue";
import { useTravelStore } from "../../stores/travel";

const props = defineProps({modelValue:String, placeholder:String, DateUnlimit:Boolean})
const emit = defineEmits(["update:modelValue"])

const travelStore = useTravelStore()

const dateTime = ref(props.modelValue?.split("T").join(', '))
const placeholder = computed(()=>props.placeholder || '')
const visible = ref(false)
const DateUnlimit = computed(()=>props.DateUnlimit || false)
const travel = computed(()=>travelStore.travel||{})
const minDate = computed(()=>travel.value.startDate?new Date(travel.value.startDate):null)
const maxDate = computed(()=>travel.value.endDate?new Date(travel.value.endDate):null)

watch(()=>dateTime.value,()=>{
    visible.value=false
})

watch(()=>props.modelValue,()=>{
    if(dateTime.value === props.modelValue?.split("T").join(', ')) return;
    dateTime.value = props.modelValue?.split("T").join(', ')
})

watch(()=>dateTime.value,()=>{
    if(dateTime.value === props.modelValue?.split("T").join(', ')) return;
    emit("update:modelValue",dateTime.value.split(", ").join('T'))
    visible.value = false
})


</script>
                