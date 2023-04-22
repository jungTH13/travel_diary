<template>
<div>
    <div class="book">
        <div class="book-head">
            항공권 예약
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
                        <td style="width:45%">
                            <div>
                                <font-awesome-icon icon="fa-solid fa-plane-departure" class="icon"/>
                                <input type="text" placeholder="출발지" v-model="book.departLocation">
                                <font-awesome-icon :icon="['fas', 'location-crosshairs']" class="icon" />
                            </div>
                        </td>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <input type="text" placeholder="시간" v-model="book.departDate">
                            </div>
                        </td>
                    </tr>
                </tbody>

                <tbody>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-plane-arrival" class="icon" />
                                <input type="text" placeholder="도착지" v-model="book.arriveLocation">
                                <font-awesome-icon :icon="['fas', 'location-crosshairs']" class="icon" />
                            </div>
                        </td>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <input type="text" placeholder="시간" v-model="book.arriveDate">
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                        <td><input type="text" placeholder="편명" v-model="book.flightNumber"></td>
                        <td><input type="text" placeholder="좌석" v-model="book.seatNumber"></td>
                        <td><input type="text" placeholder="항공사" v-model="book.airline"></td>
                    </tr>
                </tbody>
            </table>

            <table>
                <tbody>
                    <tr>
                        <td>
                            <div>
                                <font-awesome-icon icon="fa-regular fa-clock" class="icon" />
                                <input type="text" placeholder="탑승 시간" v-model="book.boardingTime">
                            </div>
                        </td>
                        <td><input type="text" placeholder="탑승구" v-model="book.boardingGate"></td>
                        <td><input type="text" placeholder="터미널" v-model="book.terminal"></td>
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

import { computed, ref, watch } from "vue";
import { toAMPMString } from "../../../composable/util";
import { useBookStore } from "../../../stores/plan/book";

const bookStore = useBookStore()

const book = computed(()=>bookStore.book)

</script>
            