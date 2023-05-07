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

  return { planCodes }
})
