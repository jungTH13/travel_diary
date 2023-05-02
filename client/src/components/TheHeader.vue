<template>
  <div id="header" class="header">
    <div v-if="['home','new'].includes(rootName)" class="banner">
      <router-link to="/" >TRAVEL DIARY</router-link>
    </div>
    <div v-else-if="pathList[0]==='travel'" class="banner" @click="active=!active">
      <div v-if="!active" id="plan-title">{{ travel.title }}</div>
      <div v-else id="plan-title">test</div>
    </div>
    <div style="width: 100%;">

    </div>

    <div v-if="rootName==='home'" class="icon-box row">
      <font-awesome-icon icon="fa-solid fa-bars" class="icon" />
    </div>
    <div v-else-if="rootName==='new'" class="icon-box row">
      <font-awesome-icon icon="fa-solid fa-arrow-left" class="icon" @click="goBack" />
    </div>
    <div v-else class="icon-box row">
      <font-awesome-icon icon="fa-solid fa-house" class="icon" @click="goHome"/>
      <div class="icon"><MapCommonIcon width="2.8rem" height="2.8rem" /></div>      
      <font-awesome-icon icon="fa-solid fa-bars" class="icon" />
    </div>
  </div>
</template>

<style lang="scss" scoped>

 
#header {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content:left;
  align-items: center;
  background-color: white;
  padding-left: 1rem;
  padding-right: 1rem;

  a {
    font-size: 2.5rem;
    font-weight: bold;
    letter-spacing: 0.2vh;
    color: $green !important;
  }
  #plan-title{
    font-size: 2.5rem;
    font-weight: bold;
    letter-spacing: 0.2vh;
    // color: $green !important;
  }
}

.banner{
  position: absolute;
  cursor: pointer;
}

.icon-box{
  justify-content:right;
  margin:auto;
  // color: gray;

  .icon{
    width: 2.8rem;
    height:2.8rem;
    margin-left:2rem;
  }
}


</style>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import MapCommonIcon from './layouts/MapCommonIcon.vue';
import { computed, ref, watch } from 'vue';
import { useTravelStore } from '../stores/travel';

const route = useRoute()
const router = useRouter()
const travelStore =useTravelStore()

const componentName = computed(()=>route.name||'')
const pathList = computed(()=>route.path.split('/').slice(1)||'')
const rootName = computed(()=>componentName.value.split('-')[0])

const travel = computed(()=>travelStore.travel)
const active = ref(false)

const goBack = ()=>{
  router.go(-1)
}

const goHome = ()=>{
  router.push({name:'home'})
}

watch(()=>route.name,()=>active.value=false)

</script>
