<template>
    <div>  
        <div id="country-list-wrapper">
          <div
            v-for="country in (visualSearched? searchCountryList : countryList)"
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
                    v-model="selectCountryList"
                  />
                  <span id="check-box-custom"> </span>
                </div>
              </div>
            </label>
          </div>
        </div>
    </div>
</template>

<style lang="scss" scoped>
  #country-list-wrapper {
    flex-grow: 1;
    padding: 0 40px;

    overflow-y: auto;
    -ms-overflow-style: none;
    scrollbar-width: none;
    &::-webkit-scrollbar {
      display: none;
    }
    #country-info {
      width: 100%;
      margin: 20px 0;

      label {
        display: block;
        width: 100%;
      }
      #country-item-wrapper {
        width: 100%;
        display: flex;
        justify-content: space-between;
        align-items: center;

        #country-detail {
          display: flex;
          align-items: center;
        }

        #flag-img {
          width: 58px;
          height: 58px;
          background-color: #fff;
          border: 2px solid #fff;
          border-radius: 50%;
          margin-right: 20px;
          img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
          }
        }

        #country-checkbox {
          display: block;
          position: relative;
          cursor: pointer;
          width: 24px;
          height: 24px;

          input[type="checkbox"] {
            position: absolute;
            opacity: 0;
            cursor: pointer;
            height: 0;
            width: 0;
          }

          #check-box-custom {
            position: absolute;
            top: 0;
            left: 0;
            height: 24px;
            width: 24px;
            border-radius: 50%;
            background-color: #fff;
            border: 2px solid $green;
          }

          input[type="checkbox"]:checked ~ #check-box-custom {
            background-color: $green;
          }
        }
      }
    }
  }
</style>
  
<script setup>
import {useCountryStore} from "../stores/country"
import { ref, reactive, onBeforeMount, onMounted, watchEffect, watch, computed } from "vue";


//props & emit
const props = defineProps({
    modelValue: Array,
    visualSearched: Boolean,
})
const emit = defineEmits(['update:modelValue'])

//stores
const countryStore = useCountryStore()

//contents
const selectCountryList = ref(props.modelValue || [])
const searchCountryList = computed(()=>countryStore.searchCountryList)
const countryList = computed(()=>countryStore.countryList)
const visualSearched = computed(()=>props.visualSearched || false)

watch(()=>selectCountryList.value,()=>{
  console.log('emit')
  emit('update:modelValue',selectCountryList.value)
})
watch(()=>props.modelValue,()=>{
    if(selectCountryList.value !== props.modelValue)
        selectCountryList.value = props.modelValue
})

onMounted(()=>{
    countryStore.getCountryList()
})

</script>
