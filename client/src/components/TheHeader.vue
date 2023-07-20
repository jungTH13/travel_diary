<template>
  <div id="header" class="header">
    <div v-if="['login'].includes(rootName)"></div>
    <div v-if="['home','new'].includes(rootName)" class="banner">
      <router-link to="/" >TRAVEL DIARY</router-link>
    </div>
    <div v-else-if="pathList[0]==='travel'" class="banner" @click="active=!active">
      <div v-if="!active" id="plan-title">{{ travel.title }}</div>
      <div v-else id="plan-title">test</div>
    </div>
    <div style="width: 100%;">

    </div>
    <div v-if="['login'].includes(rootName)"></div>
    <div v-else-if="['home'].includes(rootName)" class="icon-box row">
      <font-awesome-icon icon="fa-solid fa-bars" class="icon" @click="sideUserInfoActive=!sideUserInfoActive" />
    </div>
    <div v-else-if="rootName==='new'" class="icon-box row">
      <font-awesome-icon icon="fa-solid fa-arrow-left" class="icon" @click="goBack" />
    </div>
    <div v-else class="icon-box row">
      <font-awesome-icon icon="fa-solid fa-house" class="icon" @click="goHome"/>
      <div class="icon"><MapCommonIcon width="2.8rem" height="2.8rem" /></div>      
      <font-awesome-icon icon="fa-solid fa-bars" class="icon" @click="sideUserInfoActive=!sideUserInfoActive" />
    </div>
  </div>


  <div id="side-user-info" class="row" :class="{active:sideUserInfoActive}">
    <div class="empty-space" @click="sideUserInfoActive=!sideUserInfoActive"></div>
    <div id="user-info-container">
      <div class="header">
        <div class="close">
          <font-awesome-icon icon="fa-solid fa-x" class="icon" @click="sideUserInfoActive=!sideUserInfoActive"/>
        </div>
        <div class="options">
          <font-awesome-icon icon="fa-solid fa-arrow-right-from-bracket" class="icon" @click="logout"/>
        </div>
      </div>
      <div class="contents">
        <div class="user-profil col">
          <div class="user col">
            <img :src="userPicture" class="icon">
            <div class="user-email">{{ userEmail?userEmail:"" }}</div>
          </div>
        </div>
      </div>
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
    cursor: pointer;
  }
}

#side-user-info{
  background-color: rgba(0, 0, 0, 0.3);
  
  position: absolute;
  overflow: hidden;
  z-index: 50000;
  right: 0;
  top:0;
  height: 100%;
  width: 0%;
  transition: all ease 0.5s 0s;

  .empty-space{
    height: 100%;
    width: 10%;
  }

  #user-info-container{
    background-color: white;
    // box-shadow: 0 0 5px 5px gray;
    width:90%;
    height: 100%;

    .header{
      height: 4.5rem;

      .close{
        padding-left: 1rem;
        height: 100%;
        display: flex;
        float: left;
        .icon{
          margin-top: auto;
          margin-bottom: auto;
          cursor: pointer;
          height:3rem;
          width: 3rem;
        }
      }
      

      .options{
        height: 100%;
        display: flex;
        float:right;
        padding-right:1rem;
        

        .icon{
          margin-top: auto;
          margin-bottom: auto;
          cursor: pointer;
          height:3rem;
          width: 3rem;
          float:right;
        }
      }
    }

    .contents{
      height: 100%;
      .user-profil{
        width: 100%;
        height:40rem;
        justify-content: center;

        .user{
          margin:auto;
          display: flex;
          .icon{
            margin:auto;
            height: 10rem;
            width: 10rem;
            border-radius: 50%;
            margin: auto;
            margin-bottom: 2rem;
          }

          .user-email{
            margin:auto;
            border-radius: 25px;
            padding:0.5rem 1rem 0.5rem 1rem;
            box-shadow: 0 0 2px 2px gray;
            font-size:1.5rem;
            font-weight: 600;
          }
        }
        
        
      }
    }
  }

  &.active{
    width:100%;
  }

}

</style>

<script setup>
import { useRoute, useRouter } from 'vue-router';
import MapCommonIcon from './layouts/MapCommonIcon.vue';
import { computed, ref, watch } from 'vue';
import { useTravelStore } from '../stores/travel';
import { useUserStore } from '../stores/user';

const route = useRoute()
const router = useRouter()
const travelStore =useTravelStore()
const userStore = useUserStore()

const componentName = computed(()=>route.name||'')
const pathList = computed(()=>route.path.split('/').slice(1)||'')
const rootName = computed(()=>componentName.value.split('-')[0])

const travel = computed(()=>travelStore.travel)
const active = ref(false)
const sideUserInfoActive = ref(false)
const userPicture = computed(()=>userStore.userInfo.picture)
const userEmail = computed(()=>userStore.userInfo.email)

const logout = async()=>{
  const response = await userStore.logout()
  
  if(response.code === 200){
    sideUserInfoActive.value = false
    userStore.resetUserInfo()
    router.push({
      name:'login'
    })
  }
  else{
    alert("로그아웃에 실패했습니다.")
  }
}

const goBack = ()=>{
  router.go(-1)
}

const goHome = ()=>{
  router.push({name:'home'})
}

watch(()=>sideUserInfoActive.value,async(newValue)=>{
  if(newValue === true){
    userStore.getUserInfo()
  }
})

watch(()=>route.name,()=>active.value=false)

</script>
