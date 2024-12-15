import axios from 'axios'
import { useUserStore } from '@/stores'

const baseURL = 'http://localhost:8082'

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 60000
})

instance.interceptors.request.use(
  (config) => {
    // TODO 2. 携带token
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = userStore.token
    }
    return config
  },
  (err) => Promise.reject(err)
)

export default instance
export { baseURL }
