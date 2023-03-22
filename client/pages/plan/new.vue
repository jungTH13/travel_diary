<template>
  <div class="plan-container"> 
    <section>
      <div class="plan-title">
        <span class="font-weight-600">캐나다</span>
        <img
          src="@/assets/icons/flight.png"
          alt="flight"
          style="width: 20px; height: 20px"
        />
        <span class="font-weight-600">미국</span>
      </div>
      <p class="plan-info">캐나다 & 미국 여행</p>
      <p class="plan-info">2025년 3월 10일 ~ 2025년 4월 2일</p>
      <div class="plan-date">
         <img
          src="@/assets/icons/cal.png"
          alt="calender"
        />
        <input type="date" />
      </div>
    </section>
    <div class="plan-footer">
      <button class="font-weight-600">수정</button>
      <button class="font-weight-600">삭제</button>
      <button class="font-weight-600">등록</button>
    </div>
  </div>
</template>

<script setup>
import {useRoute} from 'vue-router';


  const route =useRoute();
  onMounted(() =>{
    if(route){
      console.log(route.params)
    }

    if(this.$router){
      console.log(this.$router)
    }
})

</script>

<style lang="scss" scoped>


</style>
