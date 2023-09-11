import { axios } from '@/service/service'

export const addSwitch = (
  group: string,
  namespace: string,
  switchKey: string,
  type: string
) => {
  return axios({
    url: `api/v1/groups/${group}/namespaces/${namespace}/switches`,
    method: 'post',
    data: {
      switchKey,
      type
    },
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    transformRequest: (params) => JSON.stringify(params)
  })
}

export function pageSwitches(params: any): any {
  debugger
  return axios({
    url: `api/v1/groups/${params.group}/namespaces/${params.namespace}/switches/page`,
    method: 'get',
    params
  })
}

export function changeSwitchValue(params: any): any {
  return axios({
    url: `api/v1/groups/${params.group}/namespaces/${params.namespace}/switches/${params.switchKey}/set-value`,
    method: 'put',
    params
  })
}
