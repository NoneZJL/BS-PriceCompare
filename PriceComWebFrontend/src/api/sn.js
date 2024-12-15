import request from '@/utils/request'

export const snGetGoodsBySearchingNameService = (name) =>
  request.get(`/sn/getGoodsBySearchingName?name=${name}`)
