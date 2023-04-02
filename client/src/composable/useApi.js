export function useApi(){
    //공용으로 사용될 함수 작성

    const getCountryList = async ()=>{
        //pinia로 들어가기 애매한 단순 기능 작성
        const { data } = await axios.post(
            "https://develop.life-traveldiary.net:8080/common/countryLike",
            searchData,
            {
                withCredentials: true,
                headers: {
                "Content-Type": "application/json",
                },
            }
        );

        return data.results
    }

    return {
        getCountryList
    }
}