<template>
<div class="full-hidden book">
    <div class="book-contents">
        <BookItem />
    </div>
    <div class="book-footer">
        <button v-if="book.id" class="font-weight-600" @click="putPlan">수정</button>
        <button v-if="book.id" class="font-weight-600" @click="delPlan">삭제</button>
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
    .book-footer {
        position: inherit;
        bottom: 0%;
        display: flex;
        width: 100%;
        justify-content: space-between;

        button {
            padding: 10px 10px;
            margin: 0px 5px 0px 5px;
            border: 1px solid $green;
            background-color: $green;
            border-radius: 5px;
            width: 100%;
            color: white;
            font-size:1.4rem;

            &:hover {
                background-color: $lite-green;
                border: 1px solid $green;
            }
        }
    }
}

</style>

<script setup>
import { ref, reactive, watch, onMounted } from "vue";
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

const nav = ref([
{ type:'', name: "전체" },
{ type:'pa', name: "항공권" },
{ type:'ph', name: "호텔" },
{ type:'pr', name: "음식점" },
{ type:'pt', name: "교통" },
{ type:'pe', name: "기타" },
]);

const goBook = ()=>{
    scheduleStore.getscheduleList(travelId.value)
    router.push({name:"book"})
}

const postPlan = async ()=>{
    const response = await bookStore.postBook(travelId.value,planType.value)
    
    if(response.code !== 200) alert('등록에 실패 했습니다.')
    else goBook()
}

const putPlan =async ()=>{
    const response = await bookStore.putBook(travelId.value,planType.value)
    
    if(response.code !== 200) alert('수정에 실패 했습니다.')
    else goBook()
}

const delPlan =async ()=>{
    const response = await bookStore.delBook(travelId.value,planType.value)
    
    if(response.code !== 200) alert('삭제에 실패 했습니다.')
    else goBook()
}


onMounted(() => {
    console.log("Mounted!");
    if(planId.value && !isNaN(planId.value))bookStore.getBook(travelId.value,planId.value,planType.value)
})
</script>
