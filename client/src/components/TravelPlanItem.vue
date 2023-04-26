<template>
  <div id="travel-plan-item">
    
    <div id="item-info" @click="editPlan">
      <div id="flag-img"></div>
      <div id="item-detail">
        <p id="item-title">{{ planItem.title }}</p>
        <p id="item-date" class="text-sm">
          {{ planItem.fromDate.split('T')[0] }} - {{ planItem.endDate.split('T')[0] }}
        </p>
      </div>
    </div>

    <div>
      <div id="edit-item" @click="editTravel">
        <font-awesome-icon icon="fa-solid fa-pen" id="edit-item-img" />
      </div>
    </div>
  </div>
</template>

<style lang="scss">
#travel-plan-item {
  cursor: pointer;
  box-shadow: 0px 0px 5px #444;
  border-radius: 8px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 15px 10px 10px;
  margin: 1rem 0;

  &:hover{
    border: 1px solid $green;
  }

  #item-info {
    display: flex;
    width: 100%;
    align-items: center;
    margin-left:1rem;

    #flag-img {
      width: 58px;
      height: 58px;
      background-color: #ddd;
      border-radius: 50%;
      margin-right: 1.8rem;
    }

    #item-title {
      font-size:1.3rem;
      font-weight: 600;
      margin-bottom: 0.5rem;
    }

  }
  #edit-item {
    width: 3.5rem;
    height: 3rem;
    padding:0.8rem;

    #edit-item-img {
      vertical-align:1em;
      width: 100%;
      height: 100%;
    }
  }
}
</style>

<script setup>
import { defineProps } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../stores/travel";

const props = defineProps({
  planItem: Object,
});

const router = useRouter()
const travelStore = useTravelStore()

const editPlan = ()=>{
  router.push({
    name:"schedule",
    params:{
      id:props.planItem.id
    }
  })
}

const editTravel = async()=>{
  await travelStore.getTravel(props.planItem.id)
  router.push({
    name:'new-country',
    params:{
      id:props.planItem.id
    }
  })
}

</script>
