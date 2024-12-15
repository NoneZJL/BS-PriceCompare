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
                <el-dropdown-item @click="jumpRemainder">我的收藏</el-dropdown-item>
                <el-dropdown-item @click="confirmLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>{{ username }}</span>
        </div>
      </el-header>
      <el-main>
        <div class="button-container">
          <el-button type="info" plain @click="back">返回</el-button>
          <el-button
            v-if="history.length > 0"
            type="danger"
            plain
            @click="confirmDeleteAll"
            class="delete-all-button"
          >
            删除所有历史记录
          </el-button>
        </div>
        <div class="history-container">
          <el-card class="history-card">
            <div class="history-title">历史记录</div>
            <el-scrollbar height="400px">
              <ul class="history-list">
                <li v-if="history.length === 0" class="no-history">暂无历史查询数据</li>
                <li
                  v-for="(item, index) in history"
                  :key="index"
                  @click="handleHistoryClick(item)"
                  class="history-item"
                >
                  {{ item }}
                  <span @click.stop="deleteHistoryItem(item)" class="delete-text"> 删除 </span>
                </li>
              </ul>
            </el-scrollbar>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import router from '@/router'
import { ref, onMounted } from 'vue'
import { useUserStore, useQueryStore } from '@/stores/index'
import {
  getAllHistoryService,
  deleteSingleHistoryByNameService,
  deleteAllHistoryService
} from '@/api/query'

const username = ref('Tom')
const history = ref([])
const userStore = useUserStore()
const queryStore = useQueryStore()

const getHistory = async () => {
  const res = await getAllHistoryService()
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  const queryHistory = res.data.payload
  history.value = queryHistory.map((item) => item.content)
}

onMounted(async () => {
  if (userStore.token === '') {
    ElMessage.error('请先登录')
    router.push('/login')
  }
  username.value = userStore.username
  getHistory()
})

const jumpHome = () => {
  router.push('/home')
}

const logout = () => {
  router.push('/login')
}

const back = () => {
  router.push('query')
}

const jumpRemainder = () => {
  router.push('/remainder')
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

const handleHistoryClick = (item) => {
  console.log('点击了历史记录:', item)
  queryStore.setQueryingName(item)
  queryStore.setRefresh()
  router.push('/query')
}

const deleteHistoryItem = async (name) => {
  console.log(name)
  const res = await deleteSingleHistoryByNameService(name)
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  getHistory()
  ElMessage.success('删除成功')
}

const confirmDeleteAll = () => {
  ElMessageBox.confirm('确定要删除所有历史记录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
    .then(async () => {
      const res = await deleteAllHistoryService()
      if (res.data.code === 2) {
        router.push('/login')
        return
      }
      if (res.data.code === 1) {
        ElMessage.error(res.data.err)
        return
      }
      getHistory()
      ElMessage.success('删除成功')
    })
    .catch(() => {})
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

.button-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.history-container {
  margin-top: 20px;
}

.history-card {
  width: 100%;
  margin: 0 auto;
}

.history-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
  text-align: center;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
}

.history-list {
  list-style-type: none;
  padding: 0;
}

.history-item {
  padding: 10px;
  border-bottom: 1px solid #e0e0e0;
  cursor: pointer;
  transition: background-color 0.3s;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.history-item:hover {
  background-color: #f5f5f5;
}

.no-history {
  text-align: center;
  color: #999;
  padding: 20px;
}

.delete-text {
  color: #f56c6c;
  cursor: pointer;
  transition: color 0.3s;
}

.delete-text:hover {
  color: #ff0000;
}

.delete-all-button {
  color: #f56c6c;
  margin-right: 10px;
  border-color: #f56c6c;
}

.delete-all-button:hover {
  background-color: #f56c6c;
  color: white;
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

  .button-container {
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  .button-container .el-button {
    margin-bottom: 0;
  }

  .history-card {
    width: 90%;
  }

  .history-title {
    font-size: 20px;
  }

  .history-item {
    padding: 8px;
  }

  .delete-all-button {
    margin-right: 0;
  }
}
</style>
