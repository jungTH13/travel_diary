<template>
  <div>
    <input type="text" v-model="searchInput" />
    <button @click="handleSearch">search</button>
    <div v-if="searchResult.length > 0">
      <span></span>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
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
      const data = await axios.post("http://3.36.127.178/user/cookie");

      console.log("country", countryList);
      console.log("data", data);
    },
  },
};
</script>
