<template>
<div class="full-hidden">
    <RouterView />
</div>
</template>

<style lang="scss"></style>

<script setup>
import { RouterView } from 'vue-router';
import { useTravelStore } from '../../stores/travel';
import { onBeforeUnmount } from 'vue';

const travelStore = useTravelStore()

onBeforeUnmount(()=>{
    travelStore.resetTravel();
})

</script>
  