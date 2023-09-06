import { defineComponent, reactive, ref, toRefs } from 'vue'
import { useTable } from './use-table'
import { NButton, NDataTable, NPageHeader, NSpace, useMessage } from 'naive-ui'
import Card from '@/components/card'
import { useSelectedGroup } from '@/store/selected-group/selectedGroup'
import Edit from './edit'
import { useRouter } from 'vue-router'
import type { Router } from 'vue-router'
export default defineComponent({
  setup(props, ctx) {
    const router: Router = useRouter()
    window.$message = useMessage()
    const groupSelected = useSelectedGroup()
    const state = reactive({
      groupCode: groupSelected.code
    })
    const { variables, getTableData, resetPageNum, createColumns } =
      useTable(state)
    const init = () => {
      createColumns(variables)
      getTableData()
    }
    init()
    const editModalRef = ref()
    const editModalShow = ref(false)
    const handleNamespaceAdd = () => {
      debugger
      editModalRef.value.handAddOpen()
    }
    const onBack = () => {
      router.go(-1)
    }
    return {
      ...toRefs(state),
      ...toRefs(variables),
      getTableData,
      resetPageNum,
      handleNamespaceAdd,
      editModalRef,
      editModalShow,
      onBack
    }
  },
  render() {
    return (
      <NSpace vertical>
        <Edit
          ref='editModalRef'
          v-model:show={this.editModalShow}
          onConfirm={this.getTableData}
        ></Edit>
        <NPageHeader
          title={'命名空间管理'}
          onBack={this.onBack}
          // subtitle={this.name}
        >
          {/* {this.descp} */}
        </NPageHeader>
        <Card>
          <NSpace justify='space-between'>
            <NSpace>
              <NButton
                type='primary'
                onClick={() => this.editModalRef.handleAddOpen(this.groupCode)}
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
          </NSpace>
        </Card>
      </NSpace>
    )
  }
})
