import type { Component } from "vue";
import utils from "@/utils";

const modules = import.meta.glob("/src/views/**/**.tsx")
const components: { [key: string]: Component } = utils.mapping(modules)

export default {
    path: '/switch',
    name: 'switch',
    redirect: { name: 'switch-list' },
    meta: { title: '开关中心' },
    component: () => import("@/layouts/content"),
    children: [
        {
            path: '/switch/list',
            name: 'switch-list',
            component: components['switch-list'],
            meta: {
                title: '开关列表',
                activeMenu: 'switch',

                auth: []
            }
        }
    ]
}