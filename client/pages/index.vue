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

      // const data = await axios.get("http://3.36.127.178/common/getListCountry");
      const data = await axios.post("http://3.36.127.178/user/cookie");
      // const { data } = await axios.get(
      //   "https://jsonplaceholder.typicode.com/todos/1"
      // );
      // .then((res) => console.log(res))
      // .catch((e) => console.log("error", e));
      console.log("data", data);
    },
  },
};
</script>
