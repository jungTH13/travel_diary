<template>
<div>
    <div @click="toTravelCreate" id="add-plan-button">
        <span class="plus-button orange">
            <font-awesome-icon icon="fa-solid fa-plus" id="plus-button-img"/>
        </span>
        <span class="text-lg">여행일정 만들기</span>
    </div>
</div>
</template>

<style lang="scss" scoped>
#add-plan-button {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0 auto;
    cursor: pointer;

    > span:last-of-type {
        font-weight: bold;
    }
}

.plus-button {
    margin-right: 10px;
    cursor: pointer;
    #plus-button-img {
        width: 18px;
        height: 18px;
    }
}

</style>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter()

const toTravelCreate = ()=>{
    router.push({name: 'create1'})
}


</script>
  