<template>
  <div v-if="showMessage" class="custom-message" :style="{ top: messageTop + 'px' }">
    {{ message }}
  </div>
  <div class="background">
    <span class="back-link" @click="back">&lt; <span>返回</span></span>
    <div class="find-password">
      <span class="title">找回密码</span>
      <p></p>
      <el-form
        ref="findbackForm"
        :model="findback"
        label-width="auto"
        style="max-width: 600px; margin: 0 auto"
      >
        <el-form-item
          label="邮箱绑定"
          prop="email"
          :rules="[
            { required: true, message: '邮箱不能为空', trigger: 'blur' },
            { type: 'email', message: '邮箱格式不正确', trigger: ['blur', 'change'] }
          ]"
          inline
        >
          <el-input
            v-model="findback.email"
            placeholder="请输入您绑定的邮箱"
            style="width: 60%; display: inline-block"
          ></el-input>
          <el-button
            type="primary"
            @click="getVerificationCode"
            style="
              margin-left: 10px;
              width: 30%;
              display: flex;
              justify-content: center;
              align-items: center;
            "
            :disabled="countdownActive"
          >
            {{ countdownActive ? `${verificationCountdown}秒后重试` : '获取验证码' }}
          </el-button>
        </el-form-item>
        <el-form-item
          label="验证码"
          prop="justifyCode"
          :rules="[
            { required: true, message: '验证码不能为空', trigger: 'blur' },
            { min: 6, max: 6, message: '验证码格式错误', trigger: 'blur' }
          ]"
        >
          <el-input
            v-model="findback.justifyCode"
            placeholder="请输入收到的验证码"
            style="width: 100%"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="密码"
          prop="password"
          :rules="[
            { required: true, message: '密码不能为空', trigger: 'blur' },
            { min: 6, message: '密码至少6个字符', trigger: 'blur' }
          ]"
        >
          <el-input
            v-model="findback.password"
            placeholder="请输入密码"
            show-password
            style="width: 100%"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="repassword"
          :rules="[
            { required: true, message: '请确认密码', trigger: 'blur' },
            { validator: validateRepassword, trigger: 'blur' }
          ]"
        >
          <el-input
            v-model="findback.repassword"
            placeholder="请再次输入密码"
            show-password
            style="width: 100%"
          ></el-input>
        </el-form-item>
        <el-button type="primary" round plain @click="submit">确认</el-button>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import router from '@/router'
import { ref } from 'vue'
import { getEmailCodeInForgetPasswordService, modifyPasswordService } from '@/api/user'
import { useUserStore } from '@/stores/index'

const userStore = useUserStore()

const back = () => {
  router.push('/login')
}

const findback = ref({
  email: '',
  justifyCode: '',
  password: '',
  repassword: ''
})

const validateRepassword = (rule, value, callback) => {
  if (value === '' || value === findback.value.password) {
    callback()
  } else {
    callback(new Error('确认密码与密码不一致'))
  }
}

const verificationCountdown = ref(30)
const countdownActive = ref(false)
const showMessage = ref(false)
const message = ref('')
const messageTop = ref(20) // 自定义顶部位置

const findbackForm = ref(null)

const getVerificationCode = async () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(findback.value.email)) {
    ElMessage.error('邮箱格式不正确')
    return
  }

  if (countdownActive.value) return // 如果已经在倒计时，直接返回

  const loading = ElLoading.service({
    lock: true,
    text: '发送中',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  const res = await getEmailCodeInForgetPasswordService(findback.value.email)
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    loading.close()
    return
  }
  userStore.setEmailCode(res.data.payload)
  loading.close()
  ElMessage.success('验证码发送成功')

  countdownActive.value = true // 开始倒计时
  const countdownInterval = setInterval(() => {
    verificationCountdown.value--
    if (verificationCountdown.value <= 0) {
      clearInterval(countdownInterval)
      countdownActive.value = false // 重置状态
      verificationCountdown.value = 30 // 重置倒计时
    }
  }, 1000)
}

const submit = async () => {
  findbackForm.value.validate((valid) => {
    if (!valid) {
      ElMessage.error('部分内容输入格式有误')
      return
    }

    // 如果验证通过，继续执行后续操作
    modifyPassword()
  })
}

const modifyPassword = async () => {
  const res = await modifyPasswordService(
    findback.value.email,
    findback.value.password,
    findback.value.justifyCode,
    userStore.emailCode
  )
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  findback.value.email = ''
  findback.value.justifyCode = ''
  findback.value.password = ''
  findback.value.repassword = ''
  userStore.removeEmailCode()
  ElMessage.success('密码修改成功')
  router.push('/login')
}
</script>

<style scoped>
.custom-message {
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  background-color: #f0f9eb; /* 背景色 */
  border: 1px solid #b7eb8f; /* 边框颜色 */
  color: #4caf50; /* 字体颜色 */
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 9999; /* 确保在其他内容上面 */
  transition: opacity 0.5s ease; /* 动画效果 */
}

.background {
  background-color: #ecebed; /* 设置背景色 */
  height: 100vh; /* 确保覆盖整个视口 */
  display: flex;
  flex-direction: column;
}

.back-link {
  margin-left: 10px;
  margin-top: 10px;
  cursor: pointer; /* 鼠标指针效果 */
  color: #007bff; /* 文字颜色 */
  font-size: 1rem; /* 字体大小 */
  text-decoration: none; /* 去掉下划线 */
  transition: color 0.3s; /* 过渡效果 */
}

.back-link:hover {
  color: #0056b3; /* 悬停时颜色 */
}

.find-password {
  /* background-color: pink; */
  margin: 0 auto;
  text-align: center;
}

.el-form-item {
  width: 100%; /* 确保每个表单项占满宽度 */
}

.title {
  font-size: larger;
  font-family: 'KaiTi', '楷体', serif;
  font-weight: bold;
}
</style>
