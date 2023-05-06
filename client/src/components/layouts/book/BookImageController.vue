<template>
<div id="image-box" >
        <ImagesUploader v-model="bookStore.uploadImageList" />
    <div id="image-show" >
        <ImagesViewer :file-base64-list="bookStore.uploadImageList" :thumb-nail-list="book.thumbNailList" @del-file-base64="delUploadImageList" @del-thumb-nail="delThumbNailList" image-size="8rem" />
    </div>
</div>

</template>

<style lang="scss">

#image-box{
    width: 100%;

    #image-show{
        // max-height:8rem;
    }
}

</style>

<script setup>
import ImagesUploader from '../ImagesUploader.vue';
import ImagesViewer from '../ImagesViewer.vue';
import { useBookStore } from "../../../stores/plan/book";
import { computed } from 'vue';


const bookStore = useBookStore()

const book = computed(()=>bookStore.book)

const delUploadImageList = (target)=>{
    bookStore.uploadImageList = bookStore.uploadImageList.filter((image)=>image!==target)
}

const delThumbNailList = (target)=>{
    bookStore.book.thumbNailList = bookStore.book.thumbNailList.filter(image=>{
        if(image!==target) return true

        bookStore.delImageList.push(image)
        return false
    })
}

</script>