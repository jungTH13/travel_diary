<template>
  <div class="plan-container full-hidden">
    <section class="col">
      <div id="travel-share-user">공유 유저 리스트</div>
      <div id="search-page">
        <form id="search-input-wrapper" @submit.prevent="handleUserSearch">
          <input class="text-lg" type="text" v-model="searchInput" />
          <button id="search-button">
            <font-awesome-icon
              id="search-button-img"
              icon="fa-solid fa-magnifying-glass"
            />
          </button>
        </form>

        <div id="user-list-wrapper">
          <div
            v-for="userData in userList"
            id="user-info"
            :key="userData.code"
          >
            <label>
              <div id="user-item-wrapper">
                <div id="user-detail">
                  <div id="flag-img">
                    <div v-if="userData.thumbnail">
                      <img :src="userData.thumbnail" alt="유저 이미지" />
                    </div>
                  </div>
                  <span>{{ userData.name }}</span>&nbsp;-&nbsp;
                  <span>{{ userData.email }}</span>
                </div>
                <div id="user-checkbox">
                  <input
                    type="checkbox"
                    name="check"
                    :value="userData"
                    v-model="travel.userList"
                  />
                  <span id="check-box-custom"> </span>
                </div>
              </div>
            </label>
          </div>
        </div>

        <SelectedShareUser v-model="travel.userList" />

        <div id="select-complete" @click="handleCheckList">
          <div class="submit-button">저장</div>
        </div>
      </div>
    </section>
  </div>
</template>

<style lang="scss">
@import "../../assets/scss/pages/share.scss";
</style>

<script setup>
import { ref, onMounted, computed, watch } from "vue";
import { useRouter } from "vue-router";
import { useTravelStore } from "../../stores/travel";
import { useShareStore } from "../../stores/share";

import SelectedShareUser from "../../components/SelectedShareUser.vue";

const router = useRouter();
const travelStore = useTravelStore();
const userShareStore = useShareStore();

const searchInput = ref("");
const userList = computed(() => userShareStore.searchUserList);
const travel = computed(() => travelStore.travel);

function handleCheckList() {
  if (travel.value.userList.length === 0) {
    return alert("유저를 선택해 주세요.");
  }
}

async function handleUserSearch() {
  userShareStore.getUserList(searchInput.value);
}

watch(
  () => searchInput.value,
  () => handleUserSearch()
);

onMounted(async () => {
  await userShareStore.getUserList();
});
</script>
