<template>
  <div id="plan-page">
    <nav class="nav">
      <ul>
        <li v-for="item in nav" v-bind:key="item.name" class="nav-item" @click="goPage(item)">
            <div class="item-box">
              <p>{{ item.name }}</p>
            </div>
            <div class="deactive" :class="{active:item.routeName==tapName}"></div>
        </li>
      </ul>
    </nav>
    <div v-if="contentsLoaded" class="contents">
      <router-view />
    </div>
  </div>
</template>

<style lang="scss" scoped>
#plan-page {
  height: 100%;
  display: flex;
  flex-direction: column;
  margin-right:0.2rem;
  .nav{
    margin-top:2rem;
    padding-bottom: 0.2rem;
    margin-bottom: 0.5rem;
  }
  .contents{
    box-shadow: inset 0rem 1rem 0.5rem 0rem rgb(255, 255, 255);
    flex:1;
    height: 100%;
    overflow: hidden;
  }

  ul {
    display: flex;
    justify-content: space-between;
    height: 3.5rem;
    
    li {
      width: 100%;
      height: 100%;
      cursor: pointer;
      // border-bottom: 1px solid rgba(128, 128, 128, 0.37);
      .item-box{
        text-align: center;
        padding-top:0.5rem;

        p{
          font-size: 1.8rem;
          font-weight: 600;
        }
      }
    }

    .deactive{
      width: 0%;
      border-bottom: 0.3rem solid $green;
      color: $green;
      transition:all ease 0.5s 0s;
      margin:auto;
    }
    .active{
      // box-shadow: 1px -2px 2px #444;
      // border-radius: 10px 10px 0px 0px;
      // border: 2px solid rgba(128, 128, 128, 0.486);
      // background-color: rgba(0, 128, 0, 0.623);
      border-bottom: 0.3rem solid $green;
      width:100%;
    }
  }
}
</style>

<script setup>
import { ref, reactive, watch, onMounted, computed, onBeforeUnmount, onBeforeMount } from "vue";
import { useRouter, useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useTravelStore } from "../../stores/travel";
import { useScheduleStore } from "../../stores/plan/schedule";
import { useMapStore } from "../../stores/map";

//stores
const route = useRoute();
const router = useRouter();
const travelStore = useTravelStore()
const scheduleStore = useScheduleStore();
const mapStore = useMapStore()

//contents
const travelId = computed(()=>route.params.id);
const tapName = computed(()=>route.name.split('-')[0])
const contentsLoaded = ref(false)

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

watch(()=>travelId.value,async()=>{
  contentsLoaded.value = false
  await travelStore.getTravel(travelId.value)

  contentsLoaded.value = true
})

onBeforeMount(async()=>{
  await travelStore.getTravel(travelId.value)
  contentsLoaded.value = true
})

onMounted(async() => {
  console.log("Mounted!");
});

onBeforeUnmount(()=>{
  travelStore.resetTravel()
  scheduleStore.resetScheduleList()
  mapStore.removeMarkerAll()
})

</script>