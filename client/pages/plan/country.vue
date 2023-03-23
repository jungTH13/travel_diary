<template>
  <div>
    <div id="search-page">
      <form id="search-input-wrapper" @submit="handleCountrySearch">
        <input class="text-lg" type="text" v-model="searchInput" />
        <button id="search-button">
          <font-awesome-icon
            id="search-button-img"
            icon="fa-solid fa-magnifying-glass"
          />
        </button>
      </form>

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
                  <div v-if="country.thumbnail">
                    <img :src="country.thumbnail" alt="국기 이미지" />
                  </div>
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

      <div id="select-complete" @click="handleCheckList">
        <div class="submit-button">선택완료</div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped></style>

<script setup>
import axios from "axios";
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
const router = useRouter();

const searchInput = ref("");
const countryList = ref([]);
const checkedList = ref([]);

function checkLimit() {
  if (checkedList.value.length > 3) {
    alert("3개까지만 선택 가능합니다.");
    checkedList.value.pop();
  }
}

function handleCheckList() {
  if (checkedList.value.length === 0) {
    return alert("나라를 선택해주세요");
  }
  const countries = checkedList.value;
  console.log("check", countries);

  const codeArr = checkedList.value.map((item) => item.code);
  const countryArr = checkedList.value.map((item) => item.name);

  console.log(codeArr, countryArr);

  router.push({
    path: "/plan/new",
    query: {
      codes: JSON.stringify(codeArr),
      countries: JSON.stringify(countryArr),
    },
  });
}

async function handleCountrySearch(e) {
  e.preventDefault();

  console.log("test", searchInput.value);

  const searchData = JSON.stringify({
    name: searchInput.value,
  });

  const { data } = await axios.post(
    "https://develop.life-traveldiary.net:8080/common/countryLike",
    searchData,
    {
      withCredentials: true,
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  console.log("country list", data.results.countryList);
  countryList.value = data.results.countryList;
  searchInput.value = "";
}

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(async () => {
  if (!searchInput.value) {
    const { data } = await axios.get(
      "https://develop.life-traveldiary.net:8080/common/countryList",
      {
        withCredentials: true,
      }
    );
    if (data.code === 401) {
      router.push("/login");
    }

    countryList.value = data.results.countryList;
    console.log("country list", data.results);
  }
});
</script>
