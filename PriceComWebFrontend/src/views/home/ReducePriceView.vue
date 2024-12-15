<template>
  <div>
    <el-container>
      <el-header class="header">
        <div class="logo-container">
          <img src="../../assets/logo.png" style="width: 50px" alt="logo" />
        </div>
        <div class="center-text">
          <span class="price-compare">Price Compare</span>
        </div>
        <div class="toolbar">
          <el-dropdown>
            <el-icon style="margin-right: 10px; margin-top: 1px; color: white">
              <User />
            </el-icon>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="jumpHome">个人主页</el-dropdown-item>
                <el-dropdown-item @click="jumpHistory">历史记录</el-dropdown-item>
                <el-dropdown-item @click="confirmLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>{{ username }}</span>
        </div>
      </el-header>
      <el-main>
        <el-button type="info" plain @click="back">返回</el-button>
        <el-row type="flex" justify="center" class="remainder-goods">
          <el-col :span="24">
            <span class="remainder-title">我的收藏</span>
            <el-card v-for="product in remainderGoods" :key="product.id" class="product-card">
              <div class="product-content">
                <div class="image-container">
                  <img :src="product.img" class="product-image" alt="Product Image" />
                </div>
                <div class="product-details">
                  <h2 class="product-title">{{ product.description }}</h2>
                  <el-button
                    type="primary"
                    plain
                    @click="sendEmail(product.id)"
                    :disabled="product.isSending"
                  >
                    {{ product.isSending ? `${product.countdown}s后可再次发送` : '降价' }}
                  </el-button>
                  <el-button type="danger" plain @click="confirmCancelReminder(product.id)">
                    取消收藏
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import router from '@/router'
import { ref, onMounted } from 'vue'
import { useUserStore, useQueryStore } from '@/stores/index'
import { getRemaindersService, deleteRemainderService, sendRemindEmailService } from '@/api/query'

const userStore = useUserStore()
const queryStore = useQueryStore()
const username = ref(userStore.username)

const remainderGoods = ref([])

onMounted(async () => {
  if (userStore.token === '') {
    ElMessage.error('请先登录')
    router.push('/login')
  }
  getRemainders()
})

const getRemainders = async () => {
  const res = await getRemaindersService()
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  remainderGoods.value = res.data.payload.map((product) => ({
    ...product,
    isSending: false,
    countdown: 20
  }))
}

const confirmLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      userStore.removeToken()
      userStore.removeUsername()
      queryStore.removeQueringName()
      queryStore.removeJdProducts()
      queryStore.removeSnProducts()
      queryStore.removeWphProducts()
      queryStore.removeWebsite()
      logout()
    })
    .catch(() => {})
}

const logout = () => {
  router.push('/login')
}

const jumpHistory = () => {
  router.push('/history')
}

const back = () => {
  router.push('query')
}

const jumpHome = () => {
  router.push('/home')
}

const confirmCancelReminder = (goodId) => {
  ElMessageBox.confirm('确定要取消收藏吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(() => {
      cancelReminder(goodId)
    })
    .catch(() => {})
}

const cancelReminder = async (goodId) => {
  const res = await deleteRemainderService(goodId)
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  getRemainders()
  ElMessage.success('取消收藏成功')
}

const sendEmail = async (id) => {
  const loading = ElLoading.service({
    lock: true,
    text: '发送中',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  const res = await sendRemindEmailService(id)
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    loading.close()
    return
  }
  ElMessage.success('降价提醒发送成功')
  loading.close()

  const product = remainderGoods.value.find((p) => p.id === id)
  if (!product) return

  product.isSending = true
  product.countdown = 20

  const countdownInterval = setInterval(() => {
    product.countdown -= 1
    if (product.countdown <= 0) {
      clearInterval(countdownInterval)
      product.isSending = false
    }
  }, 1000)
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: rgb(37, 33, 33);
  color: white;
  font-size: 12px;
  padding: 0 20px;
}

.logo-container {
  display: flex;
  align-items: center;
}

.center-text {
  flex-grow: 1;
  text-align: center;
}

.price-compare {
  font-size: 24px;
  font-weight: bold;
  background: linear-gradient(45deg, #f06, #f90, #ff0, #0f0, #0ff, #00f, #90f, #f06);
  background-size: 400% 400%;
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  animation: gradient 15s ease infinite;
  font-family: 'Pacifico', cursive;
}

@keyframes gradient {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

.toolbar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  right: 20px;
}

.remainder-goods {
  margin-top: 20px;
  text-align: center;
}

.remainder-title {
  display: block;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #409eff;
}

.product-card {
  max-width: 800px;
  margin: 20px auto;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.product-content {
  display: flex;
  flex-direction: column;
}

.image-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.product-image {
  width: 350px;
  border-radius: 10px 10px 0 0;
}

.product-details {
  padding: 20px;
}

.product-title {
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 10px;
}

.search-content {
  width: 100%;
  margin-top: 20px;
}

/* 手机端适配 */
@media (max-width: 768px) {
  .header {
    padding: 0 10px;
  }

  .price-compare {
    font-size: 20px;
  }

  .toolbar {
    right: 10px;
  }

  .remainder-title {
    font-size: 20px;
  }

  .product-card {
    width: 100%;
    margin: 20px 0;
  }

  .product-title {
    font-size: 16px;
  }

  .el-button {
    font-size: 14px;
  }

  .product-image {
    width: 100%;
    border-radius: 10px 10px 0 0;
  }

  .product-details {
    padding: 10px;
  }

  .product-title {
    font-size: 16px;
  }

  .el-button {
    font-size: 12px;
  }
}
</style>
