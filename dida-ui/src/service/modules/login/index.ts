import { axios } from "@/service/service"


export function login(params: any): any {
    return axios({
        url: 'api/v1/login',
        method: 'post',
        params
    })
}