import Card from '@/components/card'
import { listNamespace } from '@/service/modules/namespace'
import { useSelectedGroup } from '@/store/selected-group/selectedGroup'
import {
  MenuOption,
  NButton,
  NEllipsis,
  NGrid,
  NGridItem,
  NMenu,
  NSpace
} from 'naive-ui'
import { defineComponent, ref, watch } from 'vue'
import { Router, useRouter, useRoute } from 'vue-router'

const Switch = defineComponent({
  setup(props, ctx) {
    const groupSelected = useSelectedGroup()
    const router = useRouter()
    const options: any = ref<Array<any>>([])
    const groupCode = ref(groupSelected.code)
    const selectedNamespace = ref()
    const gotoNamespacePage = () => {
      router.push({ path: '/switch/namespace' })
    }
    const handleGetNamespaces = () => {
      selectedNamespace.value = null
      listNamespace(groupCode.value).then((res: any) => {
        options.value = res?.map((e: any) => {
          return {
            label: `${e.name} (${e.code})`,
            key: `${e.code}`
          }
        })
        if (options.value.length == 0) {
          window.$message.warning(
            '用户组没有命名空间，请前往管理命名空间中添加'
          )
        }
      })
    }
    handleGetNamespaces()
    watch(
      () => groupSelected.code,
      (newVal) => {
        groupCode.value = newVal
        handleGetNamespaces()
      }
    )
    return {
      options,
      groupCode,
      gotoNamespacePage,
      selectedNamespace
    }
  },
  render() {
    return (
      <NSpace>
        {this.groupCode && this.groupCode != 'null' ? (
          <NGrid cols={24} x-gap={12}>
            <NGridItem span={4}>
              <Card title='选择命名空间'>
                <NButton style='width: 100%' onClick={this.gotoNamespacePage}>
                  管理命名空间
                </NButton>
                <div style={{ maxHeight: '1000px', marginTop: '10px' }}>
                  <NMenu
                    v-model:options={this.options}
                    v-model:value={this.selectedNamespace}
                    style={{
                      width: '15vw',
                      height: '1000px',
                      overflowY: 'auto'
                    }}
                    default-value='1'
                  />
                </div>
              </Card>
            </NGridItem>
            <NGridItem span={20}>
              {this.selectedNamespace ? (
                <Card>
                  <div style='width:80vw;min-height:85vh'>
                    {this.selectedNamespace}
                  </div>
                </Card>
              ) : (
                <span>请选择命名空间</span>
              )}
            </NGridItem>
          </NGrid>
        ) : (
          '用户没有用户组，无法执行操作'
        )}
      </NSpace>
    )
  }
})
export default Switch
