import Card from '@/components/card'
import { NButton, NDataTable, NInput, NPagination, NSpace } from 'naive-ui'
import { defineComponent, toRefs } from 'vue'
import { useTable } from './use-table'

const Groups = defineComponent({
  setup(props, ctx) {
    const { variables, getTableData, createColumns } = useTable()
    createColumns(variables)
    getTableData()
    return {
      ...toRefs(variables),
      getTableData
    }
  },
  render() {
    return (
      <NSpace vertical>
        <Card>
          <NSpace justify='space-between'>
            <NSpace>
              <NButton type="primary">新增</NButton>
            </NSpace>
            <NSpace>
              <NInput
                v-model:value={this.searchForm.code}
                placeholder='输入用户组简称'
              ></NInput>
              <NInput
                v-model:value={this.searchForm.name}
                placeholder='输入用户组名称'
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
                page-count={this.pagination.count}
                show-size-picker
                page-sizes={this.pagination.pageSizes}
                show-quick-jumper
                // prefix={(e)=>}
                onUpdatePage={this.getTableData}
                onUpdatePageSize={this.getTableData}
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
export default Groups
