<template>
<div @click="visible=!visible">
    <input type="text" :value="dateTime?.split(', ')[1].slice(0,-3)" :placeholder="placeholder" disabled>
    <div v-if="visible" class="date-picker-back-ground"> 
        <div class="date-picker">
            <v-overlay v-model="visible" contained class="align-center justify-center">
                <v-card class="pa-2">
                    <VueDatePicker v-model="dateTime" inline time-picker model-type="yyyy-MM-dd, hh:mm:ss" />
                </v-card>
            </v-overlay>
        </div>
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

const props = defineProps({modelValue:String, placeholder:String})
const emit = defineEmits(["update:modelValue"])

const dateTime = ref(props.modelValue?.split("T").join(', '))
const visible = ref(false)

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
                