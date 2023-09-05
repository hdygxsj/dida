import { axios } from '@/service/service'

export function getUserInfo(): any {
  return axios({
    url: 'api/v1/users/info',
    method: 'get'
  })
}

export function listUser(params: any): any {
  return axios({
    url: 'api/v1/users',
    method: 'get',
    params
  })
}

export function pageUsers(params: any): any {
  return axios({
    url: 'api/v1/users/page',
    method: 'get',
    params
  })
}

export function deleteUser(username: string): any {
  return axios({
    url: `api/v1/users/${username}`,
    method: 'delete'
  })
}

export function addUser(params: any) {
  return axios({
    url: `api/v1/users`,
    method: 'post',
    params
  })
}
