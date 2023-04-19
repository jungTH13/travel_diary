import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useApi } from "../composable/useApi";

export const useUserStore = defineStore("user", () => {
  const API = useApi();

  async function login() {
    const exam = await API.get("/user/examCookie");

    console.log("user", exam);
  }

  return { login };
});
