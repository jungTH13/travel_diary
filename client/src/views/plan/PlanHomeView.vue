<template>
  <div id="plan-page">
    <nav class="nav">
      <ul>
        <li v-for="item in nav" v-bind:key="item.name" class="nav-item" :class="{active:item.routeName==tapName}" @click="goPage(item)">
            <div class="item-box">
              <p>{{ item.name }}</p>
            </div>
        </li>
      </ul>
    </nav>
    <router-view />
  </div>
</template>

<style lang="scss" scoped>
#plan-page {

  .nav{
    margin-top:30px;
    margin-bottom: 30px;
  }

  ul {
    display: flex;
    justify-content: space-between;
    height: 60px;
    
    li {
      width: 100%;
      height: 100%;
      cursor: pointer;
      border-bottom: 2px solid rgba(128, 128, 128, 0.486);
      .item-box{
        text-align: center;
        margin-top:15px;
        margin-bottom: 15px;
        p{
          font-size: x-large;
          font-weight: 600;
        }
      }
    }
    .active{
      box-shadow: 1px -2px 2px #444;
      border-radius: 20px 20px 0px 0px;
      border: 2px solid rgba(128, 128, 128, 0.486);
      border-bottom: 0px solid rgba(128, 128, 128, 0.486);
      // background-color: rgba(0, 128, 0, 0.623);
      color: $green;
    }
  }
}
</style>

<script setup>
import { ref, reactive, watch, onMounted, computed, onBeforeUnmount } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useTravelStore } from "../../stores/travel";
import { useScheduleStore } from "../../stores/plan/schedule";

//stores
const route = useRoute();
const router = useRouter();
const travelStore = useTravelStore()
const scheduleStore = useScheduleStore();

//contents
const travelId = computed(()=>route.params.id);
const tapName = computed(()=>route.name.split('-')[0])


const goPage = (navItem)=>{
  router.push({
    name: navItem.routeName,
    params:{
      id : travelId.value
    }
  })
}

const nav = [
  { routeName: 'schedule', name: "일정" },
  { routeName: 'book', name: "예약" },
  { routeName: 'budget', name: "가계부" },
  { routeName: 'checklist', name: "체크리스트" },
];

watch(()=>travelId.value,()=>{
  travelStore.getTravel(travelId.value)
})

onMounted(() => {
  console.log("Mounted!");
  travelStore.getTravel(travelId.value)
  // console.log(route.path);
});

onBeforeUnmount(()=>{
  travelStore.resetTravel
  scheduleStore.resetScheduleList
})

</script>
