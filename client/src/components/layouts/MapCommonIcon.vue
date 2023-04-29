<template>

<font-awesome-icon style="cursor: pointer;" :class="{checked: (searchMapResult.geometry && searchMapResult.geometry[0] && searchMapResult.geometry[0]),progress: mapVisible}" :style="{width:width,height:height}" icon="fa-regular fa-map" @click="iconClick" />

<div v-if="mapVisible" class="overlay">
    <div class="full" >
        <MapGoogle :search-text="searchText" v-model="searchMapResult" :is-overlay="isOverlay" @cancle="cancle" />
    </div>
</div>


</template>

<style lang="scss" scoped>

.checked{
    color: $green;
}
.progress{
    color: red !important;;
}
.overlay{
    position:absolute;
    z-index: 100000;
    left:0px;
    top:0px;
    width: 100%;
    height:100%;
    background-color: rgba(255, 255, 255, 0.5);
    .full{
        position: relative;
        left: 0;
        top:0px;
        height:100%;
        width:100%;
    }
}
</style>

<script setup>
import { computed, defineProps, onBeforeMount, onMounted, ref, watch } from "vue";
import MapGoogle from "../MapGoogle.vue";

const props = defineProps({modelValue: Object, searchText: String, width:String,height:String})
const emit = defineEmits(["update:modelValue"])

//utils


//contents
const mapVisible = ref(false)
const searchText = computed(()=>props.searchText)
const searchMapResult =  ref(props.modelValue||{})
const width = computed(()=>props.width||'auto')
const height = computed(()=>props.height||'auto')
const isOverlay = ref(false)


const iconClick = async()=>{
    isOverlay.value = true
    mapVisible.value=true
}


const cancle = ()=>{
    mapVisible.value = false
}

watch(()=>props.modelValue,()=>{
    if(searchMapResult.value === props.modelValue) return;
    searchMapResult.value = props.modelValue
})

watch(()=>searchMapResult.value,()=>{
    if(searchMapResult.value === props.modelValue) return;
    cancle()
    emit("update:modelValue",searchMapResult.value)
})

onBeforeMount(()=>{

})

onMounted(async()=>{
    
})

</script>

<script>
// 일반 <script>를 사용하여 옵션 선언
export default {
  inheritAttrs: true
}
</script>