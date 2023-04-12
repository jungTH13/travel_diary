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

      <SelectedCountries :countries="checkedList" />

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
import { ref, reactive, onBeforeMount, onMounted } from "vue";
import { useRouter } from "vue-router";
import { storeToRefs } from "pinia";
import { useTravelStore } from "../../stores/travel";
import SelectedCountries from "../../components/SelectedCountries.vue";

const router = useRouter();
const store = useTravelStore();

const searchInput = ref("");
const countryList = ref();
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

  store.setTravelCountries(countries);
  router.push("/travel/new");
}

async function handleCountrySearch(e) {
  e.preventDefault();

  const searchData = JSON.stringify({
    name: searchInput.value,
  });

  const data = await store.getCountrySearchResult(searchData);

  if (data.code === 401) {
    router.push("/login");
  }

  countryList.value = data.results.countryList;
  searchInput.value = "";
}

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(async () => {
  if (!searchInput.value) {
    const data = await store.getAllCountries();

    if (data.code === 401) {
      router.push("/login");
    }

    countryList.value = data.results.countryList;
  }
});
</script>
