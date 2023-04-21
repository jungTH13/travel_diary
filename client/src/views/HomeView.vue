<template>
  <div style="height:100%">
    <div id="main-page">
      <router-link to="/travel/country" id="add-plan-button">
        <span class="plus-button orange"
          ><font-awesome-icon icon="fa-solid fa-plus" id="plus-button-img"
        /></span>
        <span class="text-lg">여행일정 만들기</span>
      </router-link>

      <div id="plan-travel-list">
        <div id="plan-travel">
          <h3>떠날 여행</h3>
          <div v-for="item in travelList.plan" v-bind:key="item.id">
            <TravelPlanItem :planItem="item" />
          </div>
        </div>
        <div id="end-travel">
          <h3>떠난 여행</h3>
          <div v-for="item in travelList.end" v-bind:key="item.id">
            <TravelPlanItem :planItem="item" />
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style lang="scss" scoped>
#main-page {
  width: 100%;
  height:100%;
  padding: 0 0px;
  display:flex;
  flex-direction: column;

  #add-plan-button {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    padding: 2rem 0;

    > span:last-of-type {
      margin-left:1rem;
      font-weight: bold;
    }
  }
  // .plus-button {
  //   margin-right: 10px;
  //   cursor: pointer;
  //   #plus-button-img {
  //     width: 18px;
  //     height: 18px;
  //   }
  // }

  h3 {
    font-weight: bold;
  }
  #plan-travel-list{
    height: 100%;
    width: 100%;
    padding-top:1rem;
    overflow: auto;

    #plan-travel {
      margin-bottom: 80px;

      div{
        padding-left: 0.3rem;
        padding-right: 0.3rem;
      }
    }

    #end-travel {

      div{
        padding-left: 0.3rem;
        padding-right: 0.3rem;
      }
    }
  }
  
}
</style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted } from "vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import TravelPlanItem from "../components/TravelPlanItem.vue";
import { useTravelStore } from "../stores/travel";

const router = useRouter();
const store = useTravelStore();
const travelList = store.travelList;

const editTravel = (travelId)=>{
  router.push({
    name:"schedule",
    params:{
      id:travelId
    }
  })
}

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(() => {
  store.getTravelList();
});
</script>
