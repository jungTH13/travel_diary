<template>
<div>
    <RouterView />
</div>
</template>

<style lang="scss" scoped></style>

<script setup>
import { RouterView } from 'vue-router';
import { useTravelStore } from '../../stores/travel';
import { onBeforeUnmount } from 'vue';

const travelStore = useTravelStore()

onBeforeUnmount(()=>{
    travelStore.resetTravel();
})

</script>
  