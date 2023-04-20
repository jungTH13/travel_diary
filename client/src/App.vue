<template>
  <TheHeader />
  <RouterView />
</template>

<script setup>
import { RouterLink, RouterView, useRouter } from "vue-router";
import TheHeader from "../src/components/TheHeader.vue";
import { useApi } from "./composable/useApi";

const router = useRouter();
const API = useApi();

// # 인터셉터 설정 # //
let reqeustNumber = 0;
API.api.interceptors.request.use(
  function (config) {
    // 요청을 보내기 전에 수행할 일
    reqeustNumber++;

    config.headers["Request-Number"] = `${reqeustNumber}`;
    return config;
  },
  function (error) {
    // 오류 요청을 보내기전 수행할 일
    return Promise.reject(error);
  }
);

let loginIgnore = 0;
API.api.interceptors.response.use(
  function (response) {
    // 응답 데이터를 가공
    const requestNumber = parseInt(response.config.headers["Request-Number"]);
    if (response.data.code === 401 && loginIgnore < requestNumber) {
      loginIgnore = reqeustNumber;

      alert("로그인 페이지로 이동합니다.");

      router.push({ name: "login" });
    }

    return response;
  },
  function (error) {
    // 오류 응답을 처리
    return Promise.reject(error);
  }
);
</script>

<style scoped></style>
