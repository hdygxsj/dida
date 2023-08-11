import { defineStore } from "pinia";


export const useUserStore = defineStore(
    {
        id: 'user',
        state: (): UserState => ({
            token: "",
            userInfo: {},
            roles: []
        }),
        persist: true,
        getters: {
            getToken(): string {
                return this.token
            },
            getUserInfo(): any | {} {
                return this.userInfo
            },
            getRoles(): Array<any> | [] {
                return this.roles
            }
        },
        actions: {
            setToken(token: string): void {
                this.token = token
            },
            setUserInfo(userInfo: any): void {
                this.userInfo = userInfo
            },
            setRoles(roles: any): void {
                this.roles = roles
            }
        }

    }
)