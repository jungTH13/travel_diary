import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../../../composable/useApi";

const defaultOther = () => ({});

export const useBookOtherStore = defineStore("plan", () => {
  const API = useApi();

  const bookOther = defaultOther();
  const bookOtherList = ref([]);

  const getBookOther = async (planId) => {
    console.log("id", planId);

    const { data } = await API.post(`/travel/${planId}/plan/?`, {});

    console.log("data", data);
    bookOtherList.value = data.results;
  };

  return { bookOtherList, getBookOther };
});
