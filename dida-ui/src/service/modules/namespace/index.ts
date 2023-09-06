import { axios } from '@/service/service'

export const addNamespace = (groupCode: string, params: any) => {
  return axios({
    url: `api/v1/groups/${groupCode}/namespaces`,
    method: 'post',
    params
  })
}

export const listNamespace = (groupCode: string) => {
  return axios({
    url: `api/v1/groups/${groupCode}/namespaces`,
    method: 'get'
  })
}

export const deleteNamespace = (groupCode: string, code: string) => {
  return axios({
    url: `api/v1/groups/${groupCode}/namespaces/${code}`,
    method: 'delete'
  })
}
