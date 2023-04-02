<template>
  <div class="plan-container">
    <section>
      <div class="plan-title">
        <CountrySelected v-model="countries"/>
      </div>
      <p class="plan-info">{{ travelTitle }}</p>
      <p class="plan-info"><img src="@/assets/icons/cal.png" alt="calender" /> <input type="date" v-model="startDate" /> ~ <input type="date" v-model="endDate" /></p>
      <div class="plan-date">
        <!-- <img src="@/assets/icons/cal.png" alt="calender" /> -->
        <!-- <input type="date" v-model="startDate" /> -->
      </div>
    </section>
    <div class="plan-footer">
      <button class="font-weight-600" @click="getRoute">수정</button>
      <button class="font-weight-600">삭제</button>
      <button class="font-weight-600" @click="post">등록</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useTravelStore } from "../../stores/travel";
import CountrySelected from "../../components/CountrySelected.vue";

//stores
const travelStore = useTravelStore()
const route = useRoute();
const router = useRouter()

//contents
const countries = computed(()=>travelStore.newTravel.countryList);
const travelTitle = ref(countries.value.map(country=>country.name).join(' & ') + ' 여행')
const startDate = ref('')
const endDate = ref('')


const post = async ()=>{
  travelStore.newTravel.startDate = startDate.value
  travelStore.newTravel.endDate = endDate.value

  const result = await travelStore.post()
  if(result.code === 200){
    router.push({name:'home'})
  }
  console.log(result)
}

onMounted(() => {
  console.log(route);
  console.log("^mounted");
  // getRoute();
});
</script>

<style lang="scss" scoped>
.plan-container {
  margin: 60px 90px 0;
  position: relative;
  height: calc(100vh - 110px);

  .plan-title {
    font-size: 20px;
    line-height: 27px;
    padding: 10px;
    border-bottom: 1px solid #dddddd;

    img {
      margin: 0 10px;
    }
  }

  p {
    font-size: 16px;
    line-height: 22px;
    padding: 10px;
    border-bottom: 1px solid #dddddd;
  }

  .plan-date {
    padding: 10px;
    display: flex;
    align-items: center;

    input {
      margin-left: 10px;
      font-size: 16px;
      line-height: 22px;
    }
  }

  .plan-footer {
    position: absolute;
    bottom: 5%;
    display: flex;
    width: 500px;
    justify-content: space-between;

    button {
      padding: 10px 20px;
      border: 1px solid $green;
      background-color: $green;
      border-radius: 5px;

      &:hover {
        background-color: white;
        border: 1px solid $green;
      }
    }
  }
}

</style>
