import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import { useGoogleMapApi } from "../composable/useGoogleMapApi";





export const useMapStore = defineStore("map", () => {
    let dailyMarkerList = [];
    let markerList = [];
    const googleMapApi = useGoogleMapApi()

    class MakerBuf {

        constructor(){
            this.itr = 0
        }

        getMarker(){
            while(this.itr >= markerList.length){
                markerList.push(googleMapApi.setMarker(0,0,false))
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
                
                // 교통 관련 plan시 좌표가 두개 존재
                if(plan.type === 'pa' || plan.type === 'pt'){
                    if(!plan.x2 || !plan.y2) {
                        dayMarkerList.push(null)
                        continue
                    }
                    
                    const marker1 = markerBuf.getMarker()
                    const marker2 = markerBuf.getMarker()
                    googleMapApi.moveMarker(marker1,plan.x,plan.y,false)
                    googleMapApi.moveMarker(marker2,plan.x2,plan.y2,false)
                    
                    dayMarkerList.push([marker1,marker2])
                }
                else{
                    const marker = markerBuf.getMarker()
                    googleMapApi.moveMarker(marker,plan.x,plan.y,false)
                    
                    dayMarkerList.push(marker)
                }
            }
            dailyMarkerList.push(dayMarkerList)
        }
       
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
