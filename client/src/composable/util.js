export const getDateRange = (startDate, endDate) => {
  const start = new Date(startDate);
  const end = new Date(endDate);

  const result = [];

  while (start <= end) {
    result.push(start.toISOString().split("T")[0]);
    start.setDate(start.getDate() + 1);
  }

  return result;
};

/**
 * 
 * @param {Date} date Date 타입 시간을 한국 시간 형식으로 변환
 * @returns YYYY-MM-DDThh:mm:ss.xxxZ
 */
export const toKoreaTimeString = (date) =>{
  date.setHours(date.getHours() + 9);
  return date.toJSON().split('.')[0];
}

/**
 * 
 * @param {Date} date 
 * @returns 01.15 (일)
 */
export const DateToStringFormat1 = (date)=>{
  const DayList = ['일','월','화','수','목','금','토']

  const MONTH = date.getMonth()+1<10? `0${date.getMonth()+1}`:`${date.getMonth()+1}`
  const DATE = date.getDate()<10? `0${date.getDate()}`:`${date.getDate()}`

  return `${MONTH}.${DATE} (${DayList[date.getDay()]})`
}

/**
 * 
 * @param {String} date YYYY-MM-DDThh:mm:ss
 */
export const toAMPMString = (date)=>{
  if(!date || date === '')return ''
  try{
    let str = ''
    const time = date.split('T')[1]
    let hour = parseInt(time.split(':')[0])
    const min = parseInt(time.split(':')[1])

    if(hour>=12) {
      if(hour>12) hour-=12
      return `PM ${hour<10?`0${hour}`:hour} : ${min<10?`0${min}`:min}`
    }
    else return `AM ${hour<10?`0${hour}`:hour} : ${min<10?`0${min}`:min}`
  }
  catch(error){
    console.log(date)
    console.log(error)
    return ''
  }
}

/**
 * 
 * @param {Number|String} number 
 * @returns '1,576,302.05'
 */
export const toComaNumberString =(number)=>{
  const parts = (`${number}`).split('.')
  const regex = /\B(?=(\d{3})+(?!\d))/g
  parts[0] = parts[0].replace( regex,",")

  let result = parts[0]
  if(parts.length===2) result+= (parts[1]?'.'+parts[1].slice(0,2):'.')
  
  return result
}

/**
 * 
 * @param {*} obj Date,Time,date 이름을 가지는 String변수를 포함
 * 
 * YYYY-MM-DDThh:mm:ss.xxxZ -> YYYY-MM-DDThh:mm:ss 변환
 */
export const convertTimeFormat = (obj)=>{
  //시간 정보 전처리
  for(const key of Object.keys(obj)){
      if(key.includes("Date") || key.includes("Time") ||key.includes("date")){
        if(!obj[key]) continue  
        obj[key] = obj[key].split('.')[0]
      }
  }
}