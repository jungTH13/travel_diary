<template>
  <div id="plan-page">
    <h1>계획 페이지</h1>
    <nav>
      <ul>
        <li v-for="item in nav" v-bind:key="item.name" class="nav-item">
          <router-link :to="{ path: item.path }">{{ item.name }}</router-link>
        </li>
      </ul>
    </nav>
    <router-view></router-view>
  </div>
</template>

<style lang="scss" scoped>
#plan-page {
  ul {
    display: flex;
    justify-content: space-between;
    li {
      cursor: pointer;
    }
  }
}
</style>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";

const route = useRoute();
const router = useRouter();

const routePath = route.path;
const paramsId = route.params.id;
const nav = [
  { path: `/plan/${paramsId}`, name: "일정" },
  { path: `/plan/${paramsId}/book/all`, name: "예약" },
  { path: `/plan/${paramsId}/budget`, name: "가계부" },
  { path: `/plan/${paramsId}/checklist`, name: "체크리스트" },
];

onMounted(() => {
  console.log("Mounted!");
  // console.log(route.path);
});
</script>
