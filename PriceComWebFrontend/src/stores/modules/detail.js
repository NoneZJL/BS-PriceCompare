import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useDetailStore = defineStore(
  'detail',
  () => {
    const detailGoodName = ref('')
    const detailGoodFrom = ref('')
    const description = ref('')
    const image = ref('')
    const price = ref('')
    const setDetailGoodName = (newName) => {
      detailGoodName.value = newName
    }
    const setDetailGoodFrom = (newUrl) => {
      detailGoodFrom.value = newUrl
    }
    const setDescription = (newDescription) => {
      description.value = newDescription
    }
    const setImage = (newImage) => {
      image.value = newImage
    }
    const setPrice = (newPrice) => {
      price.value = newPrice
    }
    const removeDetailGoodName = () => {
      detailGoodName.value = ''
    }
    const removeDetailGoodFrom = () => {
      detailGoodFrom.value = ''
    }
    const removeDescription = () => {
      description.value = ''
    }
    const removeImage = () => {
      image.value = ''
    }
    const removePrice = () => {
      price.value = ''
    }
    const isDeatilGoodNameEmpty = () => {
      return detailGoodName.value.trim() === ''
    }

    return {
      detailGoodName,
      setDetailGoodName,
      removeDetailGoodName,
      isDeatilGoodNameEmpty,
      detailGoodFrom,
      setDetailGoodFrom,
      removeDetailGoodFrom,
      description,
      setDescription,
      removeDescription,
      image,
      setImage,
      removeImage,
      price,
      setPrice,
      removePrice
    }
  },
  {
    persist: true
  }
)
