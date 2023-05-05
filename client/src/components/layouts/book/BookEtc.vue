<template>
<div>
    <div class="book">
        <div class="book-head">
            기타
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
                        <font-awesome-icon icon="fa-solid fa-location-dot" class="icon"/>
                        장소
                    </th>
                    <tr>
                        <td>
                            <div>
                                <input type="text" placeholder="이름" v-model="book.name">
                            </div>
                        </td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td>
                            <div>
                                <input type="text" placeholder="주소" v-model="book.address">
                                <MapLocationIcon :search-text="searchText" :is-registration="true" v-model="mapResult" :width="iconWidth" :height="iconheight"/>
                            </div>
                        </td>
                    </tr>
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
                                    <DateTime v-model="book.reservationDate" placeholder="예약 날짜" />
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <th>
                    <font-awesome-icon icon="fa-solid fa-book-open-reader" class="icon" />
                    예약 번호
                </th>
                <tbody>
                    <tr>
                        <td><input type="text" placeholder="예약 번호" v-model="book.reservationNumber"></td>
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
@import "../../../assets/scss/components/book.scss";
</style>

<script setup>

import { computed, onMounted, ref, watch } from "vue";
import { toAMPMString } from "../../../composable/util";
import { useBookStore } from "../../../stores/plan/book";
import DateTime from "../DateTime.vue";
import MapLocationIcon from "../MapLocationIcon.vue";
import BookImageController from "./BookImageController.vue";

const bookStore = useBookStore()

const book = computed(()=>bookStore.book)
const searchText = computed(()=> `${book.value.address?book.value.address:''} ${book.value.name?book.value.name:''}`)
const mapResult = ref({})
const iconWidth = '3rem'
const iconheight = '3rem'

const init = ()=>{
    if(book.value.x && book.value.y){
        mapResult.value.geometry = [book.value.x, book.value.y]
        mapResult.value.name = book.value.name
        mapResult.value.cid = book.value.cid
    }
}

watch(()=>mapResult.value,()=>{
    book.value.name = mapResult.value.name
    book.value.address = mapResult.value.address.join(' ')
    book.value.x = mapResult.value.geometry[0]
    book.value.y = mapResult.value.geometry[1]
    book.value.cid = mapResult.value.cid
})

watch(()=>book.value,()=>init())

onMounted(()=>{
    init()
})
</script>
            