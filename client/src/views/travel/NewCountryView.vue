<template>
  <div>
    <div id="search-page">
      <form id="search-input-wrapper" @submit.prevent="handleCountrySearch">
        <input class="text-lg" type="text" v-model="searchInput" />
        <button id="search-button">
          <font-awesome-icon
            id="search-button-img"
            icon="fa-solid fa-magnifying-glass"
          />
        </button>
      </form>

      <SelectedCountries v-model="travel.countryList" />

      <div id="country-list-wrapper">
        <div
          v-for="country in countryList"
          id="country-info"
          :key="country.code"
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
                  type="checkbox"
                  name="check"
                  :value="country"
                  v-model="travel.countryList"
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
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../../stores/travel";
import SelectedCountries from "../../components/SelectedCountries.vue";
import { useCountryStore } from "../../stores/country";

const router = useRouter();
const travelStore = useTravelStore();
const countryStore = useCountryStore();

const searchInput = ref("");
const countryList = computed(()=>countryStore.searchCountryList);
const travel = computed(()=>travelStore.travel);

function handleCheckList() {
  if (travel.value.countryList.length === 0) {
    return alert("나라를 선택해주세요");
  }

  router.push({name:"new-travel"});
}

async function handleCountrySearch() {
  countryStore.getCountryList(searchInput.value);
}

watch(()=>searchInput.value,()=>handleCountrySearch());
watch(()=>travel.value.countryList?.length,()=>{
  if(travel.value.countryList.length>3){
    travel.value.countryList.pop();
    alert("3개까지만 선택 가능합니다.");
  }
});

onBeforeMount(() => {
  console.log("Before Mount!");
});

onMounted(async () => {
  await countryStore.getCountryList();
});
</script>
