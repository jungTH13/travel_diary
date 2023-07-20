<template>
  <div id="selected-country-wrapper">
    <div
      v-for="item,index in countries"
      v-bind:key="item.id"
      class="selected-country"
    >
      <span @click="delCountry(item)">{{ item.name }}</span>
      <font-awesome-icon v-if="countries.length !== index+1" icon="fa-solid fa-jet-fighter" id="plan-img" />
    </div>
  </div>
</template>

<style lang="scss">
@import "../assets/scss/components/selectedCountries.scss";
</style>

<script setup>

import { computed, defineProps, ref, watch } from "vue";

const props = defineProps({
  modelValue: Array,
});
const emit = defineEmits(['update:modelValue'])

const countries = ref(props.modelValue)

const delCountry = (country)=>{
  const newCountries = countries.value.filter((c)=>c!==country)
  emit('update:modelValue',newCountries)
}

watch(()=>props.modelValue,()=>{
    if(countries.value == props.modelValue) return;
    countries.value = props.modelValue
})

watch(()=>countries.value,()=>{
    if(countries.value == props.modelValue) return;
    emit("update:modelValue",countries.value)
})


</script>
