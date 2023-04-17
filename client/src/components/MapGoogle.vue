<template>
<div id="map" style="width:800px; height:1000px;">
    
</div>
</template>

<style lang="scss">

</style>

<script setup>
import { defineProps, onBeforeMount, onMounted, ref } from "vue";
import {useGoogleMapApi} from "../composable/useGoogleMapApi"

const props = defineProps({
    searchText: String,
    isSearch: Boolean
});

const googleMap = useGoogleMapApi()

const map = ref(null)

onMounted(async()=>{
    await googleMap.init()
    map.value = await googleMap.getMap(127,36)
    window.map = map.value
    console.log(map.value)
})

</script>
  