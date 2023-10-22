<template>
  <div id="selected-user-wrapper">
    <div id="crudPermission">
      <nav class="nav">
        <ul>
          <li>
            <div class="item-box">
              <p>계정정보</p>
            </div>
          </li>
          <li>
            <div class="item-box">
              <p>권한</p>
            </div>
          </li>
        </ul>
      </nav>
      <div v-for="item in userList" v-bind:key="item.id" class="selected-user">
        <img :src="item.thumbnail" alt="유저 이미지" />
        <span @click="delShareUser(item)">{{ item.name }}</span>
        <v-select
          :items="crudData.items"
          chips
          label="권한"
          multiple
        ></v-select>
      </div>
    </div>
  </div>
</template>

<style lang="scss">
@import "../assets/scss/components/selectedUser.scss";
</style>

<script setup>
import { computed, defineProps, ref, watch } from "vue";

const props = defineProps({
  modelValue: Array,
});
const emit = defineEmits(["update:modelValue"]);

const userList = ref(props.modelValue);
const crudData = {
  items: ["검색", "생성", "수정", "삭제"],
  value: ["search", "create", "update", "delete"],
};

const delShareUser = (user) => {
  const newCountries = userList.value.filter((c) => c !== user);
  emit("update:modelValue", newCountries);
};

watch(
  () => props.modelValue,
  () => {
    if (userList.value == props.modelValue) return;
    userList.value = props.modelValue;
  }
);

watch(
  () => userList.value,
  () => {
    if (userList.value == props.modelValue) return;
    emit("update:modelValue", userList.value);
  }
);
</script>
