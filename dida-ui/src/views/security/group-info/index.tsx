import Card from '@/components/card'
import { getGroup } from '@/service/modules/group'
import { NButton, NDataTable, NPageHeader, NPagination, NSpace } from 'naive-ui'
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import type { Router } from 'vue-router'
import { useTable } from './use-table'

export default defineComponent({
  setup(props, ctx) {
    const router: Router = useRouter()
    const vars = reactive({
      code: String(router.currentRoute.value.params.code) || '',
      name: '',
      descp: ''
    })
    const { tableVariables, getTableData, resetPageNum, createColumns } =
      useTable(vars, props, ctx)
    const init = () => {
      getGroup(vars.code).then((res: any) => {
        vars.name = res.name
        vars.descp = res.descp
      })
      createColumns(tableVariables)
      getTableData()
    }
    init()
    const onBack = () => {
      router.go(-1)
    }

    return {
      ...toRefs(vars),
      ...toRefs(tableVariables),
      onBack,
      getTableData,
      resetPageNum
    }
  },
  render() {
    return (
      <NSpace vertical>
        <NSpace vertical>
          <NPageHeader
            title={this.code}
            onBack={this.onBack}
            subtitle={this.name}
          >
            {/* {this.descp} */}
          </NPageHeader>
        </NSpace>

        <Card>
          <NSpace justify='space-between'>
            <NSpace>
              <NButton type='primary'>新增</NButton>
            </NSpace>
            <NSpace></NSpace>
          </NSpace>
        </Card>
        <Card>
          {' '}
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
