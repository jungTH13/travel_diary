<template>
<div>
    <div id="selected-country-wrapper">
        <div
        v-for="item,index in slecteCountryList"
        v-bind:key="item.id"
        class="selected-country"
        >
        <span> {{ item.name }} </span>
        <img
          v-if="index+1 !== slecteCountryList?.length"
          src="@/assets/icons/flight.png"
          alt="flight"
          style="width: 20px; height: 20px; margin-left: 4px; margin-right:4px;"
        />
        </div>
    </div>
</div>
</template>

<style lang="scss" scoped>

#selected-country-wrapper {
    padding: 20px 40px;
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    background-color: #fff;

    .selected-country {
      display: flex;
      align-items: center;

      span {
        font-size: 1.2rem;
        font-weight: bold;
      }

      #plane-img {
        width: 20px;
        height: 20px;
        margin: 0 20px;
      }
      &:last-of-type > #plane-img {
        display: none;
      }
    }
  }
</style>

<script setup>
import { computed } from 'vue';


//props & emit
const props = defineProps({
    modelValue: Array,
})

//contents
const slecteCountryList = computed(()=>props.modelValue)
</script>
  