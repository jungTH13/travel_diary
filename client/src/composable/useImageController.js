import heic2any from "heic2any"
import { ref } from "vue"

export const useImageController = ()=>{

    class ImageController{

        constructor(){
            this.isUploading = ref(false)
            this.resultsList = ref([])
        }
        
        /**
         * 
         * @param {Object} common input type = file의 이벤트객체
         * @returns {[{file:File,url:String}]}
         * 
         * input 객체에 chang 이벤트에 연결하여 사용
         * 입력 받은 file 객체와 매핑되는 base64Url 정보를 리스트로 반환
         */
        addImages = async(common)=>new Promise(async(resolve,reject)=>{
            try{
                this.isUploading.value = true

                const fileList = common.target.files
                const resultsList = []
                const imageReader = new FileReader()
                let itr = 0

                for(let file of fileList){
                    if(file.type.split('/')[0] !== 'image') return alert("이미지 파일만 업로드 가능합니다!")
                    if(['HEIC','heic'].includes(file.type.split('/')[1])) file = await HeicToJpeg(file)
                }

                if(itr===0) return 

                imageReader.readAsDataURL(fileList[itr])

                imageReader.onload = (e)=>{
                    resultsList.push({
                        file : fileList[itr],
                        url : e.target.result
                    })
                    itr+=1

                    if(itr<fileList?.length) imageReader.readAsDataURL(fileList[itr])
                    else {
                        this.resultsList.value = [...this.resultsList.value,...resultsList]
                        resolve(resultsList)
                    }
                }
            }
            finally{
                this.isUploading.value = false
            }
        })

        /**
         * 
         * @param {File} file 
         * @returns {File}
         * 
         * 아이폰 전용 이미지 확장자 파일을 jpeg 이미지로 변환
         */
        HeicToJpeg = async(file)=>{
            try{
                const JpegBlob = await heic2any({
                    blob:file,
                    toType: 'image/jpeg'
                })
    
                const name = file.name.split('.')[0] + '.jpeg'
                const newFile = new File([JpegBlob],name)
    
                return newFile
            }
            catch(error){
                if(error.code === 1) {
                    console.log(`${file.name} 파일의 확장자가 올바르지 않습니다!`)
                    return file
                }
                throw error
            }
        }

    }
    

    

    return{
        ImageController
    }
}