import { axios } from '@/service/service'

export const listGroups = (params: any): any => {
  return axios({
    url: 'api/v1/groups/page',
    method: 'get',
    params
  })
}

export const addGroup = (params: any): any => {
    return axios({
        url: 'api/v1/groups',
        method: 'post',
        params
      })
}
