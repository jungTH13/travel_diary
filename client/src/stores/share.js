import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../composable/api";

export const useShareStore = defineStore("shareUesr", () => {
  const searchUserList = ref([]);
  const selectUser = ref([]);

  async function getUserList(searchData) {
    const {data} = await API.get("/common/countryList");
    const userData =[
      {
        "email": "spfken2@gmail.com",
        "thumbnail": "https://travel-public.s3.ap-northeast-2.amazonaws.com/Main/dbafbe8a-ce18-4810-a886-c5f5ae7ae4b8.1_KR.png",
        "name": "김경섭"
      }
    ]
    
    searchUserList.value = userData;
  }

  return {
    searchUserList,
    getUserList,
  };
});
