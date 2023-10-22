<template>
  <div id="travel-plan-item">
    
    <div id="item-info" @click="editPlan">
      <div id="flag-img"></div>
      <div id="item-detail">
        <p id="item-title">{{ planItem.title }}</p>
        <p id="item-date">
          {{ planItem.fromDate.split('T')[0] }} - {{ planItem.endDate.split('T')[0] }}
        </p>
      </div>
    </div>

    <div class="edit">
      <div v-if="!planSettingState" id="edit-item" @click="planSettingState= true">
        <font-awesome-icon icon="fa-solid fa-ellipsis" id="edit-item-img" class="icon" />
      </div>
      <div v-else id="edit-item">
        <font-awesome-icon icon="fa-solid fa-trash-can" id="edit-item-img" @click="delTravel" class="icon" />
        <font-awesome-icon icon="fa-solid fa-pen" id="edit-item-img" @click="editTravel" class="icon"/>
        <font-awesome-icon icon="fa fa-share-alt-square" id="edit-item-img" @click="shareTravel" class="icon"/>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
#travel-plan-item {
  cursor: pointer;
  // box-shadow: 0px 0px 5px #444;
  // border-radius: 8px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  // padding: 10px 15px 10px 10px;
  // margin: 1rem 0;
  margin-bottom: 1rem;
  padding:1rem;

  &:hover{
    background-color: $gray;
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
      font-size:1.7rem;
      font-weight: 600;
      margin-bottom: 0.5rem;
    }

    #item-date{
      font-size:1.4rem;
    }

  }
  .edit{
    transition: all ease 1.5s 0s;
    #edit-item {
      // width: 3.5rem;
      margin-right:1rem;
      height: 100%;
      display: flex;

      #edit-item-img {
        margin:auto;
        margin-left: 2rem;
        vertical-align:1em;
        .icon{
          height: 2rem;
          width: 2rem;
          cursor: pointer;
        }
      }
    }
  }
  
}
</style>

<script setup>
import { defineProps, ref } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../stores/travel";
import { useShareStore } from "../stores/share";

const props = defineProps({
  planItem: Object,
});

const router = useRouter()
const travelStore = useTravelStore()
const userShareStore = useShareStore();

const planSettingState = ref(false)

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
    // params:{
    //   id:props.planItem.id
    // }
  })
}

const shareTravel = async()=>{
  await userShareStore.getUserList(props.planItem.id)
  router.push({
    name:'new-share',
  })
}

const delTravel = async()=>{
  await travelStore.getTravel(props.planItem.id)
  const travel = travelStore.travel

  if(travel.minDate || travel.maxDate) {
    if(!confirm("일정이 존재합니다!\n그래도 삭제할까요?")) return
  }
  const response = await travelStore.delTravel()
  if(response.code !== 200 ) alert("여행 삭제에 실패했습니다.") 
  travelStore.resetTravel()
  travelStore.getTravelList()
}

</script>
