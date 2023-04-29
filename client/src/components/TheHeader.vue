<template>
  <div id="header" class="header">
    <div class="banner">
      <router-link to="/" >TRAVEL</router-link>
    </div>
    <div style="width: 100%;">

    </div>
    <div class="icon">
      <MapCommonIcon />
    </div>
    
    
  </div>
</template>

<style lang="scss" scoped>

 
.banner{
  position: absolute;
}

.icon{
    margin-top:0.3rem;
    width: 2.5rem;
    height:2.5rem;
  }


</style>

<script setup>
import MapCommonIcon from './layouts/MapCommonIcon.vue';

</script>
