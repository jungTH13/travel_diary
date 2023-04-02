<template>
  <div style="overflow-y: auto;">
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

      <CountrySelected v-model="checkedList" />

      <CountryList v-model="checkedList" :visual-searched="isSearch" />

      <div id="select-complete" @click="testCheckList">
        <div class="submit-button">선택완료</div>
      </div>
    </div>
    
  </div>
</template>

<style lang="scss" scoped>
#search-page {
  width: 100%;
  height: calc(100vh - 70px);
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  #search-input-wrapper {
    width: 100%;
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
      width: 34px;
      height: 34px;
      margin-left: 10px;
      background: none;
      border: none;
      cursor: pointer;
      #search-button-img {
        width: 100%;
        height: 100%;
      }
    }
  }
}
#select-complete {
    position:fixed;
    bottom:10px;
    width: 100%;
    padding: 0 20px;
  }

</style>

<script setup>
import axios from "axios";
import { ref, reactive, onBeforeMount, onMounted, watch } from "vue";
import { useRouter } from "vue-router";
import CountryList from "../../components/CountryList.vue";
import CountrySelected from "../../components/CountrySelected.vue";
import { useCountryStore } from "../../stores/country";
import { useTravelStore } from "../../stores/travel";

//stores
const countryStore = useCountryStore()
const router = useRouter();
const travelStore = useTravelStore()

//contents
const searchInput = ref("");
const checkedList = ref([]);
const isSearch = ref(false);

function checkLimit() {
  if (checkedList.value.length > 3) {
    alert("3개까지만 선택 가능합니다.");
    checkedList.value.pop();
  }
}

function testCheckList() {
  if (checkedList.value.length === 0) {
    return alert("나라를 선택해주세요");
  }
  const countries = checkedList.value;
  console.log("check", countries);

  const codeArr = checkedList.value.map((item) => item.code);
  const countryArr = checkedList.value.map((item) => item.name);

  console.log(codeArr, countryArr);

  travelStore.newTravel.countryList = checkedList.value
  travelStore.newTravel.title = countryArr.join('-')
  
  router.push({
    name: "newCountry",
  });
}

async function handleCountrySearch(e) {
  e.preventDefault();
  if (searchInput.value) {
    console.log("test", searchInput.value);

    countryStore.getCountryListLike(searchInput.value)
    return isSearch.value = true
  }
  return isSearch.value =false
}


onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(async () => {
  if(travelStore.newTravel.countryList.length) checkedList.value = travelStore.newTravel.countryList
});
</script>
