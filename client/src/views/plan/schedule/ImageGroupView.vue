<template>
<div class="full-hidden book">
    <div class="book-contents">
        <ImageGroupCreater />
    </div>
    <div class="summit-footer">
        <button class="font-weight-600" @click="postPlan">등록</button>
    </div>
</div>


</template>

<style lang="scss" scoped>
.book{
    display: flex;
    flex-direction: column;
    
    .book-contents{
        margin-top:3rem;
        height: 100%;
        overflow-y: auto;
    }
}

</style>

<script setup>
import { ref, reactive, watch, onMounted, onUnmounted, onBeforeMount } from "vue";
import { useRouter, useRoute } from "vue-router";
import { computed } from "@vue/reactivity";
import BookItem from "../../../components/BookItem.vue";
import { useBookStore } from "../../../stores/plan/book";
import { useScheduleStore } from "../../../stores/plan/schedule";
import ImageGroupCreater from "../../../components/ImageGroupCreater.vue";


const route = useRoute();
const router = useRouter();
const bookStore = useBookStore()
const scheduleStore = useScheduleStore()

//contents
const travelId = computed(()=>route.params.id)
const book = computed(()=>bookStore.book)
const navImage = computed(()=>bookStore.navImage)

const goschedule = ()=>{
    scheduleStore.getscheduleList(travelId.value)
    router.push({name:"schedule"})
}

const postPlan = async ()=>{
    const response = await bookStore.postBook(travelId.value,navImage.value.type)
    
    if(response.code !== 200) alert('등록에 실패 했습니다.')
    else {
        const newPlanId = response.results[Object.keys(response.results)[0]]
        
        const response2 = await bookStore.postBookImages(travelId.value,newPlanId,navImage.value.type)
        if(response2.code !== 200) alert('이미지 등록에 실패했습니다.')
        goschedule()
    }
}

onBeforeMount(()=>{
    bookStore.resetBook()
})

onUnmounted(()=>{
    bookStore.resetBook()
})

onMounted(() => {
    console.log("Mounted!");
})
</script>
    