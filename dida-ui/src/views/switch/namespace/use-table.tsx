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
        title: '编码',
        key: 'code',
        resizable: true,
        minWidth: 100,
        maxWidth: 200
      },
      {
        title: '名称',
        key: 'name',
        resizable: true,
        minWidth: 200,
        maxWidth: 300
      },
      {
        title: '描述',
        key: 'descp',
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
                  onClick: () => handleDeleteNamespace(row.code)
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
  const getTableData = () => {
    listNamespace(state.groupCode).then((res: any) => {
      variables.data = res
    })
  }
  const resetPageNum = () => {
    variables.pagination.pageNum = 1
    getTableData
  }
  const handleDeleteNamespace = (code: string) => {
    deleteNamespace(state.groupCode, code).then(() => {
      window.$message.success('删除成功')
      getTableData()
    })
  }
  return {
    variables,
    createColumns,
    getTableData,
    resetPageNum
  }
}
