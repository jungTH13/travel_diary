import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useGoogleMapApi } from "../composable/useGoogleMapApi";





export const useMapStore = defineStore("map", () => {
    let dailyMarkerList = [];
    let markerList = [];
    const svgicons = {
        'pt':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'pa':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
        'pr':"M215.7 499.2C267 435 384 279.4 384 192C384 86 298 0 192 0S0 86 0 192c0 87.4 117 243 168.3 307.2c12.3 15.3 35.1 15.3 47.4 0zM192 128a64 64 0 1 1 0 128 64 64 0 1 1 0-128z",
        'ph':"M416 0C400 0 288 32 288 176V288c0 35.3 28.7 64 64 64h32V480c0 17.7 14.3 32 32 32s32-14.3 32-32V352 240 32c0-17.7-14.3-32-32-32zM64 16C64 7.8 57.9 1 49.7 .1S34.2 4.6 32.4 12.5L2.1 148.8C.7 155.1 0 161.5 0 167.9c0 45.9 35.1 83.6 80 87.7V480c0 17.7 14.3 32 32 32s32-14.3 32-32V255.6c44.9-4.1 80-41.8 80-87.7c0-6.4-.7-12.8-2.1-19.1L191.6 12.5c-1.8-8-9.3-13.3-17.4-12.4S160 7.8 160 16V150.2c0 5.4-4.4 9.8-9.8 9.8c-5.1 0-9.3-3.9-9.8-9L127.9 14.6C127.2 6.3 120.3 0 112 0s-15.2 6.3-15.9 14.6L83.7 151c-.5 5.1-4.7 9-9.8 9c-5.4 0-9.8-4.4-9.8-9.8V16zm48.3 152l-.3 0-.3 0 .3-.7 .3 .7z",
    }

    const googleMapApi = useGoogleMapApi()

    class MakerBuf {

        constructor(){
            this.itr = 0
        }

        getMarker(){
            while(this.itr >= markerList.length){
                markerList.push(googleMapApi.setSvgMarker(0,0,false,svgicons['pr']))
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
                
                // 교통 관련 plan시 좌표가 두개 존재
                if(plan.type === 'pa' || plan.type === 'pt'){
                    if(!plan.x2 || !plan.y2) {
                        dayMarkerList.push(null)
                        continue
                    }
                    bounds.add(plan.x2,plan.y2)
                    
                    const marker1 = markerBuf.getMarker()
                    const marker2 = markerBuf.getMarker()
                    const info1 = googleMapApi.createInfoWindow(plan.departLocation,_createInfoForm(plan.departLocation,plan.cId))
                    const info2 = googleMapApi.createInfoWindow(plan.arriveLocation,_createInfoForm(plan.arriveLocation,plan.cId2))
                    googleMapApi.moveMarker(marker1,plan.x,plan.y,false)
                    googleMapApi.moveMarker(marker2,plan.x2,plan.y2,false)
                    googleMapApi.setMarkerInfo(marker1,info1)
                    googleMapApi.setMarkerInfo(marker2,info2)
                    
                    dayMarkerList.push([marker1,marker2])
                }
                else{
                    const marker = markerBuf.getMarker()
                    const info = googleMapApi.createInfoWindow(plan.name,_createInfoForm(plan.name,plan.cId))
                    googleMapApi.moveMarker(marker,plan.x,plan.y,false)
                    googleMapApi.setMarkerInfo(marker,info)
                    
                    
                    dayMarkerList.push(marker)
                }
            }
            dailyMarkerList.push(dayMarkerList)
        }

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
                    ${!cid?'':`<a style="color:white !important;" href="https://maps.google.com/maps?z=16&t=m&hl=ko-KR&gl=US&mapclient=embed&cid=${cid?cid:''}">
                        <button style="background-color:green; color:white; margin:2px; padding:1rem; border-radius: 5px;">구글 지도</botton>
                    </a>`}
            </div>
        </div>
        `
    }

    function removeMarkerAll (){
        dailyMarkerList = []
        markerList.forEach(marker=>googleMapApi.removeMarker(marker))
        markerList = []
    }

    return {
        dailyMarkerList,
        setDailyMarkerList,
        removeMarkerAll
    };
});
