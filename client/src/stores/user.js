import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const useUserStore = defineStore("user", () => {
  async function login() {
    const exam = await axios.get(
      "https://develop.life-traveldiary.net:8080/user/examCookie",
      {
        withCredentials: true,
      }
    );
    console.log("user", exam);
  }

  return { login };
});
