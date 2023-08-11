import { NIcon } from "naive-ui"
import { reactive, h } from "vue"
import { HomeOutlined, BulbOutlined, LockOutlined, TeamOutlined, UserOutlined, SettingOutlined } from "@vicons/antd"

export function useDataList() {
    const state = reactive({
        menuOptions: [],
        isShowSide: true,
        sideMenuOptions: [],
        headerMenuOptions: []
    })

    const renderIcon = (icon: any) => {
        return () => h(NIcon, null, { default: () => h(icon) })
    }

    const changeMenuOption = (state: any) => {
        state.menuOptions = [
            {
                label: '首页',
                key: 'home',
                icon: renderIcon(HomeOutlined)
            },
            {
                label: '开关中心',
                key: 'switch',
                icon: renderIcon(BulbOutlined)
            }, {
                label: '安全中心',
                key: 'security',
                icon: renderIcon(LockOutlined),
                children: [
                    {
                        label: '用户组',
                        key: `/security/groups`,
                        icon: renderIcon(TeamOutlined)
                    },
                    {
                        label: '用户',
                        key: `/security/users`,
                        icon: renderIcon(UserOutlined)
                    },
                    {
                        label: '个人信息',
                        key: `/security/user-info`,
                        icon: renderIcon(SettingOutlined)
                    }

                ]
            }
        ]
    }

    const changeHeaderMenuOptions = (state: any) => {
        state.headerMenuOptions = state.menuOptions.map(
            (item: { label: string; key: string; icon: any }) => {
                return {
                    label: item.label,
                    key: item.key,
                    icon: item.icon
                }
            }
        )
    }

    return {
        state,
        changeMenuOption,

        changeHeaderMenuOptions

    }
}