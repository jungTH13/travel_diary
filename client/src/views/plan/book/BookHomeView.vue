<template>
  <div id="plan-book">
    <div>예약리스트</div>
    <ul>
      <li
        v-for="item in nav"
        v-bind:key="item.name"
        @click="() => getTabMenu(item.name, item.path)"
        :class="{ active: item.path === tabMenu ? true : false }"
      >
        {{ item.name }}
        <!-- <router-link :to="{ path: item.path }">{{ item.name }}</router-link> -->
      </li>
    </ul>
    <router-view></router-view>
  </div>
</template>

<style lang="scss" scoped>
#plan-book {
  ul {
    li {
      cursor: pointer;
      font-size: 32px;
      &.active {
        color: red;
      }
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
const tabMenu = ref("");

const routePath = route.path;
const paramsId = route.params.id;
const nav = [
  { path: `/plan/${paramsId}/book/all`, name: "전체" },
  { path: `/plan/${paramsId}/book/flight`, name: "항공권" },
  { path: `/plan/${paramsId}/book/hotel`, name: "호텔" },
  { path: `/plan/${paramsId}/book/food`, name: "음식점" },
  { path: `/plan/${paramsId}/book/transport`, name: "교통" },
  { path: `/plan/${paramsId}/book/other`, name: "기타" },
];

const getTabMenu = (tab, path) => {
  tabMenu.value = path;
  const pathName = path.split("/").at(-1);
  router.push({ name: `book-${pathName}` });
};

onMounted(() => {
  console.log("Mounted!");
  tabMenu.value = route.path;
});
</script>
