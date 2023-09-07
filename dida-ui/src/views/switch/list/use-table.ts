import { COLUMN_WIDTH_CONFIG } from '@/common/column-width-config'
import { deleteNamespace, listNamespace } from '@/service/modules/namespace'
import TooltipButton from '@/components/tooltip-button'
import { NSpace, useDialog } from 'naive-ui'
import { h, reactive } from 'vue'
import { DeleteOutlined } from '@vicons/antd'
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
        maxWidth: 300
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
                  onClick: () => handleDeleteSwitch(row.key)
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
  const getTableData = () => {}
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
