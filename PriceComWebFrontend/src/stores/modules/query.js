import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useQueryStore = defineStore(
  'query',
  () => {
    const queryingName = ref('')
    const toRefresh = ref(false)
    const jdProducts = ref([])
    const snProducts = ref([])
    const wphProducts = ref([])
    const website = ref('')

    const setQueryingName = (newName) => {
      queryingName.value = newName
    }
    const setRefresh = () => {
      toRefresh.value = true
    }
    const resetRefresh = () => {
      toRefresh.value = false
    }
    const setJdProducts = (products) => {
      jdProducts.value = products
    }
    const setSnProducts = (products) => {
      snProducts.value = products
    }
    const setWphProducts = (products) => {
      wphProducts.value = products
    }
    const setWebsite = (Newwebsite) => {
      website.value = Newwebsite
    }

    const removeQueringName = () => {
      queryingName.value = ''
    }
    const removeJdProducts = () => {
      jdProducts.value = []
    }
    const removeSnProducts = () => {
      snProducts.value = []
    }
    const removeWphProducts = () => {
      wphProducts.value = []
    }
    const removeWebsite = () => {
      website.value = ''
    }

    const isQueryingNameEmpty = () => {
      return queryingName.value.trim() === ''
    }

    return {
      queryingName,
      setQueryingName,
      removeQueringName,
      isQueryingNameEmpty,
      toRefresh,
      setRefresh,
      resetRefresh,
      jdProducts,
      snProducts,
      wphProducts,
      setJdProducts,
      setWphProducts,
      setSnProducts,
      removeJdProducts,
      removeSnProducts,
      removeWphProducts,
      website,
      setWebsite,
      removeWebsite
    }
  },
  {
    persist: true
  }
)
