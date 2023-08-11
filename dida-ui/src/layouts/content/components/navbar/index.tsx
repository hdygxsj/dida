
import GithubOutlined from "@vicons/antd/es/GithubOutlined";
import { NMenu } from "naive-ui";
import { defineComponent, PropType, ref, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import Theme from "../theme";
import styles from './index.module.scss'
const Navbar = defineComponent({
    name: 'Navbar',
    props: {
        headerMenuOptions: {
            type: Array as PropType<any>,
            default: []
        },
    },
    setup(props, ctx) {
        const route = useRoute()
        const router = useRouter()
        const handleMenuClick = (key: string) => {
            router.push({ path: `/${key}` })
        }
        const menuKey = ref(route.meta.activeMenu as string)
        watch(
            () => route.path,
            () => {
                menuKey.value = route.meta.activeMenu as string
            }
        )
        return { handleMenuClick, menuKey }
    },
    render() {
        return (
            <div class={styles.container}>
                <span class={styles.logo}>分布式开关平台</span>
                <div class={styles.nav}>
                    <NMenu
                        value={this.menuKey}
                        mode='horizontal'
                        options={this.headerMenuOptions}
                        onUpdateValue={this.handleMenuClick}
                    />
                </div>
                <div class={styles.settings}>
                    {/* <div>退出</div> */}

                    <Theme />
                </div>
            </div>
        )
    }
})

export default Navbar