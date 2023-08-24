import { NIcon } from "naive-ui"
import { reactive, h } from "vue"
import { HomeOutlined, BulbOutlined, LockOutlined, TeamOutlined, UserOutlined, SettingOutlined, KeyOutlined, LogoutOutlined, TrademarkCircleOutlined } from "@vicons/antd"

export function useDataList() {
    const state = reactive({
        menuOptions: [],
        isShowSide: true,
        sideMenuOptions: [],
        headerMenuOptions: [],
        userDropdownOptions:[]
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
                        label: '角色',
                        key: `/security/roles`,
                        icon: renderIcon(TrademarkCircleOutlined)
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
    const changeUserDropdown = (state: any) => {
        state.userDropdownOptions = [
          {
            label: "个人信息",
            key: 'profile',
            icon: renderIcon(UserOutlined)
          },
          {
            label:"修改密码",
            key: 'password',
            icon: renderIcon(KeyOutlined),
            // disabled: userStore.getSecurityConfigType !== 'PASSWORD'
          },
          {
            label: "退出登录",
            key: 'logout',
            icon: renderIcon(LogoutOutlined)
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
        changeUserDropdown,
        changeHeaderMenuOptions

    }
}