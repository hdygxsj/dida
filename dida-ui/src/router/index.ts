import {
    createRouter,
    createWebHistory,
    NavigationGuardNext,
    RouteLocationNormalized
} from 'vue-router'
import routes from './routes'
import NProgress from 'nprogress'

const router = createRouter({
    history: createWebHistory(
        import.meta.env.MODE === 'production' ? '/dida/ui/' : '/'
    ),
    routes
})
export default router