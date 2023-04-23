import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../../../composable/useApi";

export const useBookAllStore = defineStore("plan", () => {
  const API = useApi();

  const bookAllList = ref([]);

  const getBookAll = async (planId) => {
    console.log("id", planId);

    const { data } = await API.post(`/travel/${planId}/plan/?`, {});

    console.log("data", data);
    bookAllList.value = data.results;
  };

  return { bookAllList, getBookAll };
});
