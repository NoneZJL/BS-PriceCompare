import request from '@/utils/request'
import { hashSHA256 } from '@/utils/hash'

export const userLoginService = (username, password) =>
  request.post('/login', { username, password: hashSHA256(password) })

export const userRegisterService = (username, password, email, emailCode, lastEmailCode) =>
  request.post('/register', {
    username,
    password: hashSHA256(password),
    email,
    emailCode,
    lastEmailCode
  })

export const modifyPasswordService = (email, password, newEmailCode, lastEmailCode) =>
  request.post('/updatePassword', {
    email,
    password: hashSHA256(password),
    newEmailCode,
    lastEmailCode
  })

export const userLoginByEmailService = (email, password) =>
  request.post('/loginByEmail', { email, password: hashSHA256(password) })

export const getEmailCodeService = (email) => request.get(`/getEmailCode?email=${email}`)

export const getUserInfoService = () => request.get('/getUserInfo')

export const updateUserInfoService = (phone, address) =>
  request.post('/updateUserInfo', { phone, address })

export const getEmailCodeInForgetPasswordService = (email) =>
  request.get(`/getEmailCodeInForgetPassword?email=${email}`)
