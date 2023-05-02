<template>
<div>
    <div class="book">
        <div class="book-head">
            항공권 예약
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
                        <font-awesome-icon icon="fa-solid fa-plane-departure" class="icon"/>
                        출발지
                    </th>
                    <tr>
                        <td style="width:45%">
                            <div>
                                
                                <input type="text" placeholder="출발지" v-model="book.departLocation">
                                <MapLocationIcon :search-text="book.departLocation" v-model="departMapResult" :width="iconWidth" :height="iconheight" />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <div class="date-picker">
                                    <DateTime v-model="book.departDate" placeholder="시간 입력하기" />
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <th>
                        <font-awesome-icon icon="fa-solid fa-plane-arrival" class="icon" />
                        도착지
                    </th>
                    <tr>
                        <td>
                            <div>
                                <input type="text" placeholder="도착지" v-model="book.arriveLocation">
                                <MapLocationIcon :search-text="book.arriveLocation" v-model="arriveMapResult" :width="iconWidth" :height="iconheight" />
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <div class="date-picker">
                                    <DateTime v-model="book.arriveDate" placeholder="시간 입력하기" />
                                </div>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <th>
                        <font-awesome-icon icon="fa-solid fa-ticket" class="icon" />
                        상세정보
                    </th>
                    <tr>
                        <td><div class="sub-title">항공사와 편명</div></td>
                        
                    </tr>
                    <tr>
                        <td><input type="text" placeholder="편명" v-model="book.flightNumber"></td>
                        <td><input type="text" placeholder="항공사" v-model="book.airline"></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                       <td><div class="sub-title"> 탑승시간과 좌석</div></td>
                    </tr>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-regular fa-clock" class="icon" />
                                <div class="date-picker">
                                    <TimeOnly v-model="book.boardingTime" placeholder="탑승시간" />
                                </div>
                            </div>
                        </td>
                        <td><input type="text" placeholder="좌석" v-model="book.seatNumber"></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                       <td><div class="sub-title"> 탑승구와 터미널</div></td>
                    </tr>
                    <tr>
                        <td><input type="text" placeholder="탑승구" v-model="book.boardingGate"></td>
                        <td><input type="text" placeholder="터미널" v-model="book.terminal"></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <th>
                        <font-awesome-icon icon="fa-solid fa-book-open-reader" class="icon" />
                        예약 번호
                    </th>
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
import TimeOnly from "../TimeOnly.vue";
import MapLocationIcon from "../MapLocationIcon.vue";

const bookStore = useBookStore()


const book = computed(()=>bookStore.book)
const departMapResult = ref({})
const arriveMapResult =ref({})
const iconWidth = '1.8rem'
const iconheight = '1.8rem'

const init = ()=>{
    if(book.value.x && book.value.y){
        departMapResult.value.geometry = [book.value.x, book.value.y]
        departMapResult.value.name = book.value.departLocation
        departMapResult.value.cid = book.value.cid
    }
    if(book.value.x2 && book.value.y2){
        arriveMapResult.value.geometry = [book.value.x2, book.value.y2]
        arriveMapResult.value.name = book.value.arriveLocation
        arriveMapResult.value.cid = book.value.cid2
    }
}

watch(()=>arriveMapResult.value,()=>{
    book.value.arriveLocation = arriveMapResult.value.name
    book.value.x2 = arriveMapResult.value.geometry[0]
    book.value.y2 = arriveMapResult.value.geometry[1]
    book.value.cid2 = arriveMapResult.value.cid
})

watch(()=>departMapResult.value,()=>{
    book.value.departLocation = departMapResult.value.name
    book.value.x = departMapResult.value.geometry[0]
    book.value.y = departMapResult.value.geometry[1]
    book.value.cid = departMapResult.value.cid
})

watch(()=>book.value,()=>init())

onMounted(()=>{
    init()
})

</script>
            