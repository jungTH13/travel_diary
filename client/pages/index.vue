<template>
  <div>
    <div id="main-page">
      <div id="add-plan-button">
        <span class="plus-button orange" @click="goToPlanTravel">+</span>
        <span class="text-lg">여행일정 만들기</span>
      </div>

      <div id="plan-travel">
        <h3>떠날 여행</h3>
        <div v-for="item in planTravel" v-bind:key="item.id">
          <TravelPlanItem :planItem="item" />
        </div>
      </div>
      <div id="end-travel">
        <h3>떠난 여행</h3>
        <div v-for="item in endTravel" v-bind:key="item.id">
          <TravelPlanItem :planItem="item" />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
#main-page {
  width: 100%;
  padding: 0 40px;
  #add-plan-button {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    padding: 40px 0;

    > span:last-of-type {
      font-weight: bold;
    }
  }
  .plus-button {
    margin-right: 10px;
    cursor: pointer;
  }

  h3 {
    font-weight: bold;
  }

  #plan-travel {
    margin-bottom: 80px;
  }
}
</style>

<script>
import TravelPlanItem from "../components/TravelPlanItem.vue";

export default {
  components: {
    TravelPlanItem,
  },
  data() {
    return {
      planTravel: [
        {
          id: 1,

          title: "미국 여행",

          thumnail: "",

          fromDate: "2023-12-10",

          endDate: "2023-12-12",
        },
        {
          id: 2,

          title: "유럽 여행",

          thumnail: "",

          fromDate: "2023-12-15",

          endDate: "2023-12-18",
        },
      ],
      endTravel: [
        {
          id: 1,

          title: "일본 여행",

          thumnail: "",

          fromDate: "2022-11-14",

          endDate: "2022-11-15",
        },
        {
          id: 1,

          title: "중국 여행",

          thumnail: "",

          fromDate: "2022-12-4",

          endDate: "2022-12-5",
        },
      ],
    };
  },

  methods: {
    goToPlanTravel() {
      console.log("clicked");
    },
  },
};
</script>
