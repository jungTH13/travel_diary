import axios from "axios"

let mapObj = null
let planMarkers = []

export const useGoogleMapApi = ()=>{
    const init = async()=>{
        
        const result = new Promise((resolve,reject)=>{
            if(window.google === undefined){
                window.google = false
                window.initMap = function(){
                    console.log("googleMap installed")
                }
                
                const script = document.createElement("script")
                /* global google */
                script.async = true
                script.src =`https://maps.googleapis.com/maps/api/js?key=${import.meta.env.VITE_GOOGLE_MAP_KEY}&callback=initMap`
                document.head.appendChild(script);
            }

            const installWait = ()=>{
                if(!window.google)setTimeout(()=>{
                    console.log('wait')
                    installWait()
                },100)
                else resolve()
            }

            installWait()
            
        })
        
        await result
    }
    /**
     * 
     * @param {String} searchText 검색할 문자을 입력
     * 
     * @retruns  좌표정보를 반환
     */
    const searchPlace = async (searchText)=>{
        const url = `https://www.google.com/maps/embed/v1/place?key=${import.meta.env.VITE_GOOGLE_MAP_KEY}&q=${searchText}`

        const response = await axios.get(url)

        let target = response.data.split("initEmbed(")[1]
        target = `{ "target": ${target.split(");")[0]} }`
        target = JSON.parse(target)

        const address = target["target"][21][3][2]
        const geometry = target["target"][21][3][0][2] // [lat,lng]
        const place_id = target["target"][21][3][27]
        const name = target["target"][21][3][1]
        const cid = target["target"][21][3][0][3]

        return {address,geometry,place_id,name,cid}
    }

    const searchAddr = ()=>{
        
    }

    const createMap = async (config,elementId='map')=>{

        let mapConfig = {
			center: {lat:36,lng:127}, //center로 할 위도, 경도를 지정한다.
			zoom: 16, //zoom size를 지정.
			maxZoom: 20,
			minZoom: 3,
			streetViewControl: false,
			mapTypeControl: false,
			fullscreenControl: false,
			zoomControl: false,
		}

        if(config) mapConfig = config

        return new window.google.maps.Map(document.getElementById(elementId),mapConfig)
    }

    /**
     * 
     * @param {Number} x lat
     * @param {Number} y lng
     * @param {String} elementId map객체를 넣을 요소 이름을 지정
     *  
     * @returns 
     */
    const getMap = async ()=>{
        if(mapObj !== null) {
            const result = await Promise.all([mapObj])
            
            //생성한 map이 있는경우 해닫 객체의 element를 reinstall
            const element = document.getElementById('map')
            const parentElement = element.parentElement
            parentElement.removeChild(element)
            parentElement.appendChild(mapObj.getDiv())

            return result[0]
        }

        mapObj = createMap().then(map=>{
            mapObj = map
            return map
        })

        return mapObj
    }

    /**
     * 
     * @param {Number} x lat
     * @param {Number} y lng
     * @param {Boolean} isTrace 마커 추적여부 
     * 
     * 입력된 좌표를 맵에 표시하고 화면을 전환합니다.
     */
    const setMarker = (x,y,isTrace)=>{
        if(mapObj === null) throw new Error("지도가 생성되어 있지 않습니다!")
        
        const marker = new window.google.maps.Marker({
            position: {lat:x,lng:y},
            map: mapObj,
        })
        if(isTrace === true) {
            mapObj.setCenter({lat:x,lng:y})
            mapObj.setZoom(16)
        }

        return marker
    }


    /**
     * 
     * @param {Number} x lat
     * @param {Number} y lng
     * @param {Boolean} isTrace 마커 추적여부 
     * 
     * 입력된 좌표를 맵에 표시하고 화면을 전환합니다.
     */
    const setAdvancedMarker = (x,y,isTrace)=>{
        // if(mapObj === null) throw new Error("지도가 생성되어 있지 않습니다!")
        
        // const pinViewBackground = new google.maps.marker.PinView({
        //     background: "#FBBC04",
        // })

        // const marker = new google.maps.marker.AdvancedMarkerView({
        //     map: mapObj,
        //     position: {lat:x,lng:y},
        //     content: pinViewBackground.element,
        // })
        // if(isTrace === true) {
        //     mapObj.setCenter({lat:x,lng:y})
        //     mapObj.setZoom(16)
        // }

        // return marker
    }


    const removeListeners = (marker,eventName)=>{
        window.google.maps.event.clearListeners(marker,eventName)
    }

    const removeMarker = (marker)=>{
        moveMarker(marker,0,0,false)
        marker.setMap(null)
    }

    const createInfoWindow = (label,content)=>{
        
        return new window.google.maps.InfoWindow({
            content: content,
            ariaLabel: label,
        })
    }



    /**
     * 
     * @param {Object} marker googleMap의 마커
     * @param {Number} x lat
     * @param {Number} y lng
     * @param {Boolean} isTrace 마커 추적여부 
     * 
     * @returns 
     */
    const moveMarker = (marker,x,y,isTrace)=>{
        if(!marker) {
            console.log("마커가 존재하지 않습니다!")
            return null
        }
        
        marker.setPosition({lat:x,lng:y})

        if(isTrace === true) {
            mapObj.setCenter({lat:x,lng:y})
            mapObj.setZoom(18)
        }
    }

    const moveMap = (x,y)=>{
        mapObj.setCenter({lat:x,lng:y})
        mapObj.setZoom(16)
    }


    const setPlanList = async (map,planList)=>{

        planList.forEach((plan)=>{
            const marker = setMarker()
            planMarkers.push()
        })

    }

    return{
        init,
        createMap,
        moveMap,
        getMap,
        setMarker,
        setAdvancedMarker,
        moveMarker,
        searchPlace,
        createInfoWindow,
        removeListeners,
        removeMarker
    }
}