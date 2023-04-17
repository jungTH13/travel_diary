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
  return date.toJSON();
}