import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

const initWindowEventListener = (eventName,targetFunc)=>{
    targetFunc()

    window.removeEventListener(eventName,targetFunc)
    window.addEventListener(eventName,targetFunc)
}

export const useCommonStore = defineStore('common', () => {
    // 메인화면 sub-map 활성화 상태 변수
    const mainSubVisible = ref(false)
    // 글로벌 맵에 대한 데이터 공유
    const mapInfo = ref({
        searchText:'',
        searchInfo: {},
        type:''
    })


    const checkMainSub = ()=>{
        if(document.getElementById('main-sub')?.clientWidth>0){
            mainSubVisible.value = true
            console.log('main-sub 시각화')
        }
        else mainSubVisible.value = false
    }
    const initMainSubLisner=()=> initWindowEventListener('resize',checkMainSub)


    

    return { 
        mainSubVisible,
        initMainSubLisner,
        checkMainSub,
        mapInfo 
    }

})
