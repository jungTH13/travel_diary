<template>
  <div class="main" id="main">
    <div class="head">
      <TheHeader />
    </div>
    <div class="body">
      <RouterView />
    </div>
    <div class="progress-circular-container" v-if="progressCount">
      <div class="progress-circular-box">
        <v-progress-circular
          indeterminate
          color="green"
        >
        </v-progress-circular>
        {{ `서버 응답 수신 중(${progressCount})` }}
      </div>
    </div>
  </div>

  <div v-if="visible" class="main-sub" id="main-sub" >
    <MapGoogle v-if="mainSubVisible" />
  </div>
</template>

<script setup>
import { RouterLink, RouterView, useRoute, useRouter } from "vue-router";
import TheHeader from "../src/components/TheHeader.vue";
import * as API from "./composable/api";
import MapGoogle from "./components/MapGoogle.vue";
import { computed, onMounted, ref, watch } from "vue";
import { useCommonStore } from "./stores/common";


const router = useRouter();
const route = useRoute()
const commonStore = useCommonStore()

const visible = ref(false)

// # 인터셉터 설정 # //
let reqeustNumber = 0;
const progressCount = ref(0)
API.api.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 수행할 일
    reqeustNumber++;

    config.headers['Request-Number'] = `${reqeustNumber}`;
    progressCount.value++;
    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    progressCount.value--;
    return Promise.reject(error);
  });

let loginIgnore = 0;
API.api.interceptors.response.use(
  function (response) {
    // 응답 데이터를 가공
    progressCount.value--;
    const requestNumber = parseInt(response.config.headers['Request-Number']);
    if(response.data.code === 401 && loginIgnore < requestNumber){
      if(response.request.responseURL.includes('user/userInfo')) return response
      
      loginIgnore = reqeustNumber;
      
      alert('로그인 페이지로 이동합니다.')

      router.push({
        name:'login',
        params:{
          code: 401
        }
      })
    }
    if(response.data.code !== 200) console.log(response)
    return response;
  },
  function (error) {
    // 오류 응답을 처리
    progressCount.value--;
    return Promise.reject(error);
});

watch(()=>route.name,()=>{
  if(route.name && !route.name.includes('login')){
    visible.value = true
    setTimeout(commonStore.checkMainSub,0) 
  }
  else{
    visible.value = false
  }
})

const mainSubVisible= computed(()=>commonStore.mainSubVisible)


onMounted(()=>{
  commonStore.initMainSubLisner()
})


</script>

<style lang="scss">
#app{
  height:100%;
  width: 100%;
  margin: 0 auto;
  overflow: hidden;
}

.main{
  height: 100%; 
  width: 100%; 
  margin:auto;
  min-width: 300px;
  max-width: 720px;
  display: flex; 
  overflow: hidden;
  flex-direction: column;
  padding: 0 0 1.2rem 0;
  position: relative;

  .head{
    height: 4.5rem;
    width: 100%;
    // padding: 0 1.2rem 0 1.2rem;
  }

  .body{
    flex: 1;
    width: 100%;
    padding: 0 1.2rem 0 1.2rem;
    display:flex;
    flex-direction: column;
    overflow: hidden;
    transition: all ease 1.5s 0s;
  }

  .progress-circular-container{
    position: absolute;
    z-index: 200000;
    left:0;
    top:0;
    width:100%;
    height:100%;
    overflow: hidden;
    display: flex;

    .progress-circular-box{
      margin:auto;
      font-weight: 600;
      font-size: 1.5rem;
      text-shadow: -1px 0 rgb(255, 255, 255), 0 1px rgb(255, 255, 255), 1px 0 rgb(255, 255, 255), 0 -1px rgb(255, 255, 255);
    }
  }
}
.main-sub{
  display: none;
}
.disable{
  width:0% !important;
  transition: all ease 1s 1s;
}


</style>
