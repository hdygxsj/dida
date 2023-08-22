import { axios } from "@/service/service"


export function getUserInfo(): any {
    return axios({
        url: 'api/v1/users/info',
        method: 'get',
    })
}