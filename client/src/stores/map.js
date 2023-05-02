import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useGoogleMapApi } from "../composable/useGoogleMapApi";





export const useMapStore = defineStore("map", () => {
    let dailyMarkerList = [];
    let markerList = [];
    let poligonline = null
    const markerListChange = ref(false)
    const svgicons = {
        'pt':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'pa':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'pr':"M32 32C32 14.3 46.3 0 64 0H320c17.7 0 32 14.3 32 32s-14.3 32-32 32H290.5l11.4 148.2c36.7 19.9 65.7 53.2 79.5 94.7l1 3c3.3 9.8 1.6 20.5-4.4 28.8s-15.7 13.3-26 13.3H32c-10.3 0-19.9-4.9-26-13.3s-7.7-19.1-4.4-28.8l1-3c13.8-41.5 42.8-74.8 79.5-94.7L93.5 64H64C46.3 64 32 49.7 32 32zM160 384h64v96c0 17.7-14.3 32-32 32s-32-14.3-32-32V384z",
        'ph':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
    }

    const googleMapApi = useGoogleMapApi()

    class MakerBuf {

        constructor(){
            this.itr = 0
        }

        getMarker(label){
            while(this.itr >= markerList.length){
                markerList.push(googleMapApi.setSvgMarker(0,0,false,svgicons['pr'],label))
            }
            return markerList[this.itr++]
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

    /**
     * 
     * @param {Array} dailyScheduleList 스케쥴의 날자별 정렬된 리스트
     * @param {Array} dailyScheduleVisibleList 날자별 시각화 여부를 담은 리스트 [bool,]
     * @param {String} planTypeVisible 시각화할 여행 타입 입력
     */
    async function setDailyMarkerList(dailyScheduleList,dailyScheduleVisibleList,planTypeVisible=false) {
        dailyMarkerList = []
        markerList.forEach(marker=>googleMapApi.removeMarker(marker))
        markerList = []
        if(poligonline) poligonline.setMap(null)
        
        const poligonCoordinateList = []
        const markerBuf = new MakerBuf()
        const bounds = new Bounds()

        for(let index =0;index <dailyScheduleList?.length;index++){
            const scheduleList = dailyScheduleList[index]
            
            const dayMarkerList = []
            for(const plan of scheduleList){
                if(!plan.x || !plan.y || 
                    (dailyScheduleVisibleList?.length && !dailyScheduleVisibleList[index]) ||
                    (planTypeVisible && planTypeVisible !== plan.type)) {
                    
                        dayMarkerList.push(null)
                        continue
                }
                bounds.add(plan.x,plan.y)
                poligonCoordinateList.push({lat: plan.x, lng: plan.y})
                
                // 교통 관련 plan시 좌표가 두개 존재
                if(plan.type === 'pa' || plan.type === 'pt'){
                    if(!plan.x2 || !plan.y2) {
                        dayMarkerList.push(null)
                        continue
                    }
                    bounds.add(plan.x2,plan.y2)
                    poligonCoordinateList.push({lat: plan.x2, lng: plan.y2})
                    
                    const marker1 = markerBuf.getMarker()
                    const marker2 = markerBuf.getMarker()
                    const info1 = googleMapApi.createInfoWindow(plan.departLocation,_createInfoForm(plan.departLocation,plan.cId))
                    const info2 = googleMapApi.createInfoWindow(plan.arriveLocation,_createInfoForm(plan.arriveLocation,plan.cId2))
                    googleMapApi.moveMarker(marker1,plan.x,plan.y,false)
                    googleMapApi.moveMarker(marker2,plan.x2,plan.y2,false)
                    googleMapApi.setMarkerInfo(marker1,info1,'mousehover')
                    googleMapApi.setMarkerInfo(marker2,info2,'mousehover')
                    
                    dayMarkerList.push([marker1,marker2])
                }
                else{
                    const marker = markerBuf.getMarker()
                    const info = googleMapApi.createInfoWindow(plan.name,_createInfoForm(plan.name,plan.cId))
                    googleMapApi.moveMarker(marker,plan.x,plan.y,false)
                    googleMapApi.setMarkerInfo(marker,info,'mousehover')
                    
                    
                    dayMarkerList.push(marker)
                }
            }
            dailyMarkerList.push(dayMarkerList)
        }
        window.markerList = markerList

        //폴리곤 라인 생성성
        poligonline = googleMapApi.setPoligonLine(poligonCoordinateList)
        
        //마커 변경 전파
        markerListChange.value = !markerListChange.value
        console.log("markerListChange:",markerListChange.value)
        
        //변경 및 마커가 존재할 경우 맵 이동
        if(!bounds.getCount()) return
        const {minX,minY,maxX,maxY} = bounds.resolve()
        googleMapApi.setMapLatLngBounds(minX,minY,maxX,maxY)
    }

    function _createInfoForm (name,cid){
        return `
        <div id="content">
            <div id="siteNotice">
            </div>
            <h1 id="firstHeading" class="firstHeading" style="font-size: larger; font-weight: 600;">${name}</h1>
            <div id="bodyContent">
                    
            </div>
        </div>
        `
    }

    function removeMarkerAll (){
        dailyMarkerList = []
        markerList.forEach(marker=>googleMapApi.removeMarker(marker))
        markerList = []
    }

    function getdailyMarkerListObj(){
        return dailyMarkerList
    }

    return {
        dailyMarkerList,
        markerList,
        setDailyMarkerList,
        removeMarkerAll,
        getdailyMarkerListObj,
        markerListChange
    };
});
