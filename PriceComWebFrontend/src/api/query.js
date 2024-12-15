import request from '@/utils/request'

export const getAllHistoryService = () => request.get('/getAllHistory')

export const queryGoodService = (goodName) => request.get(`/queryGood?name=${goodName}`)

export const deleteSingleHistoryByNameService = (goodName) =>
  request.get(`/deleteSingleHistory?name=${goodName}`)

export const deleteAllHistoryService = () => request.get('/deleteAllHistory')

export const queryDeatilGoodService = (from, url) => request.post('/getDetail', { from, url })

export const insertRemainderService = (description, price, img, detailUrl, from) =>
  request.post('/insertRemainder', { description, price, img, detailUrl, from })

export const getRemaindersService = () => request.get('/getRemainsers')

export const deleteRemainderService = (id) => request.get(`/deleteRemainder?id=${id}`)

export const sendRemindEmailService = (id) => request.get(`/sendRemindEmail?id=${id}`)
