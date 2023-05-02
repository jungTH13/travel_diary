<template>
<div class="plan-box-contents">
    <div :class="{mainDeactive:isBoxContentsClick}" class="plan-box-contents-main" @click="isBoxContentsClick = !isBoxContentsClick" >
        <slot name="main"></slot>
    </div>

    <div class="plan-box-contents-options" :class="{optionsActive: isBoxContentsClick}">
        <slot name="sub"></slot>
    </div>
</div>

</template>

<style lang="scss" scoped>

.plan-box-contents{
    height:100%;
    width:100%;
    border-radius: 3px;
    opacity: 0.9;
    background-color: white;
    box-shadow: 0.05rem 0.05rem 0.5rem 0rem gray;
    overflow: hidden;
    padding:1rem;

    

    .plan-box-contents-main{
        transition: all ease 1.5s 0s;
        // height: 100%;
        max-height:100vh;
        overflow: hidden;
    }

    .mainDeactive{
        transition: all ease 0s 0s;
        max-height:0vh;
        overflow: hidden;
        margin-bottom: 0rem;
    }

    .plan-box-contents-options{
        width:100%;
        max-height:0vh;
        transition: all ease 1.5s 0s;
        overflow: hidden;
    }

    .optionsActive{
        max-height:70vh;
        width:100%;
    }

}

</style>

<script setup>
import { ref } from 'vue';


const isBoxContentsClick = ref(false)

</script>