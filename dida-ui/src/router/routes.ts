import utils from '@/utils'
import { Component } from 'vue'
import type { RouteRecordRaw } from 'vue-router'

const modules = import.meta.glob('/src/views/**/**.tsx')
const components: { [key: string]: Component } = utils.mapping(modules)
/**
 * Login page
 */
const loginPage: RouteRecordRaw[] = [
    {
        path: '/',
        name: 'root',
        redirect: '/login'
    },
    {
        path: '/login',
        name: 'login',
        component: components['login'],
        meta: {
            auth: []
        }
    }
]
const basePage: RouteRecordRaw[] = []
const routes: RouteRecordRaw[] = [...basePage, ...loginPage]

// 重新组织后导出
export default routes