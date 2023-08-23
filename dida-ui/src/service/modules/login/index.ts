import { axios } from "@/service/service"


export function login(params: any): any {
    return axios({
        url: 'api/v1/login',
        method: 'post',
        params
    })
}

export function getOauth2Providers():any{
    return axios({
        url:'api/v1/oauth2-providers',
        method:'get'
    })
}

