import Card from '@/components/card'
import { NButton, NDataTable, NInput, NPagination, NSpace } from 'naive-ui'
import { defineComponent, ref, toRefs } from 'vue'
import { useTable } from './use-table'
import Edit from './components/edit'

const Groups = defineComponent({
  setup(props, ctx) {
    const {
      variables,
      getTableData,
      createColumns,
      resetPageNum,
      handletDeleteGroup
    } = useTable()
    createColumns(variables)
    getTableData()
    const show = ref(false)
    const editModalRef = ref()
    const handleAdd = () => {
      show.value = true
    }

    return {
      ...toRefs(variables),
      getTableData,
      show,
      handleAdd,
      editModalRef,
      resetPageNum,
      handletDeleteGroup
    }
  },
  render() {
    return (
      <NSpace vertical>
        <Edit
          ref='editModalRef'
          v-model:show={this.show}
          onConfirm={this.resetPageNum}
        ></Edit>
        <Card>
          <NSpace justify='space-between'>
            <NSpace>
              <NButton
                type='primary'
                onClick={() => this.editModalRef.onAddOpen()}
              >
                新增
              </NButton>
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
export default Groups
