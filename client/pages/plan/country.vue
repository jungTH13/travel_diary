<template>
  <div>
    <div id="search-page">
      <div id="search-input-wrapper">
        <input class="text-lg" type="text" v-model="searchInput" />
        <button id="search-button" @click="handleCountrySearch">
          <font-awesome-icon
            id="search-button-img"
            icon="fa-solid fa-magnifying-glass"
          />
        </button>
      </div>

      <div id="selected-country-wrapper">
        <div
          v-for="item in checkedList"
          v-bind:key="item.id"
          class="selected-country"
        >
          <span>{{ item.name }}</span>
          <font-awesome-icon icon="fa-solid fa-jet-fighter" id="plane-img" />
        </div>
      </div>

      <div id="country-list-wrapper">
        <div
          v-for="country in countryList"
          id="country-info"
          v-bind:key="country.id"
        >
          <label>
            <div id="country-item-wrapper">
              <div id="country-detail">
                <div id="flag-img">
                  <img
                    v-if="country.thumbnail"
                    :src="country.thumbnail"
                    alt="국기 이미지"
                  />
                </div>
                <span>{{ country.name }}</span>
              </div>
              <div id="country-checkbox">
                <input
                  @change="checkLimit"
                  type="checkbox"
                  name="check"
                  :value="country"
                  v-model="checkedList"
                />
                <span id="check-box-custom"> </span>
              </div>
            </div>
          </label>
        </div>
      </div>

      <div id="select-complete" @click="$router.push({path:'/plan/new',params:{checkedList}})">
          <div class="submit-button">선택완료</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>

<script>
import axios from "axios";
import { ref, reactive } from "vue";
import {useRouter} from 'vue-router';

export default {
  name: "search",
  setup() {
    const searchInput = ref("");
    const searchResult = ref([]);
    const countryList = ref([]);
    const checkedList = ref([]);
    const router =useRouter()

    function checkLimit() {
      if (checkedList.value.length > 3) {
        // alert("3개까지만 선택 가능");
        checkedList.value.pop();
      }
    }

    async function handleCountrySearch(e) {
      e.preventDefault();
      if (searchInput.value) {
        console.log("test", searchInput.value);
        searchInput.value = "";
      }
    }

    function goToPlan () {
       const countries = checkedList.value;
       
      router.push({
        path:'/plan/new',
        state: {
          data:'hi'
        }
      })
    }

    onBeforeMount(() => {
      console.log("Before Mount!");
    });

    onMounted(async () => {
      await axios.get(
        "https://develop.life-traveldiary.net:8080/user/examCookie",
        { withCredentials: true }
      );

      const { data } = await axios.get(
        "http://3.36.127.178:80/common/countryList"
      );
      countryList.value = data.results.countryList;
      console.log("country list", data.results.countryList);
    });

    return {
      checkLimit,
      goToPlan,
      handleCountrySearch,
      searchInput,
      searchResult,
      countryList,
      checkedList,
    };
  },

};
</script>
