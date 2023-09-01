<template>
 
<label id="image-add-container">
    <slot>

    </slot>
    <div v-if="!useSlot" id="image-input-icon">
        <font-awesome-icon icon="fa-solid fa-plus" class="icon" />
    </div>
    <input type="file" multiple id="fileUpload" @change="addImages" :disabled="isUploading">
    <span v-if="!useSlot">{{(isUploading? `업로딩 중...${isUploading}`:title)}}</span>
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
import imageCompression from 'browser-image-compression';


const props = defineProps({modelValue:Array,useSlot:Boolean,title:String}) // [{url:String,file:File}]
const emit = defineEmits(["update:modelValue"])

const useSlot = computed(()=>props.useSlot)
const isUploading = ref(false)
const imageBase64List = computed(()=>props.modelValue)
const title = computed(()=>props.title||"이미지 추가하기")

const compressImage = async(file)=>{
    const options = {
        maxSizeMB: 0.3,
    }

    const compressFile = await imageCompression(file,options)

    return new File([compressFile],file.name)
}

const addImages = async(common)=>{
    try{

        const fileList = common.target.files
        const imageReader = new FileReader()
        let newImageBase64List = []
        let itr = 0
        isUploading.value = fileList.length

        for(let file of fileList){
            if(file.type.split('/')[0] !== 'image') {
                isUploading.value = 0
                return alert("이미지 파일만 업로드 가능합니다!")
            }
            if(['HEIC','heic'].includes(file.type.split('/')[1])) file = await HeicToJpeg(file)
        }

        if(fileList?.length===0) return isUploading.value = 0

        const compressFileList = {}
        for(let itr=0;itr<fileList.length;itr++){
            compressFileList[itr] = compressImage(fileList[itr])
        }

        imageReader.onload = async (e)=>{
            const compressFile = await compressFileList[itr]
            // console.log(fileList[itr])
            // console.log(compressFile)
            newImageBase64List.push({
                file : compressFile,
                url : e.target.result
            })
            itr+=1
            isUploading.value-=1

            if(itr<fileList?.length) imageReader.readAsDataURL(fileList[itr])
            else {
                emit('update:modelValue',[...imageBase64List.value,...newImageBase64List]) 
                isUploading.value = 0
            }
        }

        imageReader.readAsDataURL(fileList[itr])
    }
    catch(error){
        isUploading.value = 0
        throw error
    }
}

onMounted(()=>{

})


</script>