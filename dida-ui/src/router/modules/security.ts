import type { Component } from "vue";
import utils from "@/utils";

const modules = import.meta.glob("/src/views/**/**.tsx")
const components: { [key: string]: Component } = utils.mapping(modules)
debugger
export default {
    path: '/security',
    name: 'security',
    redirect: { name: 'security-users' },
    meta: { title: '开关中心' },
    component: () => import("@/layouts/content"),
    children: [
        {
            path: '/security/users',
            name: 'security-users',
            component: components['security-users'],
            meta: {
                title: '用户列表',
                activeMenu: 'security',
                showSide: true,
                auth: []
            }
        }
    ]
}