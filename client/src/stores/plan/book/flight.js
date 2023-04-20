import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../../../composable/useApi";

const defaultFlight = () => ({
  airline: "",
  arriveDate: "",
  arriveLocation: "",
  boardingGate: "",
  boardingTime: "",
  createdDate: "",
  deleted: false,
  departDate: "",
  departLocation: "",
  flightNumber: "",
  id: "",
  memo: "",
  modifiedDate: "",
  reservationNumber: "",
  seatNumber: "",
  terminal: "",
  title: "",
  x: null,
  y: null,
});

export const useFlightStore = defineStore("plan", () => {
  const API = useApi();

  const bookFlight = defaultFlight();
  const bookFlightList = ref([]);

  const getBookFlight = async (planId) => {
    console.log("id", planId);

    const { data } = await API.post(
      `/travel/${planId}/plan/airPlane/airPlaneList`,
      {}
    );

    console.log("data", data);
    bookFlightList.value = data.results.planAirPlaneList;
  };

  // const getPlanCountries = computed(() => planCountries.value);

  return { bookFlightList, getBookFlight };
});
