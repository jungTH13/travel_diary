import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../composable/useApi";

const defaultTravel = () => ({
  title: "",
  countryList: [],
  startDate: "",
  endDate: "",
});

export const useTravelStore = defineStore("travel", () => {
  const API = useApi();

  const travelList = reactive({
    plan: [],
    end: [],
  });

  const travel = ref([]);

  function resetTravel() {
    travel.value = defaultTravel();
  }

  async function getTravelList() {
    const { data } = await API.get("/travel/userTravelList");

    travelList.plan = data.results.planTravel;
    travelList.end = data.results.endTravel;

    return data;
  }

  async function postTravel() {
    const form = {};

    Object.keys(travel.value).forEach((key) => (form[key] = travel.value[key]));

    form.countryList = form.countryList.map((country) => country.code);

    const { data } = await API.post("/travel/travelInsert", form);

    return data;
  }

  return {
    travelList,
    travel,
    resetTravel,
    getTravelList,
    postTravel,
  };
});
