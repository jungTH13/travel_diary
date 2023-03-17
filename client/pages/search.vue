<template>
  <div>
    <div id="search-page">
      <div id="search-input-wrapper" @click="handleCountrySearch">
        <input class="text-lg" type="text" v-model="searchInput" />
        <button id="search-button">
          <font-awesome-icon icon="fa-solid fa-magnifying-glass" />
        </button>
      </div>

      <div id="selected-country-wrapper">
        <div
          v-for="item in checkedList"
          v-bind:key="item.id"
          class="selected-country"
        >
          <span class="text-lg">{{ item.name }}</span>
          <font-awesome-icon icon="fa-solid fa-jet-fighter" id="plane-img" />
        </div>
      </div>

      <div id="country-list-wrapper">
        <div
          v-for="country in dummyCountry"
          id="country-info"
          v-bind:key="country.id"
        >
          <div id="country-detail">
            <div id="flag-img">{{ country.thumbnail }}</div>
            <span>{{ country.name }}</span>
          </div>

          <label>
            <input
              :disabled="checkedList.length > 2"
              type="checkbox"
              name="check"
              :value="country"
              v-model="checkedList"
            />
            <span id="check-box-custom"></span>
          </label>
        </div>
      </div>

      <div id="select-complete" @click="getCheckedData">
        <div class="submit-button">선택완료</div>
      </div>
    </div>

    <!-- <input type="text" v-model="searchInput" /> -->
    <!-- <button @click="handleSearch">search</button>
    <div v-if="searchResult.length > 0">
      <span></span>
    </div>
    <span>되는건가요</span>
    <span>그런건가요</span> -->
  </div>
</template>

<style lang="scss">
#search-page {
  width: 100%;
  height: calc(100vh - 70px);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  #search-input-wrapper {
    margin-top: 20px;
    padding: 0 40px;
    display: flex;
    justify-content: center;
    input {
      border: none;
      border-bottom: 1px solid;
      flex-grow: 1;
    }
    input:focus {
      outline: none;
    }
    #search-button {
      width: 36px;
      height: 36px;
      margin-left: 10px;
      background: none;
      border: none;
      cursor: pointer;
    }
  }

  #selected-country-wrapper {
    padding: 0 40px;
    width: 100%;
    flex-wrap: wrap;
    display: flex;
    justify-content: center;
    margin: 20px 0;

    .selected-country {
      display: flex;
      align-items: center;

      span {
        font-weight: bold;
      }

      #plane-img {
        width: 28px;
        height: 28px;
        margin: 0 20px;
      }
      &:last-of-type > #plane-img {
        display: none;
      }
    }
  }

  #country-list-wrapper {
    flex-grow: 1;
    padding: 0 40px;

    overflow-y: auto;
    -ms-overflow-style: none; /* IE and Edge */
    scrollbar-width: none; /* Firefox */
    &::-webkit-scrollbar {
      display: none; /* Chrome, Safari, Opera*/
    }

    #country-info {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin: 20px 0;
      #country-detail {
        display: flex;
        align-items: center;
      }

      #flag-img {
        width: 58px;
        height: 58px;
        background-color: #ddd;
        border-radius: 50%;
        margin-right: 20px;
      }

      #country-checkbox {
        display: block;
        position: relative;
        cursor: pointer;
        width: 24px;
        height: 24px;
        /* border-radius: 50%; */

        input[type="checkbox"] {
          position: absolute;
          opacity: 0;
          cursor: pointer;
          height: 0;
          width: 0;
        }

        #check-box-custom {
          position: absolute;
          top: 0;
          left: 0;
          height: 24px;
          width: 24px;
          /* border-radius: 50%; */
          background-color: #fff;
          border: 2px solid #999;
        }

        input[type="checkbox"]:checked ~ #check-box-custom {
          background-color: #999;
        }
      }
    }
  }

  #select-complete {
    padding: 0 20px;
  }
}
</style>

<script>
import axios from "axios";

export default {
  // layout: "default",
  data() {
    return {
      searchInput: "",
      searchResult: [],
      dummyCountry: [
        { id: 1, thumbnail: "", name: "가나", checked: false },
        { id: 2, thumbnail: "", name: "미국", checked: false },
        { id: 3, thumbnail: "", name: "캐나다", checked: false },
        { id: 4, thumbnail: "", name: "일본", checked: false },
        { id: 5, thumbnail: "", name: "캐나다", checked: false },
        { id: 6, thumbnail: "", name: "캐나다", checked: false },
        { id: 7, thumbnail: "", name: "캐나다", checked: false },
        { id: 8, thumbnail: "", name: "캐나다", checked: false },
        { id: 9, thumbnail: "", name: "캐나다", checked: false },
        { id: 10, thumbnail: "", name: "캐나다", checked: false },
        { id: 11, thumbnail: "", name: "캐나다", checked: false },
        { id: 12, thumbnail: "", name: "캐나다", checked: false },
      ],
      checkedList: [],
    };
  },
  methods: {
    handleCountrySearch(e) {
      e.preventDefault();
      if (this.searchInput) {
        console.log("test", this.searchInput);
        this.searchInput = "";
      }
    },
    getCheckedData() {
      console.log(this.checkedList);
    },

    // async handleSearch() {
    //   if (this.searchInput === "") {
    //     return;
    //   }

    //   const countryList = await axios.get(
    //     "http://3.36.127.178/common/countryList"
    //   );
    //   const data = await axios.post("http://3.36.127.178/user/cookie", {
    //     withCredentials: true,
    //   });

    //   const headers = {
    //     "Content-Type": "application/json;charset=UTF-8",
    //   };
    //   console.log("country", countryList);
    //   console.log("data", data);
    // },
  },
};
</script>
