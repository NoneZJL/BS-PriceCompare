import request from '@/utils/request'

export const wphGetGoodsBySearchingNameService = (name) =>
  request.get(`/wph/getGoodsBySearchingName?name=${name}`)
