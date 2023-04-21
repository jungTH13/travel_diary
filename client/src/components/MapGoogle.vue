<template>
<div class="full-hidden">
    <div class="search-bar">
        <Search  v-model="searchText" @submit="search" class="search" />
    </div>
    <div id="map" style="width:100%; height:100%; overflow: visible !important;">
    </div>
</div>

</template>

<style lang="scss" scoped>

#map{
    width: 100%;
    height: 100%;
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
    // border:2px solid green;
}

.firstHeading{
    font-size: larger;
    font-weight: 600;
}
</style>

<script setup>
import { defineProps, onBeforeMount, onMounted, ref } from "vue";
import {useGoogleMapApi} from "../composable/useGoogleMapApi"
import Search from "./layouts/Search.vue";

const props = defineProps({
    searchText: String,
    isSearch: Boolean
});

const emit = defineEmits(['searchInfo'])

//utils
const googleMap = useGoogleMapApi()

//contents
const map = ref(null)
const searchText = ref(props.searchText)
const searchMarker = ref(null)
const searchInfo = ref({})
const infowindow = ref(null)

const complete = ()=>{
    emit('searchInfo',searchInfo.value)
}

const setMarker = ()=>{
    const lat = searchInfo.value.geometry[0]
    const lng = searchInfo.value.geometry[1]

    if(searchMarker.value !==null) {
        googleMap.moveMarker(searchMarker.value,lat,lng,true)

    }
    else{
        searchMarker.value = googleMap.setMarker(lat,lng,true)
    }
}

const setInfowindow = ()=>{
    const {address,name,cid,geometry} = searchInfo.value
    const content =
    '<div id="content">' +
    '<div id="siteNotice">' +
    "</div>" +
    `<h1 id="firstHeading" class="firstHeading" style="font-size: larger; font-weight: 600;">${name}</h1>` +
    '<div id="bodyContent">' +
    `<p>${address} </br>` +
    `<a href="https://maps.google.com/maps?ll=${geometry[0]},${geometry[1]}&z=16&t=m&hl=ko-KR&gl=US&mapclient=embed&cid=${cid}">구글 지도에서 보기</a> ` +
    "</p>" +
    "</div>" +
    "</div>";
    
    if(infowindow.value !==null) infowindow.value.close()
    infowindow.value = googleMap.createInfoWindow(name,content)
    googleMap.removeListeners(searchMarker.value,'click')
    searchMarker.value.addListener("click",()=>{
        infowindow.value.open({
            anchor:searchMarker.value,
            map:map
        })
    })
    infowindow.value.open({
        anchor:searchMarker.value,
        map:map
    })
}

const search = async()=>{
    searchInfo.value = await googleMap.searchPlace(searchText.value)

    setMarker()
    setInfowindow()
}





onMounted(async()=>{
    await googleMap.init()
    map.value = await googleMap.getMap(127,36)
    window.map = map.value
    console.log(map.value)
    window.marker = searchMarker
    // setInterval(()=>{
    //     console.log(document.getElementsByClassName("place-card"))
    //     console.log(document.getElementById("mapDiv"));
    // },1000)
})

</script>
  