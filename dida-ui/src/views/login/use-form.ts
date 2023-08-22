import { getOauth2Providers, login } from "@/service/modules/login"
import { OAuth2Provider } from "@/service/modules/login/types"
import { getUserInfo } from "@/service/modules/user"
import { useRouteStore } from "@/store/route/route"

import { useUserStore } from "@/store/user/user"
import { AxiosResponse } from "axios"
import { reactive, ref } from "vue"
import { Router, useRouter, useRoute } from "vue-router"

export const useForm = () => {

    const userStore = useUserStore()
    const route = useRoute()
    const router: Router = useRouter()
    const routeStore = useRouteStore()
    const variables = reactive(
        {
            loginForm: {
                username: "",
                password: ""
            }
        }
    )
    const handleLogin = () => {
        debugger
        login({ ...variables.loginForm }).then((res: any) => {
            userStore.setToken(res.token)
            const path = routeStore.lastRoute
            handleGetUserInfo()
            router.push({ path: path || '/home' })
        })
    }

    const handleGetOAuth2Provider = () => {
        getOauth2Providers().then((res: Array<OAuth2Provider> | []) => {
            oauth2Providers.value = res
        })
    }

    const oauth2Providers = ref<Array<OAuth2Provider> | []>([])

    const gotoOAuth2Page = async (oauth2Provider: OAuth2Provider) => {
        window.location.href = `${oauth2Provider.authorizationUri}?client_id=${oauth2Provider.clientId}` +
            `&response_type=code&redirect_uri=${oauth2Provider.redirectUri}?provider=${oauth2Provider.provider}`
    }

    const handleGetUserInfo = () =>{
        getUserInfo().then((res: any) => {
            debugger
            userStore.setUserInfo(res.baseInfo)
        })
    }

    const handleLoginByToken = () => {
        if (route.query.status === 'success') {
            let token = String(route.query.token)
            const path = routeStore.lastRoute
            userStore.setToken(token)
            handleGetUserInfo()
            router.push({ path: path || '/home' })

        }
    }


    return {
        variables,
        handleLogin,
        gotoOAuth2Page,
        handleLoginByToken,
        handleGetOAuth2Provider,
        oauth2Providers
    }
}