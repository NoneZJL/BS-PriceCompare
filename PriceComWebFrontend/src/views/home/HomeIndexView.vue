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
                <el-dropdown-item @click="jumpHistory">历史记录</el-dropdown-item>
                <el-dropdown-item @click="jumpRemainder">我的收藏</el-dropdown-item>
                <el-dropdown-item @click="confirmLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <span>{{ userInfo.username }}</span>
        </div>
      </el-header>
      <el-main>
        <el-button type="info" plain @click="back">返回</el-button>
        <el-card class="user-info-card">
          <div class="user-info-header">
            <el-icon class="user-icon"><User /></el-icon>
            <div class="user-info-details">
              <div class="user-name">{{ userInfo.username }}</div>
              <div class="user-email">{{ userInfo.email }}</div>
            </div>
          </div>
          <div class="user-info-body">
            <el-descriptions title="个人信息" :column="1">
              <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ userInfo.email }}</el-descriptions-item>
              <el-descriptions-item label="手机号">
                <span v-if="userInfo.phone">{{ userInfo.phone }}</span>
                <span v-else>暂未补充</span>
              </el-descriptions-item>
              <el-descriptions-item label="地址">
                <span v-if="userInfo.address">{{ userInfo.address }}</span>
                <span v-else>暂未补充</span>
              </el-descriptions-item>
            </el-descriptions>
          </div>
          <div class="edit-button-container">
            <el-button type="primary" @click="showEditForm">修改信息</el-button>
          </div>
        </el-card>
      </el-main>
    </el-container>

    <!-- 修改信息的弹出表单 -->
    <el-dialog title="修改信息" v-model="editFormVisible" width="40%" :fullscreen="isMobile">
      <el-form :model="editForm" :rules="editFormRules" ref="editFormRef">
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="editForm.address" placeholder="请输入地址"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEditForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import router from '@/router'
import { ref, onMounted, computed } from 'vue'
import { useUserStore, useQueryStore } from '@/stores/index'
import { getUserInfoService, updateUserInfoService } from '@/api/user'

const userStore = useUserStore()
const queryStore = useQueryStore()

const userInfo = ref({
  username: 'John Doe',
  email: 'john.doe@example.com',
  phone: '123-456-7890',
  address: '123 Main St, City, Country'
})

const editFormVisible = ref(false)
const editForm = ref({
  phone: '',
  address: ''
})

const editFormRef = ref(null)

const editFormRules = ref({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3456789]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
  ],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
})

onMounted(() => {
  if (userStore.token === '') {
    ElMessage.error('请先登录')
    router.push('/login')
  }
  userInfo.value.username = userStore.username
  getUserInfo()
})

const getUserInfo = async () => {
  const res = await getUserInfoService()
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  userInfo.value.username = res.data.payload.username
  userInfo.value.address = res.data.payload.address
  userInfo.value.email = res.data.payload.email
  userInfo.value.phone = res.data.payload.phone
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

const jumpRemainder = () => {
  router.push('/remainder')
}

const showEditForm = () => {
  editForm.value.phone = userInfo.value.phone
  editForm.value.address = userInfo.value.address
  editFormVisible.value = true
}

const submitEditForm = async () => {
  editFormRef.value.validate((valid) => {
    if (!valid) {
      ElMessage.error('请检查输入信息')
      return
    }
  })
  const res = await updateUserInfoService(editForm.value.phone, editForm.value.address)
  if (res.data.code === 2) {
    router.push('/login')
    return
  }
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  getUserInfo()
  editFormVisible.value = false
  ElMessage.success('信息修改成功')
}

// 判断是否为手机端
const isMobile = computed(() => {
  return window.innerWidth <= 768
})
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

.user-info-card {
  margin-top: 20px;
  width: 50%;
  margin-left: auto;
  margin-right: auto;
}

.user-info-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-icon {
  font-size: 60px;
  margin-right: 20px;
}

.user-info-details {
  margin-left: 20px;
}

.user-name {
  font-size: 24px;
  font-weight: bold;
}

.user-email {
  font-size: 16px;
  color: #888;
}

.edit-button-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

/* 手机端适配 */
@media (max-width: 768px) {
  .user-info-card {
    width: 90%;
  }

  .user-info-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .user-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }

  .user-info-details {
    margin-left: 0;
  }

  .user-name {
    font-size: 20px;
  }

  .user-email {
    font-size: 14px;
  }

  .edit-button-container {
    justify-content: center;
  }
}
</style>
