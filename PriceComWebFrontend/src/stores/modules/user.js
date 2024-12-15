import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore(
  'user',
  () => {
    const token = ref('')
    const emailCode = ref('')
    const username = ref('')
    const setToken = (newToken) => {
      token.value = newToken
    }
    const removeToken = () => {
      token.value = ''
    }
    const setEmailCode = (newEmailCode) => {
      emailCode.value = newEmailCode
    }
    const removeEmailCode = () => {
      emailCode.value = ''
    }
    const setUsername = (newUsername) => {
      username.value = newUsername
    }
    const removeUsername = () => {
      username.value = ''
    }

    return {
      token,
      setToken,
      removeToken,
      emailCode,
      setEmailCode,
      removeEmailCode,
      username,
      setUsername,
      removeUsername
    }
  },
  {
    persist: true
  }
)
