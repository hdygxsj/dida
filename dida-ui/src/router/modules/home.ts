import type { Component } from "vue";
import utils from "@/utils";

const modules = import.meta.glob("/src/views/**/**.tsx")
const components: { [key: string]: Component } = utils.mapping(modules)
debugger
export default {
    path: '/',
    name: '',
    redirect: { name: 'home' },
    meta: { title: '开关中心' },
    component: () => import("@/layouts/content"),
    children: [
        {
            path: '/home',
            name: 'home',
            component: components['home'],
            meta: {
                title: '首页',
                activeMenu: 'home',
                auth: []
            }
        }
    ]
}