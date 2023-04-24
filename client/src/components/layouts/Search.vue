<template>
    <div>
        <div id="search-page">
            <form id="search-input-wrapper" @submit.prevent="handleSearch">
                <input class="text-lg" type="text" v-model="searchInput" />
                <button id="search-button">
                    <font-awesome-icon
                    id="search-button-img"
                    icon="fa-solid fa-magnifying-glass"
                    />
                </button>
            </form>
            </div>
    </div>
</template>
  
<style lang="scss" scoped>
#search-page{
    width: auto;
    height: auto;

    #search-input-wrapper{
        margin-top: 0px;
        padding: 0 0;
    }
}
</style>

<script setup>
import { ref, reactive, onBeforeMount, onMounted, computed, watch } from "vue";


const props = defineProps({modelValue:String})
const emit = defineEmits(["update:modelValue","submit"])

const searchInput = ref(props.modelValue)

const handleSearch = ()=>{
    emit("submit")
}


watch(()=>props.modelValue,()=>{
    if(searchInput.value === props.modelValue) return;
    searchInput.value = props.modelValue
})

watch(()=>searchInput.value,()=>{
    if(searchInput.value === props.modelValue) return;
    emit("update:modelValue",searchInput.value)
})

</script>
  