import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useGoogleMapApi } from "../composable/useGoogleMapApi";
import { findPlanofDate } from "../composable/util";





export const useMapStore = defineStore("map", () => {
    // let dailyMarkerList = []; //여행 일자 길이를 가지dalayPlan과 매칭되어 보관되는 2dArray
    let markerList = []; //등록된 모든 마커를 보관 1dArray
    const markerListMappingPlanList = ref([]) //등록된 모든 마커와 매칭되는 플랜 리스트트
    let poligonline = null
    const lastClickMarkerIndex = ref(0) 
    const markerListChange = ref(false)
    const svgicons = {
        'pt':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'pa':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'pr':"M32 32C32 14.3 46.3 0 64 0H320c17.7 0 32 14.3 32 32s-14.3 32-32 32H290.5l11.4 148.2c36.7 19.9 65.7 53.2 79.5 94.7l1 3c3.3 9.8 1.6 20.5-4.4 28.8s-15.7 13.3-26 13.3H32c-10.3 0-19.9-4.9-26-13.3s-7.7-19.1-4.4-28.8l1-3c13.8-41.5 42.8-74.8 79.5-94.7L93.5 64H64C46.3 64 32 49.7 32 32zM160 384h64v96c0 17.7-14.3 32-32 32s-32-14.3-32-32V384z",
        'ph':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'de':"M32 32C32 14.3 46.3 0 64 0H320c17.7 0 32 14.3 32 32s-14.3 32-32 32H290.5l11.4 148.2c36.7 19.9 65.7 53.2 79.5 94.7l1 3c3.3 9.8 1.6 20.5-4.4 28.8s-15.7 13.3-26 13.3H32c-10.3 0-19.9-4.9-26-13.3s-7.7-19.1-4.4-28.8l1-3c13.8-41.5 42.8-74.8 79.5-94.7L93.5 64H64C46.3 64 32 49.7 32 32zM160 384h64v96c0 17.7-14.3 32-32 32s-32-14.3-32-32V384z",
    }
    const dailyScheduleVisibleList = ref([])
    const nowTap = ref({})
    const isTrace = ref(true)
    const isCurrentPositionTrace = ref(false)
    

    const googleMapApi = useGoogleMapApi()

    class MakerBuf {

        constructor(){
            this.itr = 0
        }
        /**
         * 
         * @param {'de'|'pt'|'pa'|'pr'|'ph'} iconType svg 마커 타입 설정
         * @returns 
         * 
         * 해당 메소드는 svg마커만을 반환함
         */
        getMarker(iconType='de'){
            while(this.itr >= markerList.length){
                markerList.push( googleMapApi.setSvgMarker(0,0,false,svgicons[iconType]))
            }
            return markerList[this.itr++]
        }

        getSetMarker(x,y,path,markerType='svg'|'image',title=''){
            let newMarker = null
            if(markerType === 'image') newMarker = googleMapApi.setImageMarker(x,y,false,path,title)
            else newMarker = googleMapApi.setSvgMarker(x,y,false,path)

            if(markerList[this.itr])markerList[this.itr] = newMarker
            else markerList.push(newMarker)

            this.itr++
            
            return newMarker
        }

        done(start){
            for(;this.itr<markerList.length;this.itr++){
                console.log((new Date()) - start)
                googleMapApi.moveMarker(markerList[this.itr],0,0,false)
            }
            this.itr = 0
        }
    }

    class Bounds {
        constructor(){
            this.minX= 360
            this.minY= 360
            this.maxX= 0
            this.maxY= 0
            this.count = 0
        }

        add(x,y){
            this.count++

            if(this.minX>x)this.minX = x
            if(this.maxX<x)this.maxX = x
            if(this.minY>y)this.minY = y
            if(this.maxY<y)this.maxY = y
            
            return this
        }

        resolve(){
            return {
                minX:this.minX,
                minY:this.minY,
                maxX:this.maxX,
                maxY:this.maxY
            }
        }

        getCount(){
            return this.count
        }
    }

    function reset (){
        // dailyMarkerList = []
        markerList.forEach(marker=>googleMapApi.removeMarker(marker))
        markerList = []
        markerListMappingPlanList.value = []
        if(poligonline) poligonline.setMap(null)
    }

    function getSetLastClickMarkerIndexFunc(index){

        return ()=>{
            console.log('markerclick',index)
            lastClickMarkerIndex.value = index
        }

    }

    /**
     * 
     * @param {Array} dailyScheduleList 스케쥴의 날자별 정렬된 리스트
     * 
     * @param {Array} dailyScheduleVisibleList 날자별 시각화 여부를 담은 리스트 [bool,]
     * @param {String} planTypeVisible 시각화할 여행 타입 입력
     * @param {boolean} isTrace 생성된 마커로 화면을 이동할지 여부
     */
    async function setDailyMarkerList(dailyScheduleList,dailyImageGroupList,dailyScheduleVisibleList,planTypeVisible=false,isTrace=true) {
        reset()
        
        const poligonCoordinateList = []
        const markerBuf = new MakerBuf()
        const bounds = new Bounds()

        let planList = []

        const max = dailyScheduleVisibleList.length || dailyScheduleList.length

        for(let index=0;index<max;index++){
            const scheduleList = dailyScheduleList[index] || []
            const imageList = dailyImageGroupList[index] || []

            if(!dailyScheduleVisibleList.length || dailyScheduleVisibleList[index]){
                planList = [...planList,...scheduleList,...imageList]
            }
        }
        

        planList.sort((a,b)=>{
            const aDate = findPlanofDate(a)
            const bDate = findPlanofDate(b)
            if(aDate > bDate) return 1
            if(aDate < bDate) return -1
            return 0
        })

        _DailyMarkerListProcess({
            planList,
            planTypeVisible,
            poligonCoordinateList,
            markerBuf,
            bounds
        })
        window.markerList = markerList

        //폴리곤 라인 생성
        poligonline = googleMapApi.setPoligonLine(poligonCoordinateList)
        
        //마커 변경 전파
        markerListChange.value = !markerListChange.value
        console.log("markerListChange:",markerListChange.value)
        
        //변경 및 마커가 존재할 경우 맵 이동
        if(!bounds.getCount()||!isTrace) return
        const {minX,minY,maxX,maxY} = bounds.resolve()
        googleMapApi.setMapLatLngBounds(minX*0.996,minY,maxX,maxY)

    }

    function _DailyMarkerListProcess({
        planList,
        planTypeVisible,
        poligonCoordinateList,
        markerBuf,
        bounds
    }){

        function markerProcessing(x,y,cId,path,plan){

            bounds.add(x,y)
            poligonCoordinateList.push({lat: x, lng: y})
            // const marker = markerBuf.getSetMarker(x,y,path,type) 
            const info = googleMapApi.createInfoWindow(plan.name,_createInfoForm(plan.name,cId))
            let marker = null
            if(plan.type === 'pig') 
                marker = markerBuf.getSetMarker(x,y,path,'image',plan.title||'') 
            else 
                marker = markerBuf.getSetMarker(x,y,path,'svg')
            
            if(plan.type !=='pig') googleMapApi.setMarkerInfo(marker,info,'mousehover')

            markerListMappingPlanList.value.push(plan)
            googleMapApi.setMarkerEvent(marker,getSetLastClickMarkerIndexFunc(markerListMappingPlanList.value.length-1),plan.type === 'pig'?'gmp-click':'click')
        }

        for(let index =0;index <planList?.length;index++){
            const plan = planList[index]

            if(planTypeVisible && planTypeVisible !== plan.type) continue

            //마커 좌표가 두개 존재하는 플랜
            if(plan.type === 'pa' || plan.type === 'pt'){
                if(!plan.x || !plan.y || !plan.x2 || !plan.y2) continue
                
                markerProcessing(plan.x,plan.y,plan.cId,svgicons['de'],plan)
                
                while(index+1<planList.length && planList[index+1].type === 'pig' &&  plan.arriveDate>planList[index+1].date){
                    const newPlan = planList[index+1]
                    index+=1
                    if(!newPlan.x || !newPlan.y) continue
                    markerProcessing(newPlan.x,newPlan.y,newPlan.cId,newPlan.thumbNailList[0].url,newPlan)
                }
                // TO DO : 해당 마커가 지도상에 표시되지 않음 리스트는 표시됨, 해당부분 fix 필요
                markerProcessing(plan.x2,plan.y2,plan.cId,svgicons['de'],plan)
            }
            else{//마커 좌표가 하나만 존재하는 플랜
                if(!plan.x || !plan.y) continue

                if(plan.type==='pig') markerProcessing(plan.x,plan.y,plan.cId,plan.thumbNailList[0].url,plan)
                else markerProcessing(plan.x,plan.y,plan.cId,svgicons['de'],plan)
            }

        }

    }

    function _createInfoForm (name,cid){
        return `
        <div id="content">
            <div id="siteNotice">
            </div>
            <h1 id="firstHeading" class="firstHeading" style="font-size: larger; font-weight: 600;">${name||''}</h1>
            <div id="bodyContent">
            </div>
        </div>
        `
    }

    function removeMarkerAll (){
        // dailyMarkerList = []
        markerList.forEach(marker=>googleMapApi.removeMarker(marker))
        markerList = []
    }

    // function getdailyMarkerListObj(){
    //     return dailyMarkerList
    // }

    return {
        // dailyMarkerList,
        markerList,
        markerListMappingPlanList,
        lastClickMarkerIndex,
        setDailyMarkerList,
        removeMarkerAll,
        // getdailyMarkerListObj,
        markerListChange,

        //controll 상태 변수
        dailyScheduleVisibleList,
        nowTap,
        isTrace,
        isCurrentPositionTrace

    };
});
