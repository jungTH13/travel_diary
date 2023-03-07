<template>
  <div>
    <input type="text" v-model="searchInput" />
    <button @click="handleSearch">search</button>
    <div v-if="searchResult.length > 0">
      <span></span>
    </div>
    <span>되는건가요</span>
    <span>그런건가요</span>
  </div>
</template>

<script>
import axios from "axios";

export default {
  layout: "default",
  data() {
    return {
      searchInput: "",
      searchResult: [],
    };
  },
  methods: {
    async handleSearch() {
      if (this.searchInput === "") {
        return;
      }

      const countryList = await axios.get(
        "http://3.36.127.178/common/getListCountry"
      );
      const data = await axios.post("http://3.36.127.178/user/cookie", {
        withCredentials: true,
      });

      const headers = {
        "Content-Type": "application/json;charset=UTF-8",
      };
      console.log("country", countryList);
      console.log("data", data);
    },
  },
};
</script>
