import request from '@/utils/request'

export const jdGetGoodsBySearchingNameService = (name) =>
  request.get(`/jd/getGoodsBySearchingName?name=${name}`)
