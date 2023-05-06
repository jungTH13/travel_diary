<template>
<div class="full-hidden col" id="googl-map-component">
    <div class="search-bar">
        <Search  v-model="searchText" @submit="search" class="search" />
        <button  v-if="isOverlay" @click="cancle">{{ "<-" }}</button>
    </div>
    <div class="marker-controll">
        <div class="marker-date-controll">
            <MapMarkerCotroll />
        </div>
    </div>
    <div class="schedule-plan-list">
        <div class="plan-list">
            <SchedulePlanList />
        </div>
    </div>
    <div id="map" style="width:100%; height:120vh; overflow: visible !important;">
    </div>
</div>

</template>

<style lang="scss" scoped>

#map{
    width: 100%;
    height: 100%;
    background-color: white;
}

.search-bar{
    position:relative;
    width:100%;

    .search{
        position:absolute;
        z-index: 1000;
        left:1vw;
        width:80%;
        margin:auto;
        border-radius: 5px;
        
        margin-top:1vw;
        padding:  1px 1px 0px 5px;
        
        box-shadow: 2px 2px 2px 2px rgb(184, 184, 184);
        
        background-color: rgb(255, 255, 255);
    }

    button{
        position:absolute;
        z-index: 1000;
        right:1.5rem;
        top:1.5rem;
        background-color: gray;
        color:white;
        height:3rem;
        width:3rem;
        font-size: 1.8rem;
        border-radius:50%;
        
    }
    // border:2px solid green;
}

.marker-controll{
    position:relative;
    top:6rem;
    width:100%;
    .marker-date-controll{
        // top:6rem;
        right:0;
        width:100%;
        height:0;
        position: absolute;
        z-index: 100000;
        // max-height: 75vh;
        // overflow: hidden;
    }
}

.schedule-plan-list{
    position: relative;
    top:75vh;
    left:0;
    .plan-list{
        position: absolute;
        width:100%;
        height:25vh;
        z-index: 10000;
        overflow: hidden;
    }
}

.firstHeading{
    font-size: larger;
    font-weight: 600;
}
</style>

<script setup>
import { computed, defineProps, onBeforeMount, onMounted, onUnmounted, ref, watch } from "vue";
import {useGoogleMapApi} from "../composable/useGoogleMapApi"
import Search from "./layouts/Search.vue";
import MapMarkerCotroll from "./layouts/MapMarkerCotroll.vue";
import SchedulePlanList from "./SchedulePlanList.vue";

const props = defineProps({
    modelValue: Object,
    searchText: String,
    isSearch: Boolean,
    isOverlay: Boolean,
    isRegistration:Boolean,
});

const emit = defineEmits(['cancle','update:modelValue'])

//utils
const googleMap = useGoogleMapApi()

//contents
const map = ref(null)
const searchText = ref(props.searchText||'')
const searchMarker = ref(null)
const searchInfo = ref(props.modelValue || {})
const infowindow = ref(null)
const isOverlay = computed(()=>props.isOverlay||false)
const isRegistration = computed(()=>props.isRegistration)

const complete = ()=>{
    console.log('complete')
    emit('update:modelValue',searchInfo.value)
}

const cancle = ()=>{
    emit('cancle')
}

const setMarker = (x,y)=>{
    const lat = x || searchInfo.value.geometry[0]
    const lng = y || searchInfo.value.geometry[1]

    if(searchMarker.value !==null) {
        googleMap.moveMarker(searchMarker.value,lat,lng,true)

    }
    else{
        searchMarker.value = googleMap.setMarker(lat,lng,true)
    }

}

const removeMarker = ()=>{
    if(searchMarker.value) {
        googleMap.moveMarker(searchMarker.value,0,0,false)
        searchMarker.value.setMap(null)
        searchMarker.value = null
    }
    if(infowindow.value !==null) infowindow.value.close()
}

const setInfowindow = ()=>{
    const {address,name,cid,geometry} = searchInfo.value
    const content =
    `
    <div id="content">
        <div id="siteNotice">
        </div>
        <h1 id="firstHeading" class="firstHeading" style="font-size: larger; font-weight: 600;">${name?name:''}</h1>
        <div id="bodyContent">
            <p>${address?address:''}</p>
            
            ${!isRegistration.value ?'':`<button onclick="searchComplete()" style="background-color:green; color:white !important; margin:2px; padding:1rem; border-radius: 5px; font-size:1rem;">등록하기</botton>`}
                
                
            ${!cid ?'':`<a style="color:white !important; font-size:1rem;" href="https://maps.google.com/maps?ll=${geometry[0]},${geometry[1]}&z=16&t=m&hl=ko-KR&gl=US&mapclient=embed&cid=${cid?cid:''}">
            <button style="background-color:green; color:white !important; margin:2px; padding:1rem; border-radius: 5px; font-size:1rem;">
                구글 지도
            </botton></a>`}
        </div>
    </div>
    `
    
    if(infowindow.value !==null) infowindow.value.close()
    infowindow.value = googleMap.createInfoWindow(name,content)
    googleMap.removeListeners(searchMarker.value,'click')
    searchMarker.value.addListener("click",()=>{
        if(infowindow.value.getAnchor()){
            infowindow.value.close()
            return
        }
        infowindow.value.open({
            anchor:searchMarker.value,
            map:map
        })
    })
    infowindow.value.open({
        anchor:searchMarker.value,
        map:map
    })
    window.info = infowindow.value
    window.searchComplete = complete
}

const search = async()=>{
    searchInfo.value = await googleMap.searchPlace(searchText.value)

    setMarker()
    setInfowindow()
}

const init = ()=>{
    if(searchInfo.value.geometry && searchInfo.value.geometry[0] && searchInfo.value.geometry[1]){
        if(infowindow.value !==null) infowindow.value.close()
        setMarker()
        setInfowindow()
    }
    else{
        if(searchText.value.length>0) search()
    }
}

watch(()=>props.modelValue,()=>{

})

onMounted(async()=>{
    console.log("MapGoogle Mounted")
    await googleMap.init()
    map.value = await googleMap.getMap(127,36)
    window.map = map.value
    console.log(map.value)
    window.marker = searchMarker

    init()
})

onUnmounted(()=>{
    removeMarker()
    if(document.getElementById('googl-map-component'))document.getElementById('googl-map-component').appendChild(map.value.getDiv())
})

</script>
  