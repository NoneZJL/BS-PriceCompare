import CryptoJS from 'crypto-js'

export const hashSHA256 = (text) => {
  return CryptoJS.SHA256(text).toString(CryptoJS.enc.Hex)
}
