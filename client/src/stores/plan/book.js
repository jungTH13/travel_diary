import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";
import * as API from "../../composable/api";

export const useBookStore = defineStore("book", () => {
  const book = ref({});
  const uploadImageList = ref([]) // [{url:base64Url,file:File}]
  const delImageList = ref([]) // [{url:string,id:number}]

  const nav = ref([
    { type:'', name: "전체" },
    { type:'pa', name: "항공권" },
    { type:'ph', name: "호텔" },
    { type:'pr', name: "음식점" },
    { type:'pt', name: "교통" },
    { type:'pe', name: "기타" },
  ]);

  const navImage = {type:'pig',name:'이미지그룹'}

  const postBookImages = async(travelId,planId,type)=>{
    console.log(travelId,planId,type)
    const formData = new FormData()

    let idArr = ""
    if(delImageList.value) idArr += delImageList.value.map((imageUrl)=>imageUrl.id).join(',')

    uploadImageList.value.forEach((obj)=>{
      formData.append("multipartFileList",obj.file)
    })
    formData.append("planType",type)
    formData.append("planTypeId",planId)
    formData.append("idArr",idArr)

    console.log(formData)

    const {data} = await API.postFormData(`/upload/${travelId}`, formData);

    return data
  }

  const getBook = (travelId,planId,type) => {
    if(type === 'pa') return _getBookAirPlan(travelId,planId)
    if(type === 'ph') return _getBookHotel(travelId,planId)
    if(type === 'pr') return _getBookRestaurant(travelId,planId)
    if(type === 'pt') return _getBookTransPort(travelId,planId)
    if(type === 'pe') return _getBookEtc(travelId,planId)
    if(type === 'pig') return _getImageGroup(travelId,planId)
  }

  const putBook = async (travelId,type)=>{
    const form = {}

    Object.keys(book.value).forEach((key)=>form[key] = book.value[key]);
    delete form.deleted
    delete form.createdDate
    delete form.modifiedDate

    let response = null
    if(type === 'pa') response = await API.put(`/travel/${travelId}/plan/airPlane/airPlaneUpdate`, form)
    if(type === 'ph') response = await API.put(`/travel/${travelId}/plan/hotel/hotelUpdate`, form)
    if(type === 'pr') response = await API.put(`/travel/${travelId}/plan/restaurant/restaurantUpdate`, form)
    if(type === 'pt') response = await API.put(`/travel/${travelId}/plan/transPort/transPortUpdate`, form)
    if(type === 'pe') response = await API.put(`/travel/${travelId}/plan/etc/etcUpdate`, form)
    if(type === 'pig') response = await API.put(`/travel/${travelId}/plan/imageGroup/imageGroupUpdate`, form)

    return response.data
  }

  const postBook = async (travelId,type)=>{
    const form = {}

    Object.keys(book.value).forEach((key)=>form[key] = book.value[key]);

    let response = null
    if(type === 'pa') response = await API.post(`/travel/${travelId}/plan/airPlane/airPlaneInsert`, form)
    if(type === 'ph') response = await API.post(`/travel/${travelId}/plan/hotel/hotelInsert`, form)
    if(type === 'pr') response = await API.post(`/travel/${travelId}/plan/restaurant/restaurantInsert`, form)
    if(type === 'pt') response = await API.post(`/travel/${travelId}/plan/transPort/transPortInsert`, form)
    if(type === 'pe') response = await API.post(`/travel/${travelId}/plan/etc/etcInsert`, form)
    if(type === 'pig') response = await API.post(`/travel/${travelId}/plan/imageGroup/imageGroupInsert`, form)

    return response.data
  }

  const delBook = async (travelId,type)=>{

    let response = null
    if(type === 'pa') response = await API.delete(`/travel/${travelId}/plan/airPlane/airPlaneDelete/${book.value.id}`)
    if(type === 'ph') response = await API.delete(`/travel/${travelId}/plan/hotel/hotelDelete/${book.value.id}`)
    if(type === 'pr') response = await API.delete(`/travel/${travelId}/plan/restaurant/restaurantDelete/${book.value.id}`)
    if(type === 'pt') response = await API.delete(`/travel/${travelId}/plan/transPort/transPortDelete/${book.value.id}`)
    if(type === 'pe') response = await API.delete(`/travel/${travelId}/plan/etc/etcDelete/${book.value.id}`)
    if(type === 'pig') response = await API.delete(`/travel/${travelId}/plan/imageGroup/imageGroupDelete/${book.value.id}`)

    return response.data
  }

  const resetBook = ()=>{
    book.value= {}
    uploadImageList.value = []
    delImageList.value = []
  }

  const _getBookAirPlan = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/airPlane/airPlaneOne`, {id:planId});
    
    convertTime(data.results.planAirPlaneOne)
    book.value = data.results.planAirPlaneOne
    book.value.thumbNailList = data.results.thumbNailList || []
    return data
  };

  const _getBookHotel = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/hotel/hotelOne`, {id:planId});

    convertTime(data.results.planHotelOne)
    book.value = data.results.planHotelOne
    book.value.thumbNailList = data.results.thumbNailList ||[]
    return data
  };

  const _getBookRestaurant = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/restaurant/restaurantOne`, {id:planId});

    convertTime(data.results.planRestaurantOne)
    book.value = data.results.planRestaurantOne
    book.value.thumbNailList = data.results.thumbNailList || []
    return data
  }

  const _getBookTransPort = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/transPort/transPortOne`, {id:planId});

    convertTime(data.results.planTransPortOne)
    book.value = data.results.planTransPortOne
    book.value.thumbNailList = data.results.thumbNailList || []
    return data
  };

  const _getBookEtc = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/etc/etcOne`, {id:planId});
    
    convertTime(data.results.planEtcOne)
    book.value = data.results.planEtcOne
    book.value.thumbNailList = data.results.thumbNailList || []
    return data
  };

  const _getImageGroup = async (travelId,planId) => {
    const {data} = await API.post(`/travel/${travelId}/plan/imageGroup/imageGroupOne`, {id:planId});
    
    convertTime(data.results.planImageGroupOne)
    book.value = data.results.planImageGroupOne
    book.value.thumbNailList = data.results.thumbNailList || []
    return data
  };

  const convertTime = (obj)=>{
    //시간 정보 전처리
    for(const key of Object.keys(obj)){
        if(key.includes("Date") || key.includes("Time") ||key.includes("date")){
          if(!obj[key]) continue  
          obj[key] = obj[key].split('.')[0]
        }
    }
  }



  // const getPlanCountries = computed(() => planCountries.value);

  return { 
    book,
    uploadImageList,
    delImageList,
    nav,
    navImage,
    getBook,
    resetBook,
    putBook,
    postBook,
    delBook,
    postBookImages
  };
});
