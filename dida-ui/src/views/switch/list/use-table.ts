import { COLUMN_WIDTH_CONFIG } from '@/common/column-width-config'
import { deleteNamespace, listNamespace } from '@/service/modules/namespace'
import TooltipButton from '@/components/tooltip-button'
import { NSelect, NSpace, NSwitch, useDialog } from 'naive-ui'
import { h, reactive } from 'vue'
import { DeleteOutlined } from '@vicons/antd'
import { changeSwitchValue, pageSwitches } from '@/service/modules/switch'
export const useTable = (state: any) => {
  const variables = reactive({
    data: [],
    columns: [],
    searchForm: {
      username: ''
    },
    pagination: {
      count: 0,
      pageNum: 1,
      pageSize: 10,
      pageSizes: [10, 20, 50, 100, 9999]
    }
  })
  const createColumns = (variables: any) => {
    variables.columns = [
      {
        title: '开关编码',
        key: 'switchKey',
        resizable: true,
        minWidth: 100,
        maxWidth: 200
      },
      {
        title: '开关状态',
        key: 'value',
        resizable: true,
        minWidth: 200,
        maxWidth: 300,
        render: (row: any) => {
          const type = JSON.parse(row.type)

          if (type.switchType == 0) {
            return h(NSwitch, {
              ['default-value']: row.switchValue === 'true',
              ['on-update:value']: (newVal: string) =>
                handleChangeSwitchKeyValue(
                  row.groupCode,
                  row.namespaceCode,
                  row.switchKey,
                  newVal,
                  row
                )
            })
          } else if (type.switchType == 1) {
            return h(NSelect, {
              ['default-value']: row.switchValue,
              options: type.options?.map((e: any) => {
                return { label: e.key, value: e.value }
              }),
              ['on-update:value']: (newVal: string) =>
                handleChangeSwitchKeyValue(
                  row.groupCode,
                  row.namespaceCode,
                  row.switchKey,
                  newVal,
                  row
                )
            })
          }
        }
      },
      {
        title: '创建时间',
        key: 'createTime',
        resizable: true,
        minWidth: 200,
        maxWidth: 600
      },
      {
        title: '修改时间',
        key: 'updateTime',
        resizable: true,
        minWidth: 200,
        maxWidth: 600
      },
      {
        title: '操作',
        ...COLUMN_WIDTH_CONFIG.operation(1),
        render: (row: any) => {
          return h(NSpace, null, {
            default: () => [
              h(
                TooltipButton,
                {
                  type: 'error',
                  size: 'small',
                  text: '删除',
                  onClick: () => handleDeleteSwitch(row.switchKey)
                },
                {
                  icon: () => h(DeleteOutlined)
                }
              )
            ]
          })
        }
      }
    ]
  }
  const handleChangeSwitchKeyValue = (
    group: string,
    namespace: string,
    switchKey: string,
    value: string,
    row: any
  ) => {
    debugger
    const params = {
      group,
      namespace,
      switchKey,
      value
    }
    changeSwitchValue(params).then(() => {
      window.$message.success('修改成功')
    })
  }
  const getTableData = () => {
    const params = {
      group: state.groupCode,
      namespace: state.selectedNamespace,
      pageNum: variables.pagination.pageNum,
      pageSize: variables.pagination.pageSize
    }
    pageSwitches(params).then((res: any) => {
      variables.data = res.records
      variables.pagination.count = res.total
    })
  }
  const resetPageNum = () => {
    variables.pagination.pageNum = 1
    getTableData()
  }
  const handleDeleteSwitch = (key: string) => {}
  return {
    variables,
    createColumns,
    getTableData,
    resetPageNum
  }
}
