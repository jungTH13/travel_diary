<template>
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
</template>

<style lang="scss"></style>

<script setup>
import { useCountryStore } from "../stores/country";
import {
  ref,
  reactive,
  onBeforeMount,
  onMounted,
  watchEffect,
  watch,
  computed,
} from "vue";

//props & emit
const props = defineProps({
  modelValue: Array,
  visualSearched: Boolean,
});
const emit = defineEmits(["update:modelValue"]);

//stores
const countryStore = useCountryStore();

//contents
const selectCountryList = ref(props.modelValue || []);
const searchCountryList = computed(() => countryStore.searchCountryList);
const countryList = computed(() => countryStore.countryList);
const visualSearched = computed(() => props.visualSearched || false);

watch(
  () => selectCountryList.value,
  () => {
    console.log("emit");
    emit("update:modelValue", selectCountryList.value);
  }
);

watch(
  () => props.modelValue,
  () => {
    if (selectCountryList.value !== props.modelValue)
      selectCountryList.value = props.modelValue;
  }
);

onMounted(() => {
  countryStore.getCountryList();
});
</script>
