<template>
<div class="full-hidden book">
    <div class="book-contents">
        <ImageGroupCreater />
    </div>
    <div class="summit-footer">
        <button v-if="book.id" class="font-weight-600" @click="delPlan">삭제</button>
        <button v-if="book.id" class="font-weight-600" @click="putPlan">수정</button>
        <button v-else class="font-weight-600" @click="postPlan">등록</button>
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
const planId = computed(()=>route.params.planId)
const book = computed(()=>bookStore.book)
const navImage = computed(()=>bookStore.navImage)

const goschedule = ()=>{
    scheduleStore.getscheduleList(travelId.value,null,true)
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

const putPlan =async ()=>{
    const response = await bookStore.putBook(travelId.value,'pig')
    
    if(response.code !== 200) alert('수정에 실패 했습니다.')
    else {
        const response = await bookStore.postBookImages(travelId.value,book.value.id,'pig')
        if(response.code !== 200) alert('이미지 수정에 실패했습니다.')
        goschedule()
    }
}

const delPlan =async ()=>{
    const response = await bookStore.delBook(travelId.value,'pig')
    
    if(response.code !== 200) alert('삭제에 실패 했습니다.')
    else goschedule()
}

onBeforeMount(()=>{
    bookStore.resetBook()
    if(planId.value && !isNaN(planId.value)) bookStore.getBook(travelId.value,planId.value,'pig')
})

onUnmounted(()=>{
    bookStore.resetBook()
})

onMounted(() => {
    console.log("Mounted!");
})
</script>
    