<template>
  <div class="main" id="main">
    <div class="head">
      <TheHeader />
    </div>
    <div class="body">
      <RouterView />
    </div>
  </div>

  <div class="main-sub" id="main-sub">
    <MapGoogle v-if="mainSubVisible" />
  </div>
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from "vue-router";
import TheHeader from "../src/components/TheHeader.vue";
import * as API from "./composable/api";
import MapGoogle from "./components/MapGoogle.vue";
import { computed, onMounted, ref } from "vue";
import { useCommonStore } from "./stores/common";


const router = useRouter();
const commonStore = useCommonStore()

// # 인터셉터 설정 # //
let reqeustNumber = 0;
API.api.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 수행할 일
    reqeustNumber++;

    config.headers['Request-Number'] = `${reqeustNumber}`;
    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    return Promise.reject(error);
  });

let loginIgnore = 0;
API.api.interceptors.response.use(
  function (response) {
    // 응답 데이터를 가공
    const requestNumber = parseInt(response.config.headers['Request-Number']);
    if(response.data.code === 401 && loginIgnore < requestNumber){
      
      loginIgnore = reqeustNumber;
      
      alert('로그인 페이지로 이동합니다.')

      router.push({name:'login'})
    }
    if(response.data.code !== 200) console.log(response)

    return response;
  },
  function (error) {
    // 오류 응답을 처리
    return Promise.reject(error);
});

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
  flex-direction: column;
  padding: 0 0 1.2rem 0;

  .head{
    height: 3rem;
    width: 100%;
    padding: 0 1.2rem 0 1.2rem;
  }

  .body{
    flex: 1;
    width: 100%;
    padding: 0 1.2rem 0 1.2rem;
    display:flex;
    flex-direction: column;
    overflow: hidden;
  }
}
.main-sub{
  display: none;
}



</style>
