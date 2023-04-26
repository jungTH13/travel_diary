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


    const _checkMainSub = ()=>{
        if(document.getElementById('main-sub')?.clientHeight>0){
            mainSubVisible.value = true
            console.log('main-sub 시각화')
        }
        else mainSubVisible.value = false
    }
    const initMainSubLisner=()=> initWindowEventListener('resize',_checkMainSub)


    

    return { 
        mainSubVisible,
        initMainSubLisner 
    }

})
