import { defineComponent, reactive, ref, toRefs } from 'vue'
import { useTable } from './use-table'
import {
  NButton,
  NDataTable,
  NDialogProvider,
  NInput,
  NPagination,
  NSpace,
  useDialog
} from 'naive-ui'
import Card from '@/components/card'
import Edit from './components/edit'

const Users = defineComponent({
  setup(props, ctx) {
    window.$dialog = useDialog()
    const vars = reactive({
      editModalRef: ref()
    })
    const {
      variables,
      createColumns,
      resetPageNum,
      getTableData,
      handleUserAdd
    } = useTable(vars, ctx)
    createColumns(variables)
    getTableData()

    return {
      ...toRefs(variables),
      createColumns,
      resetPageNum,
      getTableData,
      ...toRefs(vars),
      handleUserAdd
    }
  },
  render() {
    return (
      <NSpace vertical>
        <Edit ref='editModalRef' onConfirm={this.handleUserAdd}></Edit>
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
            <NSpace>
              <NInput
                v-model:value={this.searchForm.username}
                placeholder='输入用户号'
              ></NInput>

              <NButton onClick={this.getTableData}>搜索</NButton>
            </NSpace>
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
    )
  }
})
export default Users
