import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../../../composable/useApi";

const defaultHotel = () => ({});

export const useBookHotelStore = defineStore("plan", () => {
  const API = useApi();

  const bookHotel = defaultHotel();
  const bookHotelList = ref([]);

  const getBookHotel = async (planId) => {
    console.log("id", planId);

    const { data } = await API.post(`/travel/${planId}/plan/?`, {});

    console.log("data", data);
    bookHotelList.value = data.results;
  };

  return { bookHotelList, getBookHotel };
});
