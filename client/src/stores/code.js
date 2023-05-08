import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCodeStore = defineStore('code', () => {
  const planCodes = ref({
     'pa': "항공권",
     'ph': "호텔",
     'pr': "음식점",
     'pt': "교통",
     'pe': "기타",
     'pig': "이미지 그룹"
  })

  const dateNamesOfPlan = ref({
    "pa":['departDate','arriveDate'],
    "ph":['checkinDate','checkoutDate'],
    "pr":['date'],
    "pt":['arriveDate','departDate'],
    "pe":['reservationDate'],
    "pig":['date']
  })

  return { planCodes , dateNamesOfPlan}
})
