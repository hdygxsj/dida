import axios, { AxiosRequestConfig, AxiosResponse, AxiosError, InternalAxiosRequestConfig } from 'axios'
import { useUserStore } from '@/store/user/user'
import qs from 'qs'
import _ from 'lodash'
import router from '@/router'

const userStore = useUserStore()

const handleError = (res: AxiosResponse<any, any>) => {
    window.$message.error(res.data.message)
}


const baseRequestConfig: AxiosRequestConfig = {
    baseURL:
        import.meta.env.MODE === 'development'
            ? '/dida'
            : import.meta.env.VITE_APP_PROD_WEB_URL + '/dida',
    timeout: 20000,
    transformRequest: (params) => {
        if (_.isPlainObject(params)) {
            return qs.stringify(params, { arrayFormat: 'repeat' })
        } else {
            return params
        }
    },
    paramsSerializer: (params) => {
        return qs.stringify(params, { arrayFormat: 'repeat' })
    }
}


const service = axios.create(baseRequestConfig)

const err = (err: AxiosError): Promise<AxiosError> => {
    if (err.response?.status === 401 || err.response?.status === 504) {
        userStore.setToken('')
        userStore.setRoles([])
        userStore.setUserInfo({})
        window.$message.error('未登录或会话过期，请重新登陆')
        router.push({ path: '/login' })
    }

    return Promise.reject(err)
}

service.interceptors.request.use((config: InternalAxiosRequestConfig<any>) => {
    debugger

    config.headers = config.headers || {}

    config.headers && (config.headers['TOKEN'] = userStore.getToken)

    return config
}, err)


service.interceptors.response.use((res: AxiosResponse) => {
    // No code will be processed
    if (res.data.code === undefined) {
        return res.data
    }

    switch (res.data.code) {
        case 0:
            return res.data.data
        default:
            handleError(res)
            throw new Error()
    }
}, err)

export { service as axios }
