import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";

export const useUserStore = defineStore("user", () => {

  const userInfo = ref({})
  const env = import.meta.env.VITE_ENVIRONMENT

  function resetUserInfo (){
    userInfo.value = {}
  }

  async function examUserLogin() {
    if(env === 'production') return;
    
    const {data} = await API.get("/user/examCookie");

    return data
  }

  async function googleOAuthLogin (code){
    const {data} = await API.post("/user/googleOAuthLogin",{code})

    return data
  }

  async function getUserInfo(){
    const {data} = await API.get("/user/userInfo")

    if(data.code===200) userInfo.value = data.results.userInfo

    return data
  }

  async function logout(){
    const {data} = await API.get("/user/logout")
    console.log(data)
    return data
  }

  return { 
    userInfo,
    env,
    examUserLogin, 
    googleOAuthLogin,
    getUserInfo,
    resetUserInfo,
    logout
  };
});
