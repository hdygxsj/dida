import { NLayout, NLayoutContent, NLayoutHeader, useMessage } from 'naive-ui'
import { defineComponent, onMounted, ref, toRefs, watch } from 'vue'
import { useRoute } from 'vue-router'
import Navbar from './components/navbar'
import SideBar from './components/sidebar'
import { useDataList } from './use-dataList'
import { useRouteStore } from '@/store/route/route'

const Content = defineComponent({
  name: 'Content',
  setup(props, ctx) {
    const route = useRoute()
    window.$message = useMessage()
    const {
      state,
      changeMenuOption,
      changeHeaderMenuOptions,
      changeUserDropdown
    } = useDataList()
    onMounted(() => {
      changeMenuOption(state)
      getSideMenu(state)
      changeHeaderMenuOptions(state)
      changeUserDropdown(state)
    })
    const routeStore = useRouteStore()
    const sideKeyRef = ref()
    const getSideMenu = (state: any) => {
      const key = route.meta.activeMenu
      state.isShowSide = route.meta.showSide as boolean
      state.sideMenuOptions =
        state.menuOptions.filter((menu: { key: string }) => menu.key === key)[0]
          ?.children || state.menuOptions
    }
    watch(
      () => route.path,
      () => {
        if (route.path !== '/login') {
          routeStore.setLastRoute(route.path)
          state.isShowSide = route.meta.showSide as boolean
          getSideMenu(state)

          const currentSide = (
            route.meta.activeSide
              ? route.meta.activeSide
              : route.matched[1].path
          ) as string
          sideKeyRef.value = currentSide
        }
      },
      { immediate: true }
    )

    return {
      ...toRefs(state),
      getSideMenu,
      sideKeyRef
    }
  },
  render() {
    return (
      <NLayout position='absolute' style={{ height: '100%' }}>
        <NLayoutHeader style={{ height: '65px' }}>
          <Navbar
            class='tab-horizontal'
            headerMenuOptions={this.headerMenuOptions}
            userDropdownOptions={this.userDropdownOptions}
          ></Navbar>
        </NLayoutHeader>
        <NLayout hasSider={true} position='absolute' style='top:65px'>
          {this.isShowSide && (
            <SideBar
              sideMenuOptions={this.sideMenuOptions}
              sideKey={this.sideKeyRef}
            />
          )}
          <NLayoutContent
            native-scrollbar={false}
            style='padding: 16px 22px'
            contentStyle={'height: 100%'}
          >
            {/* das */}
            <router-view key={this.$route.fullPath} />
          </NLayoutContent>
        </NLayout>
      </NLayout>
    )
  }
})
export default Content
