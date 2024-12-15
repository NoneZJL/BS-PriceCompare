<template>
  <div :class="['login-page', { 'move-up': moveUP }]">
    <div style="display: block">
      <img style="width: 30vh" src="../../assets/logo.png" alt="LOGO" />
    </div>
    <div class="login-part">
      <h2 class="header">欢迎来到 Price Compare !</h2>
    </div>
    <Transition name="fade">
      <div v-if="homeVisible" class="button">
        <el-button type="primary" plain style="font-size: large" @click="clickLogin"
          >登录</el-button
        >
        <el-button
          type="primary"
          plain
          style="margin-left: 5vw; font-size: large"
          @click="clickRegister"
          >注册</el-button
        >
      </div>
    </Transition>
    <Transition name="fade" mode="out-in">
      <div v-if="loginVisible" class="login">
        <h1>{{ loginByUsername ? '用户名登录' : '邮箱登录' }}</h1>
        <el-form :model="login" label-width="auto" style="max-width: 600px">
          <el-form-item :label="loginByUsername ? '用户名' : '邮箱'">
            <el-input
              v-model="loginInputValue"
              :placeholder="loginByUsername ? '请输入用户名' : '请输入邮箱'"
              style="width: 225px"
            ></el-input>
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="loginPasswordType" placeholder="请输入密码" show-password></el-input>
          </el-form-item>
          <el-button type="primary" round plain @click="clickReturn">返回</el-button>
          <el-button type="primary" round plain @click="loginFunction">登录</el-button>
          <br /><br />
          <span class="forgot-password" @click="loginByUsername = !loginByUsername">{{
            loginByUsername ? '切换为邮箱登录' : '切换为用户名登录'
          }}</span>
          <br /><br />
          <span class="forgot-password" @click="handleForgotPassword">忘记密码？</span>
        </el-form>
      </div>
    </Transition>
    <Transition name="fade">
      <div v-if="registerVisible" class="register">
        <h1>注册</h1>
        <el-form
          ref="registerForm"
          :model="register"
          label-width="auto"
          style="max-width: 600px"
          :rules="registerRules"
        >
          <el-form-item label="用户名" prop="name">
            <el-input v-model="register.name" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="register.password" placeholder="请输入密码" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认密码" prop="repassword">
            <el-input
              v-model="register.repassword"
              placeholder="请再次输入密码"
              show-password
            ></el-input>
          </el-form-item>
          <el-form-item label="邮箱绑定" prop="email" inline>
            <el-input
              v-model="register.email"
              placeholder="请输入您绑定的邮箱"
              style="width: 60%"
            ></el-input>
            <el-button
              type="primary"
              @click="getVerificationCode"
              style="margin-left: 10px"
              :disabled="countdownActive"
            >
              {{ countdownActive ? `${verificationCountdown}秒后重试` : '获取验证码' }}
            </el-button>
          </el-form-item>
          <el-form-item label="验证码" prop="justifyCode">
            <el-input
              v-model="register.justifyCode"
              placeholder="请输入您邮箱收到的验证码"
            ></el-input>
          </el-form-item>
          <el-button type="primary" round plain @click="clickReturn">返回</el-button>
          <el-button type="primary" round plain @click="submitRegister">注册</el-button>
        </el-form>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import {
  getEmailCodeService,
  userLoginByEmailService,
  userLoginService,
  userRegisterService
} from '@/api/user'
import router from '@/router/index'
import { ref, onMounted, computed } from 'vue'
import { useUserStore, useQueryStore } from '@/stores/index'

const userStore = useUserStore()
const queryStore = useQueryStore()

const loginByUsername = ref(true)

const loginInputValue = computed({
  get() {
    return loginByUsername.value ? login.value.name : loginByEmail.value.email
  },
  set(value) {
    if (loginByUsername.value) {
      login.value.name = value
    } else {
      loginByEmail.value.email = value
    }
  }
})

const loginPasswordType = computed({
  get() {
    return loginByUsername.value ? login.value.password : loginByEmail.value.password
  },
  set(value) {
    if (loginByUsername.value) {
      login.value.password = value
    } else {
      loginByEmail.value.password = value
    }
  }
})

const login = ref({
  name: '',
  password: ''
})

const register = ref({
  name: '',
  password: '',
  repassword: '',
  email: '',
  justifyCode: ''
})

const loginByEmail = ref({
  email: '',
  password: ''
})

const loginVisible = ref(false)
const loginByEmailVisible = ref(false)
const homeVisible = ref(true)
const moveUP = ref(false)
const registerVisible = ref(false)

onMounted(() => {
  userStore.removeEmailCode()
  // userStore.removeToken()
  // userStore.removeUsername()
  if (userStore.token) {
    router.push('/query')
  }
})

const validateRepassword = (rule, value, callback) => {
  if (value === '' || value === register.value.password) {
    callback()
  } else {
    callback(new Error('确认密码与密码不一致'))
  }
}

const clickLogin = () => {
  // 开始向上移动
  moveUP.value = true // 先隐藏 login
  setTimeout(() => {
    homeVisible.value = false // 隐藏按钮
    setTimeout(() => {
      loginVisible.value = true // 按钮完全消失后显示 login
    }, 500) // 按钮消失的时间
  }, 100) // 向上移动的时间
}

const clickRegister = () => {
  // 开始向上移动
  moveUP.value = true // 先隐藏 login
  setTimeout(() => {
    homeVisible.value = false // 隐藏按钮
    setTimeout(() => {
      registerVisible.value = true // 按钮完全消失后显示 login
    }, 500) // 按钮消失的时间
  }, 100) // 向上移动的时间
}

const clickReturn = () => {
  moveUP.value = false
  loginVisible.value = false
  registerVisible.value = false
  loginByEmailVisible.value = false
  setTimeout(() => {
    homeVisible.value = true
  }, 500) // 500毫秒后显示button
}

const verificationCountdown = ref(30)
const countdownActive = ref(false)

const getVerificationCode = async () => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(register.value.email)) {
    ElMessage.error('邮箱格式不正确')
    return
  }

  const loading = ElLoading.service({
    lock: true,
    text: '发送中',
    background: 'rgba(0, 0, 0, 0.7)'
  })
  const res = await getEmailCodeService(register.value.email)
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    loading.close()
    return
  }
  userStore.setEmailCode(res.data.payload)
  if (countdownActive.value) return // 如果已经在倒计时，直接返回
  countdownActive.value = true // 开始倒计时
  // 设置提示信息
  ElMessage.success('验证码已发送！')
  loading.close()

  const countdownInterval = setInterval(() => {
    verificationCountdown.value--
    if (verificationCountdown.value <= 0) {
      clearInterval(countdownInterval)
      countdownActive.value = false // 重置状态
      verificationCountdown.value = 30 // 重置倒计时
    }
  }, 1000)
}

const handleForgotPassword = () => {
  console.log('点击了忘记密码')
  router.push('/find')
}

const submitRegister = () => {
  registerForm.value.validate(async (valid) => {
    if (valid) {
      const res = await userRegisterService(
        register.value.name,
        register.value.password,
        register.value.email,
        register.value.justifyCode,
        userStore.emailCode
      )
      if (res.data.code === 1) {
        ElMessage.error(res.data.err)
        return
      }
      userStore.removeEmailCode()
      register.value.name = ''
      register.value.password = ''
      register.value.repassword = ''
      register.value.email = ''
      register.value.justifyCode = ''
      ElMessage.success('注册成功')
      setTimeout(() => {
        registerVisible.value = false
        loginVisible.value = true
      }, 1000)
    } else {
      console.log('表单验证失败')
      return false
    }
  })
}

const userLogin = async () => {
  const res = await userLoginService(login.value.name, login.value.password)
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    login.value.password = ''
    return
  }
  ElMessage.success('登录成功')
  userStore.setToken(res.data.payload)
  userStore.setUsername(login.value.name)
  queryStore.removeJdProducts()
  queryStore.removeSnProducts()
  queryStore.removeWphProducts()
  queryStore.removeQueringName()

  router.push('/query')
}

const userLoginByEmail = async () => {
  const res = await userLoginByEmailService(loginByEmail.value.email, loginByEmail.value.password)
  if (res.data.code === 1) {
    ElMessage.error(res.data.err)
    return
  }
  console.log(res)
  userStore.setToken(res.data.payload.token)
  userStore.setUsername(res.data.payload.username)
  ElMessage.success('登录成功')
  router.push('/query')
}

const loginFunction = () => {
  if (loginByUsername.value) {
    userLogin()
  } else {
    userLoginByEmail()
  }
}

const registerForm = ref(null)

const registerRules = {
  name: [
    { required: true, message: '用户名不能为空', trigger: 'blur' },
    { min: 6, message: '用户名至少6个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '密码不能为空', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  repassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateRepassword, trigger: 'blur' }
  ],
  email: [
    { required: true, message: '邮箱不能为空', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: ['blur', 'change'] }
  ],
  justifyCode: [
    { required: true, message: '验证码不能为空', trigger: 'blur' },
    { min: 6, max: 6, message: '验证码格式错误', trigger: 'blur' }
  ]
}
</script>

<style scoped>
.success-message {
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  background-color: #f0f9eb;
  border: 1px solid #b7eb8f;
  color: #4caf50;
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 9999;
  transition: opacity 0.5s ease;
}

.fail-message {
  position: fixed;
  left: 50%;
  transform: translateX(-50%);
  background-color: #fef0f0;
  border: 1px solid #fde2e2;
  color: #f56c6c;
  padding: 10px 20px;
  border-radius: 5px;
  z-index: 9999;
  transition: opacity 0.5s ease;
}

.login-page {
  /* background-color: #e2e0e3; */
  background: linear-gradient(to right, #ffffff 0%, #e2e0e3 50%, #ffffff 100%);
  height: 100vh;
  display: flex;
  flex-direction: column;
  padding-top: 17vh;
  align-items: center;
  transition: transform 1s;
}

.move-up {
  transform: translateY(-17vh); /* 将页面移动到顶部 */
}

.login-part {
  display: block;
  /* background-color: pink; */
}

.header {
  margin-top: 1vh;
  margin-bottom: 0;
  font-size: 5vw;
  /* font-family: 'Trebuchet MS', 'Lucida Sans Unicode', 'Lucida Grande', 'Lucida Sans', Arial,
    sans-serif; */
  font-family: 'Pacifico', cursive;
  font-weight: 700;
  background: linear-gradient(
    270deg,
    #f3a048,
    #ff4081,
    #ffd700,
    #1e90ff,
    #f3a048,
    #ff4081,
    #ffd700
  ); /* 渐变色 */
  background-size: 300%; /* 增加背景尺寸以便于动画 */
  -webkit-background-clip: text; /* 将背景应用到文本 */
  background-clip: text;
  -webkit-text-fill-color: transparent; /* 使文本颜色透明 */
  animation: gradient-animation 8s linear infinite; /* 修改为线性动画 */
}

@keyframes gradient-animation {
  0% {
    background-position: 0% 50%;
  }
  100% {
    background-position: 100% 50%;
  }
}

.button {
  margin-top: 6vh;
}

.login,
.register {
  margin-top: 1vh;
  display: flex;
  flex-direction: column;
  /* justify-content: center; */
  align-items: center;
  text-align: center;
}

.login h1,
.register h1 {
  font-size: 1.5rem; /* 增加字体大小 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2); /* 添加阴影效果 */
  margin-bottom: 3vh; /* 添加下边距 */
  font-family: 'KaiTi', '楷体', serif; /* 设置楷体 */
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.5s;
}
.fade-enter, .fade-leave-to /* .fade-leave-active 在 <2.1.8 版本中使用 */ {
  opacity: 0;
}

.forgot-password {
  margin-top: 10px; /* 上边距 */
  font-size: 0.9rem; /* 字体大小 */
  color: #007bff; /* 字体颜色 */
  cursor: pointer; /* 鼠标指针效果 */
  transition: color 0.3s; /* 过渡效果 */
}

.forgot-password:hover {
  color: #0056b3; /* 悬停时颜色 */
  text-decoration: underline; /* 下划线效果 */
}
</style>
