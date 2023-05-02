<template>

<font-awesome-icon style="cursor: pointer;" :class="{checked: (searchMapResult.geometry && searchMapResult.geometry[0] && searchMapResult.geometry[0]),progress: mapVisible}" :style="{width:width,height:height}" icon="fa-solid fa-map-location-dot" @click="iconClick" />

<div v-if="mapVisible" class="overlay">
    <div class="full" >
        <MapGoogle :search-text="searchText" v-model="searchMapResult" :is-overlay="isOverlay" :is-registration="true" @cancle="cancle" />
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
    position:fixed;
    z-index: 100000;
    left:0px;
    top:0px;
    width: 100%;
    height:100%;
    background-color: rgba(255, 255, 255, 0.5);
    .full{
        position: relative;
        left: v-bind(mapLeft);
        top:0px;
        height:100%;
        width:v-bind(mapWidth);
    }
}
</style>

<script setup>
import { computed, defineProps, onBeforeMount, onMounted, ref, watch } from "vue";
import MapGoogle from "../MapGoogle.vue";
import { useCommonStore } from "../../stores/common";

const props = defineProps({modelValue: Object, searchText: String, width:String,height:String})
const emit = defineEmits(["update:modelValue"])


const commonStore = useCommonStore()

//utils


//contents
const mapVisible = ref(false)
const searchText = computed(()=>props.searchText)
const searchMapResult =  ref(props.modelValue||{})
const width = computed(()=>props.width||'auto')
const height = computed(()=>props.height||'auto')
const isOverlay = ref(false)
const subMapVisible = computed(()=>commonStore.mainSubVisible)
const mapLeft = ref('0px')
const mapWidth = ref('100%')

const iconClick = async()=>{
    isOverlay.value = true
    mapVisible.value=true
}


const cancle = ()=>{
    mapVisible.value = false
}

const resizeMap = ()=>{
    if(document.getElementById('main-sub').offsetWidth){
        const mainWidth = document.getElementById('main').offsetWidth
        const targetWidth = document.getElementById('main-sub').offsetWidth
        mapLeft.value = `${mainWidth}px`
        mapWidth.value = `${targetWidth}px`
    }
    else{
        mapLeft.value = '0px'
        mapWidth.value = '100%'
    }

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
    resizeMap()
    window.removeEventListener('resize',resizeMap)
    window.addEventListener('resize',resizeMap)
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