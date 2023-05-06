<template>
<div class="full-hidden book">
    <div class="book-contents">
        <BookItem />
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
import { ref, reactive, watch, onMounted, onUnmounted } from "vue";
import { useRouter, useRoute } from "vue-router";
import { computed } from "@vue/reactivity";
import BookItem from "../../../components/BookItem.vue";
import { useBookStore } from "../../../stores/plan/book";
import { useScheduleStore } from "../../../stores/plan/schedule";


const route = useRoute();
const router = useRouter();
const bookStore = useBookStore()
const scheduleStore = useScheduleStore()

//contents
const travelId = computed(()=>route.params.id)
const planId = computed(()=>route.params.planId)
const planType = computed(()=>route.params.planType)
const book = computed(()=>bookStore.book)
const nowTap = ref({});
const tapChange = (tap)=>{
nowTap.value = tap
}

const goBook = ()=>{
    scheduleStore.getscheduleList(travelId.value)
    router.push({name:"book"})
}

const postPlan = async ()=>{
    const response = await bookStore.postBook(travelId.value,planType.value)
    
    if(response.code !== 200) alert('등록에 실패 했습니다.')
    else {
        const response = await bookStore.postBookImages(travelId.value,book.value.id,planType.value)
        if(response.code !== 200) alert('이미지 등록에 실패했습니다.')
        goBook()
    }
}

const putPlan =async ()=>{
    const response = await bookStore.putBook(travelId.value,planType.value)
    
    if(response.code !== 200) alert('수정에 실패 했습니다.')
    else {
        const response = await bookStore.postBookImages(travelId.value,book.value.id,planType.value)
        if(response.code !== 200) alert('이미지 수정에 실패했습니다.')
        goBook()
    }
}

const delPlan =async ()=>{
    const response = await bookStore.delBook(travelId.value,planType.value)
    
    if(response.code !== 200) alert('삭제에 실패 했습니다.')
    else goBook()
}

onUnmounted(()=>{
    bookStore.resetBook()
})

onMounted(() => {
    console.log("Mounted!");
    if(planId.value && !isNaN(planId.value))bookStore.getBook(travelId.value,planId.value,planType.value)
})
</script>
