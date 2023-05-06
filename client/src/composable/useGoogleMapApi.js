import axios from "axios"

let mapObj = null
let planMarkers = []
let mapEventListenerList = []

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
                script.src =`https://maps.googleapis.com/maps/api/js?key=${import.meta.env.VITE_GOOGLE_MAP_KEY}&v=beta&libraries=marker&callback=initMap`
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
            mapId:"50e7475cbacb137e",
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
     * @param {String} eventName 
     * @param {Function} func 
     * @returns 등록된 리스너 객체를 반환
     * 
     * 맵에서 일어나는 이벤트에 리스너를 생성
     */
    const addMapEventListener = (eventName="click",func)=>{
        const mapEventListener = google.maps.event.addListener(mapObj,eventName,func)

        return mapEventListener
    }

    const removeMapEventListener = (mapEventListener)=>{
        google.maps.event.removeListener(mapEventListener)
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

        const options = {
            position: {lat:x,lng:y},
            map: mapObj,
        }
        console.log('test')
        const marker = new window.google.maps.Marker(options)
        if(isTrace === true) {
            mapObj.setCenter({lat:x,lng:y})
            mapObj.setZoom(14)
        }

        return marker
    }

    const setImageMarker = (x,y,imageUrl='https://developers.google.com/maps/documentation/javascript/examples/full/images/parking_lot_maps.png')=>{
        const content = document.createElement("div");
        const style = `
        border-radius: 10px;
        border: 2px solid white;
        width:35px; 
        height:35px;
        `
        content.innerHTML = `
        <img style="${style}" src=${imageUrl}>
        `

        const markerView = new google.maps.marker.AdvancedMarkerView({
            map: mapObj,
            position: { lat: x, lng: y },
            content: content,
        });
        
        return markerView
    }

    const setPositionMarker = (x,y,isTrace=false)=>{
        if(mapObj === null) throw new Error("지도가 생성되어 있지 않습니다!")

        const options = {
            position: {lat:x,lng:y},
            map: mapObj,
            // label:{
            //     text:label,
            //     fontWeight:'600',
                
            // },
            icon:{
                path: "M256 512A256 256 0 1 0 256 0a256 256 0 1 0 0 512z",
                fillColor: 'blue',
                fillOpacity: 1,
                scale: 0.03,
                strokeColor: 'white',
                strokeWeight: 2,                
                anchor: new google.maps.Point(200, 500),
            },
        }
        
        const marker = new window.google.maps.Marker(options)
        if(isTrace === true) {
            mapObj.setCenter({lat:x,lng:y})
            mapObj.setZoom(15)
        }

        return marker
    }

    /**
     * 
     * @param {Object} marker 
     * @param {Object} info
     * @param {'click'|'mousehover'} type 
     * 
     * 마커 클릭,hover시 인포 정보 시각화 설정 
     */
    const setMarkerInfo = (marker,info,type='click')=>{
        if(type==='click'){
            marker.addListener(type,()=>{
                info.open({
                    anchor:marker,
                    map:mapObj
                })
            })
        }
        if(type==='mousehover'){
            marker.addListener('mouseover',()=>{
                info.open({
                    anchor:marker,
                    map:mapObj
                })
            })
            marker.addListener('mouseout',()=>{
                info.close()
            })
        }
        
    }

    /**
     * 
     * @param {Object} marker 
     * @param {Function} func
     * @param {'click'|'mousehover'} type 
     * 
     * 마커 클릭,hover시 실행 이벤트 설정 
     */
    const setMarkerEvent = (marker,func,type='click')=>{
        if(type==='click'){
            marker.addListener(type,func)
        }
        if(type==='mousehover'){
            marker.addListener('mouseover',func)
        }
    }


    /**
     * 
     * @param {Number} x lat
     * @param {Number} y lng
     * @param {Boolean} isTrace 마커 추적여부 
     * 
     * 입력된 좌표를 맵에 표시하고 화면을 전환합니다.
     */
    const setSvgMarker = (x,y,isTrace,svgPath,label=null)=>{
        if(mapObj === null) throw new Error("지도가 생성되어 있지 않습니다!")

        const options = {
            position: {lat:x,lng:y},
            map: mapObj,
            // label:{
            //     text:label,
            //     fontWeight:'600',
                
            // },
            icon:{
                path: svgPath,
                fillColor: 'blue',
                fillOpacity: 0.6,
                scale: 0.05,
                strokeColor: 'blue',
                strokeWeight: 0.8,                
                anchor: new google.maps.Point(200, 500),
            },
        }
        
        const marker = new window.google.maps.Marker(options)
        if(isTrace === true) {
            mapObj.setCenter({lat:x,lng:y})
            mapObj.setZoom(15)
        }

        return marker
    }

    /**
     * 
     * @param {Object} marker 
     * @param {String} eventName
     * 
     * 마커의 특정 이벤트의 리스너를 제거 
     */
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
            mapObj.setZoom(15)
        }
    }

    const setMapLatLngBounds = (minX,minY,maxX,maxY)=>{
        if(mapObj === null) throw new Error("지도가 생성되어 있지 않습니다!")

        const sw = new google.maps.LatLng(minX, minY)
        const ne = new google.maps.LatLng(maxX, maxY)
        const bounds = new google.maps.LatLngBounds(sw, ne)

        mapObj.fitBounds(bounds)
    }

    const moveMap = (x,y)=>{
        mapObj.setCenter({lat:x,lng:y})
        mapObj.setZoom(15)
    }
    /**
     * 
     * @param {Array} poligonCoordinateList [{lat: x, lng: y},...]
     */
    const setPoligonLine = ( poligonCoordinateList)=>{
        const lineSymbol = {
            path: google.maps.SymbolPath.FORWARD_CLOSED_ARROW,
            strokeOpacity: 0.8,
            scale: 1.5,
        };
        
        const flightPath = new google.maps.Polyline({
            path: poligonCoordinateList,
            // geodesic: true,
            strokeColor: "green",
            strokeOpacity: 0,
            // strokeWeight: 2,
            icons:[{
                icon: lineSymbol,
                offset: "0",
                repeat: "10px",
            }]
        })
        flightPath.setMap(mapObj)

        return flightPath
    }

    return{
        init,
        createMap,
        moveMap,
        setMapLatLngBounds,
        getMap,
        setMarker,
        setMarkerInfo,
        setMarkerEvent,
        setSvgMarker,
        setPositionMarker,
        setPoligonLine,
        moveMarker,
        searchPlace,
        createInfoWindow,
        removeListeners,
        removeMarker,
        addMapEventListener,
        removeMapEventListener
    }
}