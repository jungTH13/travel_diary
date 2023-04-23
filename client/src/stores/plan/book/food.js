import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../../../composable/useApi";

const defaultFood = () => ({});

export const useBookFoodStore = defineStore("plan", () => {
  const API = useApi();

  const bookFood = defaultFood();
  const bookFoodList = ref([]);

  const getBookFood = async (planId) => {
    console.log("id", planId);

    const { data } = await API.post(`/travel/${planId}/plan/?`, {});

    console.log("data", data);
    bookFoodList.value = data.results;
  };

  return { bookFoodList, getBookFood };
});
