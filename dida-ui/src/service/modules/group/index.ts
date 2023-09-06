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

export const deleteGroup = (code: string): any => {
  return axios({
    url: `api/v1/groups/${code}`,
    method: 'delete'
  })
}

export const getGroup = (code: string): any => {
  return axios({
    url: `api/v1/groups/${code}/info`,
    method: 'get'
  })
}

export const pageGroupUsers = (params: any): any => {
  return axios({
    url: `api/v1/groups/${params.code}/members`,
    method: 'get',
    params
  })
}

export const addGroupUsers = (params: any): any => {
  return axios({
    url: `api/v1/groups/${params.code}/members`,
    method: 'post',
    params
  })
}

export const deleteGroupUser = (params: any): any => {
  return axios({
    url: `api/v1/groups/${params.code}/members/${params.username}`,
    method: 'delete'
  })
}

export const listMyGroup = (params: any): any => {
  return axios({
    url: `api/v1/groups`,
    method: 'get',
    params
  })
}
