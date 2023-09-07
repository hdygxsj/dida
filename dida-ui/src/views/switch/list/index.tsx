import Card from '@/components/card'
import { listNamespace } from '@/service/modules/namespace'
import { useSelectedGroup } from '@/store/selected-group/selectedGroup'
import { useTable } from './use-table'
import {
  MenuOption,
  NButton,
  NConfigProvider,
  NDataTable,
  NEllipsis,
  NGrid,
  NGridItem,
  NMenu,
  NPagination,
  NSpace
} from 'naive-ui'
import { defineComponent, reactive, ref, toRefs, watch } from 'vue'
import { Router, useRouter, useRoute } from 'vue-router'
import Edit from './edit'

const Switch = defineComponent({
  setup(props, ctx) {
    const groupSelected = useSelectedGroup()
    const router = useRouter()
    const options: any = ref<Array<any>>([])
    const groupCode = ref(groupSelected.code)
    const state = reactive({
      editModalRef: ref(),
      editModalShow: false
    })
    const { variables, getTableData, resetPageNum, createColumns } =
      useTable(state)
    createColumns(variables)
    getTableData()
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
      selectedNamespace,
      ...toRefs(variables),
      getTableData,
      resetPageNum,
      createColumns,
      ...toRefs(state)
    }
  },
  render() {
    return (
      <NSpace>
        <Edit
          ref='editModalRef'
          v-model:show={this.editModalShow}
          onConfirm={this.getTableData}
        ></Edit>
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
                <NConfigProvider>
                  <NSpace vertical>
                    <Card>
                      <NSpace justify='space-between'>
                        <NSpace>
                          <NButton
                            type='primary'
                            onClick={() => this.editModalRef.handleAddOpen()}
                          >
                            新增
                          </NButton>
                        </NSpace>
                        <NSpace></NSpace>
                      </NSpace>
                    </Card>
                    <Card>
                      <NSpace vertical>
                        <NDataTable
                          columns={this.columns}
                          data={this.data}
                          row-class-name='items'
                        />
                        <NSpace justify='center' align='center'>
                          <span> {`共 ${this.pagination.count} 条`}</span>
                          <NPagination
                            v-model:page={this.pagination.pageNum}
                            v-model:page-size={this.pagination.pageSize}
                            item-count={this.pagination.count}
                            show-size-picker
                            page-sizes={this.pagination.pageSizes}
                            show-quick-jumper
                            // prefix={(e)=>}
                            onUpdatePage={this.getTableData}
                            onUpdatePageSize={this.resetPageNum}
                            v-slots={{
                              goto: '跳到'
                            }}
                          />
                        </NSpace>
                      </NSpace>
                    </Card>
                  </NSpace>
                </NConfigProvider>
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
