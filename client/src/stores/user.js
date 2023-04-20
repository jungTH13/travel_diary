import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";

export const useUserStore = defineStore("user", () => {
  async function login() {
    const exam = await API.get("/user/examCookie");

    console.log("user", exam);
  }

  return { login };
});
