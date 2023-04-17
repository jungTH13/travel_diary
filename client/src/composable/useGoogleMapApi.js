let mapObj = null

export const useGoogleMapApi = ()=>{
    const init = ()=>new Promise((resolve,reject)=>{
        if(!window.google){
			const script = document.createElement("script")
			/* global google */
            script.async = true
			script.src =`https://maps.googleapis.com/maps/api/js?key=${import.meta.env.VITE_GOOGLE_MAP_KEY}`
			document.head.appendChild(script);

            const installWait = ()=>{
                if(!window.google)setTimeout(()=>{
                    installWait()
                },100)
                else resolve()
            }

            installWait()
		}
    })
    /**
     * 
     * @param {String} searchText 검색할 문자을 입력
     * 
     * @retruns  좌표정보를 반환
     */
    const searchPlace = (searchText)=>{



    }

    const searchAddr = ()=>{
        
    }

    const createMap = async (config,elementId='map')=>{

        let mapConfig = {
			center: {lat:36,lng:127}, //center로 할 위도, 경도를 지정한다.
			zoom: 16, //zoom size를 지정.
			maxZoom: 20,
			minZoom: 3,
			streetViewControl: true,
			mapTypeControl: true,
			fullscreenControl: true,
			zoomControl: true,
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
    const getMap = ()=>{
        if(mapObj !== null) return mapObj

        mapObj = createMap()

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
        if(mapObj === null) return alert("지도가 생성되어 있지 않습니다!")
        
        const marker = new window.google.maps.Marker({
            position: {lat:x,lng:y},
            map: mapObj,
        })

        if(isTrace === true) mapObj.setCenter({lat:x,lng:y})

        return marker
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
        if(!marker) return alert("마커가 존재하지 않습니다!")
        
        marker.value.setPosition({lat:x,lng:y})

        if(isTrace === true) mapObj.setCenter({lat:x,lng:y})
    }

    return{
        init,
        createMap,
        getMap,
        setMarker,
        moveMarker
    }
}