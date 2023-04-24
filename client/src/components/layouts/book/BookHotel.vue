<template>
<div>
    <div class="book">
        <div class="book-head">
            호텔 예약
        </div>
        <div class="book-contents full-hidden">
            <table>
                <tbody>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-book-bookmark" class="icon"/>
                                <input type="text" placeholder="타이틀" v-model="book.title">
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-hotel" class="icon"/>
                                <input type="text" placeholder="호텔 이름" v-model="book.name">
                            </div>
                        </td>
                    </tr>
                </tbody>
                <tbody>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-location-dot" class="icon"/>
                                <input type="text" placeholder="주소" v-model="book.address">
                                <MapLocationIcon :search-text="searchText" v-model="mapResult" :width="iconWidth" :height="iconheight" />
                                
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>


            <table>
                <tbody>
                    <tr>
                        <td class="width:50%">
                            <div>
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <div class="date-picker">
                                    <DateTime v-model="book.checkinDate" placeholder="체크인" />
                                </div>
                            </div>
                        </td>
                        <td class="width:50%">
                            <div>
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <div class="date-picker">
                                    <DateTime v-model="book.checkoutDate" placeholder="체크아웃" />
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                        <td><input type="text" placeholder="예약 번호" v-model="book.reservationNumber"></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                        <td><input type="text" placeholder="전화번호" v-model="book.phone"></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                        <td><textarea placeholder="메모" v-model="book.memo" style="border:0px;"></textarea></td>
                    </tr>
                </tbody>
            </table>

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

const bookStore = useBookStore()

const book = computed(()=>bookStore.book)
const searchText = computed(()=> `${book.value.address?book.value.address:''} ${book.value.name?book.value.name:''}`)
const mapResult = ref({})
const iconWidth = '1.8rem'
const iconheight = '1.8rem'

const init = ()=>{
    if(book.value.x && book.value.y){
        mapResult.value.geometry = [book.value.x, book.value.y]
    }
}

watch(()=>mapResult.value,()=>{
    book.value.name = mapResult.value.name
    book.value.address = mapResult.value.address.join(' ')
    book.value.x = mapResult.value.geometry[0]
    book.value.y = mapResult.value.geometry[1]
})

watch(()=>book.value,()=>init())

onMounted(()=>{
    init()
})

</script>
            