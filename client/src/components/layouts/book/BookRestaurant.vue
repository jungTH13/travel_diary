<template>
<div>
    <div class="book">
        <div class="book-head">
            음식점 예약
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
                                <font-awesome-icon :icon="['fas', 'location-crosshairs']" class="icon" />
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
                                <font-awesome-icon icon="fa-solid fa-calendar-check" class="icon" />
                                <input type="text" placeholder="예약 날짜" v-model="book.date">
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

import { computed, ref, watch } from "vue";
import { toAMPMString } from "../../../composable/util";
import { useBookStore } from "../../../stores/plan/book";

const bookStore = useBookStore()

const book = computed(()=>bookStore.book)

</script>
            