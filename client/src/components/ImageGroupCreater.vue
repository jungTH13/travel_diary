<template>
<div>
    <div class="book">
        <div class="book-head">
            이미지 그룹
        </div>
        <div class="book-contents full-hidden">
            <table>
                <tbody>
                    <th>
                        <font-awesome-icon icon="fa-solid fa-book-bookmark" class="icon"/>
                        타이틀
                    </th>
                    <tr>
                        <td>
                            <div>
                                
                                <input type="text" placeholder="타이틀" v-model="book.title">
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <th>
                        장소 설정하기
                        <MapLocationIcon :search-text="searchText" :is-registration="true" v-model="mapResult" :width="iconWidth" :height="iconheight"/>
                    </th>
                </tbody>
            </table>


            <table>
                <tbody>
                    <th>
                        <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                        날짜
                    </th>
                    <tr>
                        <td>
                            <div>
                                <div class="date-picker">
                                    <DateTime v-model="book.date" placeholder="촬영 날짜" />
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <th>
                        메모
                    </th>
                    <tr>
                        <td><textarea placeholder="메모" v-model="book.memo" style="border:0px;"></textarea></td>
                    </tr>
                </tbody>
            </table>
            <BookImageController />
        </div>
    </div>
</div>
</template>

<style lang="scss" scoped>
@import "../assets/scss/components/book.scss";
</style>

<script setup>

import { computed, onBeforeMount, onMounted, ref, watch } from "vue";

import { useBookStore } from "../stores/plan/book";
import BookImageController from "./layouts/book/BookImageController.vue";
import DateTime from "./layouts/DateTime.vue";
import MapLocationIcon from "./layouts/MapLocationIcon.vue";
import { toKoreaTimeString } from "../composable/util";

const bookStore = useBookStore()

const book = computed(()=>bookStore.book)
const searchText = computed(()=> `${book.value.address?book.value.address:''} ${book.value.name?book.value.name:''}`)
const mapResult = ref({})
const iconWidth = '3rem'
const iconheight = '3rem'

const init = ()=>{
    if(book.value.x && book.value.y){
        mapResult.value.geometry = [book.value.x, book.value.y]
        mapResult.value.name = book.value.title
        mapResult.value.cid = book.value.cid
    }
    book.value.date = toKoreaTimeString(new Date()).split('.')[0];
}

watch(()=>mapResult.value,()=>{
    book.value.x = mapResult.value.geometry[0]
    book.value.y = mapResult.value.geometry[1]
    book.value.cid = mapResult.value.cid
    book.value.title = mapResult.value.name
})

onMounted(()=>{
    init()
})
</script>
            