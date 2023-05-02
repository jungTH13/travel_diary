<template>
<div class="full-hidden">
    <RouterView />
</div>
</template>

<style lang="scss"></style>

<script setup>
import { RouterView, useRoute } from 'vue-router';
import { useTravelStore } from '../../stores/travel';
import { onBeforeMount, onBeforeUnmount } from 'vue';

const travelStore = useTravelStore()
const route = useRoute()

onBeforeMount(()=>{
    if(route.params.id)travelStore.getTravel(route.params.id)
})

onBeforeUnmount(()=>{
    travelStore.resetTravel();
})

</script>
  