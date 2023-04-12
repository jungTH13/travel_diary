import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const useBudgetStore = defineStore("budget", () => {
  async function saveBudget(budget) {
    const { data } = await axios.post(
      "https://develop.life-traveldiary.net:8080/travel/plan/accountBook/accountBookInsert",
      budget,
      {
        withCredentials: true,
      }
    );
  }

  return { saveBudget };
});
