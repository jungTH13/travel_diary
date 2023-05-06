<template>
<div id="image-box" >
        <ImagesUploader v-model="bookStore.uploadImageList" />
    <div id="image-show" >
        <ImagesViewer :file-base64-list="bookStore.uploadImageList" :url-list="imageUrlList" @del-file-base64="delUploadImageList" @del-url="delBookImageList" image-size="8rem" />
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
const imageUrlList = computed(()=>bookStore.book.thumbNailList?.map((thumbNail)=>thumbNail.url)||[])

const delUploadImageList = (target)=>{
    bookStore.uploadImageList = bookStore.uploadImageList.filter((image)=>image!==target)
}
const delBookImageList = (target)=>{
    bookStore.book.thumbNailList = bookStore.book.thumbNailList.filter((image)=>{
        if(image.url!==target) return true

        bookStore.delImageList.push(image)
        return false
    })
    
}

</script>