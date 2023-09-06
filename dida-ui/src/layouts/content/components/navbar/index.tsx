import GithubOutlined from '@vicons/antd/es/GithubOutlined'
import { NMenu } from 'naive-ui'
import { defineComponent, PropType, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Theme from '../theme'
import styles from './index.module.scss'
import User from '../user'
import GroupSelectButton from '@/components/group-select-button'
import { useSelectedGroup } from '@/store/selected-group/selectedGroup'
const Navbar = defineComponent({
  name: 'Navbar',
  props: {
    headerMenuOptions: {
      type: Array as PropType<any>,
      default: []
    },
    userDropdownOptions: {
      type: Array as PropType<any>,
      default: []
    }
  },
  setup(props, ctx) {
    const route = useRoute()
    const router = useRouter()
    const selectedGroup = useSelectedGroup()
    const handleMenuClick = (key: string) => {
      router.push({ path: `/${key}` })
    }
    const handleGroupSelect = (code: any) => {
      selectedGroup.setCode(code)
    }
    const menuKey = ref(route.meta.activeMenu as string)
    const groupCode = ref()
    const initSelectedCode = () => {
      debugger
      groupCode.value = selectedGroup.code
    }
    initSelectedCode()
    watch(
      () => route.path,
      () => {
        menuKey.value = route.meta.activeMenu as string
      }
    )
    return { handleMenuClick, menuKey, handleGroupSelect, groupCode }
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
          <GroupSelectButton
            v-model:initCode={this.groupCode}
            onSelect={this.handleGroupSelect}
          ></GroupSelectButton>
          <Theme />
          <User userDropdownOptions={this.userDropdownOptions} />
        </div>
      </div>
    )
  }
})

export default Navbar
