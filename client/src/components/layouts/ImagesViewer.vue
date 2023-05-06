<template>

<div class="photo-scroll">
    <span v-for="obj,index in fileBase64List">
        <font-awesome-icon v-if="!disabled" icon="fa-solid fa-xmark" class="del-icon" @click="delFileBase64(obj)" />
        <img  class="photo-scroll-image" :src="obj.url" @click="activeOverlay(index)"/>
    </span>
    <span v-for="url,index in urlList">
        <font-awesome-icon v-if="!disabled" icon="fa-solid fa-xmark" class="del-icon" @click="delUrl(url)" />
        <img class="photo-scroll-image" :src="url" @click="activeOverlay(fileBase64List.length+index)">
    </span>
    <span v-for="obj,index in thumbNailList">
        <font-awesome-icon v-if="!disabled" icon="fa-solid fa-xmark" class="del-icon" @click="delThumbNail(obj)" />
        <img class="photo-scroll-image" :src="obj.url" @click="activeOverlay(fileBase64List.length+urlList.length+index)">
    </span>
</div>

<div id="overlay" :class="{active:overlayState}" class="col">
    <div id="overlay-empty" @click="closeOverlay"></div>
    <div id="iamge-carousel">
        <v-carousel  v-if="overlayState" v-model="overlayCarouselState"
        :continuous="false"
        :show-arrows="false"
        hide-delimiter-background
        delimiter-icon="mdi-square"
        style="height:100%"
        >
            <v-carousel-item
                @click="closeOverlay"
                v-for="url in allUrlList"
                :src="url"
            ></v-carousel-item>
        </v-carousel>
    </div>
</div>

</template>

<style lang="scss" scoped>
.photo-scroll {
    overflow-x: scroll;
    white-space: nowrap;
    transition: all ease 0.5s 0s;

    .photo-scroll-image {
        border-radius: 10px;
        height: v-bind(imageSize);
    }

    span{
        position: relative;
        margin-right: 1rem;

        .del-icon{
            cursor: pointer;
            position: absolute;
            z-index: 10000;
            right:0;
            top:50%;
            transform: translate(20%, -50%);
            background-color: white;
            border-radius: 50%;
            border:1px solid $gray;
            width: 2rem;
            height: 2rem;
            color:gray;
        }
    }
}

#overlay{
    position: absolute;
    z-index: 110000;
    background-color: rgba(0, 0, 0, 0.164);
    width: 100%;
    height: 0;
    bottom:0;
    left:0;
    overflow: hidden;

    &.active{
        height: 100vh;
    }

    #overlay-empty{
        height: 100%;
    }

    #iamge-carousel{
        position:absolute;
        bottom:0px;
        width:100%;
        height:100%;
    }

}


</style>

<script setup>

import { computed, onMounted, ref } from 'vue';


const props = defineProps({urlList:Array, thumbNailList:Array,  fileBase64List:Array, imageSize:String, disabled:Boolean, noDetail:Boolean})
const emit = defineEmits(["delUrl","delFileBase64","delThumbNail"])

const urlList = computed(()=>props.urlList || [])
const fileBase64List = computed(()=>props.fileBase64List || [])
const thumbNailList = computed(()=>props.thumbNailList || [])
const disabled = computed(()=>props.disabled)
const imageSize = computed(()=>props.imageSize||"auto")
const noDetail = computed(()=>props.noDetail)

const allUrlList = computed(()=>[...(fileBase64List.value.map((obj)=>obj.url)),...urlList.value,...thumbNailList.value.map((obj)=>obj.originalUrl || obj.url)])

const overlayState = ref(false)
const overlayCarouselState = ref(0)
const activeOverlay = (index)=>{
    if(noDetail.value === true) return
    overlayState.value = true
    overlayCarouselState.value = index 
}

const closeOverlay = ()=>{
    overlayState.value = false
}

const delUrl = (url)=>{
    emit('delUrl',url)
}

const delFileBase64 = (fileBase64)=>{
    emit('delFileBase64',fileBase64)
}

const delThumbNail = (thumbNail)=>{
    emit('delThumbNail',thumbNail)
}

onMounted(()=>{

})


</script>