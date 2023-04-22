<template>
<div>
    <div class="book-contents-wrapper">
        <BookAirport v-if="planType==='pa'"/>
        <BookHotel v-if="planType === 'ph'"/>
        <BookRestaurant v-if="planType === 'pr'" v-model="book" />
        <BookTransport v-if="planType === 'pt'" v-model="book" />
        <BookEtc v-if="planType === 'pe'" v-model="book" />
    </div>
</div>
</template>

<style lang="scss" scoped>
.book-contents-wrapper{

}

</style>

<script setup>

import { computed, ref, watch } from "vue";
import { toAMPMString } from "../composable/util";
import { useBookStore } from "../stores/plan/book";
import BookAirport from "./layouts/book/BookAirport.vue";
import { useRoute } from "vue-router";
import BookHotel from "./layouts/book/BookHotel.vue";
import BookRestaurant from "./layouts/book/BookRestaurant.vue";
import BookTransport from "./layouts/book/BookTransport.vue";
import BookEtc from "./layouts/book/BookEtc.vue";

const route = useRoute()
const bookStore = useBookStore()

const book = computed(()=>bookStore.book)
const planType = computed(()=>route.params.planType)

</script>
          