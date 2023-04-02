import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

export const usePlanStore = defineStore("plan", () => {
  const planCountries = ref([]);

  function setPlanCountries(countries) {
    planCountries.value = countries;
  }

  const getPlanCountries = computed(() => planCountries.value);

  return { planCountries, setPlanCountries, getPlanCountries };
});
