import type { Component } from "vue";
import utils from "@/utils";

const modules = import.meta.glob("/src/views/**/**.tsx")
const components: { [key: string]: Component } = utils.mapping(modules)
debugger
export default {
    path: '/security',
    name: 'security',
    redirect: { name: 'security-users' },
    meta: { title: '安全中心', showSide: true, },
    component: () => import("@/layouts/content"),
    children: [
        {
            path: '/security/users',
            name: 'security-users',
            component: components['security-users'],
            meta: {
                title: '用户列表',
                activeMenu: 'security',

                auth: []
            }
        },
        {
            path: '/security/groups',
            name: 'security-groups',
            component: components['security-groups'],
            meta: {
                title: '用户组',
                activeMenu: 'security',

                auth: []
            }
        },
        {
            path: '/security/user-info',
            name: 'security-info',
            component: components['security-user-info'],
            meta: {
                title: '用户信息',
                activeMenu: 'security',

                auth: []
            }
        }
    ]
}