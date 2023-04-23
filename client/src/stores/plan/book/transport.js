import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../../../composable/useApi";

const defaultTransport = () => ({});

export const useBookTransportStore = defineStore("plan", () => {
  const API = useApi();

  const bookTransport = defaultTransport();
  const bookTransportList = ref([]);

  const getBookTransport = async (planId) => {
    console.log("id", planId);

    const { data } = await API.post(`/travel/${planId}/plan/?`, {});

    console.log("data", data);
    bookTransportList.value = data.results;
  };

  return { bookTransportList, getBookTransport };
});
