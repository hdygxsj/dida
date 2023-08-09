import { NIcon } from "naive-ui"
import { reactive, h } from "vue"
import { HomeOutlined, LockOutlined } from "@vicons/antd"

export function useDataList() {
    const state = reactive({
        menuOptions: [],

    })

    const renderIcon = (icon: any) => {
        return () => h(NIcon, null, { default: () => h(icon) })
    }

    const changeMenuOption = (state: any) => {
        state.menuOptions = [
            {
                label: '开关中心',
                key: 'switch',
                icon: renderIcon(HomeOutlined)
            }, {
                label: '安全中心',
                key: 'security',
                icon: renderIcon(LockOutlined)
            }
        ]
    }

    return {
        state,
        changeMenuOption
    }
}