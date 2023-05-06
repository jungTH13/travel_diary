<template>
 
<label id="image-add-container">
    <slot>

    </slot>
    <div v-if="!useSlot" id="image-input-icon">
        <font-awesome-icon icon="fa-solid fa-plus" class="icon" />
    </div>
    <input type="file" multiple id="fileUpload" @change="addImages" :disabled="isUploading">
    <span v-if="!useSlot">{{title}}</span>
</label>

</template>

<style lang="scss">
#image-add-container{
    cursor: pointer;
    display: flex;
    vertical-align: middle;
    width: 20rem;

    #image-input-icon{
        // margin:auto;
        width: 2.5rem;
        height: 2.5rem;
        border-radius: 50%;
        background-color: rgb(212, 212, 212);
        margin-right: 1rem;
        text-align: center;
        margin:auto;
        margin-left: 0;
        margin-right: 1rem;

        
        .icon{
            width: 2rem;
            height: 2rem;
            text-align: center;
            margin-top:0.2rem;

        }

        
    }

    span{
        font-size: 2rem;
        font-weight: 600;
    }

    input{
        display: none;
    }
}
</style>

<script setup>

import { computed, onMounted, ref } from 'vue';
import {HeicToJpeg} from '../../composable/util'


const props = defineProps({modelValue:Array,useSlot:Boolean,title:String}) // [{url:String,file:File}]
const emit = defineEmits(["update:modelValue"])

const useSlot = computed(()=>props.useSlot)
const isUploading = ref(false)
const imageBase64List = computed(()=>props.modelValue)
const title = computed(()=>props.title||"이미지 추가하기")

const addImages = async(common)=>{
    try{
        isUploading.value = true

        const fileList = common.target.files
        const imageReader = new FileReader()
        let newImageBase64List = []
        let itr = 0

        for(let file of fileList){
            if(file.type.split('/')[0] !== 'image') {
                isUploading.value = false
                return alert("이미지 파일만 업로드 가능합니다!")
            }
            if(['HEIC','heic'].includes(file.type.split('/')[1])) file = await HeicToJpeg(file)
        }

        if(fileList?.length===0) return isUploading.value = false

        

        imageReader.onload = (e)=>{
            newImageBase64List.push({
                file : fileList[itr],
                url : e.target.result
            })
            itr+=1

            if(itr<fileList?.length) imageReader.readAsDataURL(fileList[itr])
            else {
                emit('update:modelValue',[...imageBase64List.value,...newImageBase64List]) 
                isUploading.value = false
            }
        }

        imageReader.readAsDataURL(fileList[itr])
    }
    catch(error){
        isUploading.value = false
        throw error
    }
}

onMounted(()=>{

})


</script>