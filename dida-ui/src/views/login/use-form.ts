import { login } from "@/service/modules/login"
import { useRouteStore } from "@/store/route/route"

import { useUserStore } from "@/store/user/user"
import { AxiosResponse } from "axios"
import { reactive } from "vue"
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
        login({ ...variables.loginForm }).then((res: AxiosResponse<string>) => {
            userStore.setToken(res.data)
            const path = routeStore.lastRoute

            router.push({ path: path || '/home' })
        })
    }

    const handleGithubLoginClick = () => {
        window.location.href = `https://github.com/login/oauth/authorize?client_id=8e9e1b8fcf23ec1ca7b5&redirect_uri=http://localhost:8080/dida/api/v1/login/oauth`
    }

    const handleLoginByToken = () => {
        if (route.query.status === 'success') {
            let token = String(route.query.token)
            const path = routeStore.lastRoute

            userStore.setToken(token)
            router.push({ path: path || '/home' })

        }
    }
    return {
        variables,
        handleLogin,
        handleGithubLoginClick,
        handleLoginByToken
    }
}