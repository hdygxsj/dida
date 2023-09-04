import { COLUMN_WIDTH_CONFIG } from '@/common/column-width-config'
import { DeleteOutlined } from '@vicons/antd'
import { NSpace } from 'naive-ui/es/space'
import { h, reactive } from 'vue'
import TooltipButton from '@/components/tooltip-button'
import { deleteGroupUser, pageGroupUsers } from '@/service/modules/group'

export const useTable = (vars: any, props: any, ctx: any) => {
  const tableVariables = reactive({
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
  const resetPageNum = () => {
    tableVariables.pagination.pageNum = 1
    getTableData()
  }
  const handleDeleteMmber = (username: string) => {
    const params = {
      code: vars.code,
      username
    }
    deleteGroupUser(params).then(() => {
      window.$message.success(`移除用户${username}成功`)
      getTableData()
    })
  }
  const createColumns = (variables: any) => {
    variables.columns = [
      {
        title: '用户号',
        key: 'username',
        resizable: true,
        minWidth: 200,
        maxWidth: 600
      },
      {
        title: '添加时间',
        key: 'createTime',
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
                  onClick: () => handleDeleteMmber(row.username)
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
    const params = {
      code: vars.code,
      username: tableVariables.searchForm.username,
      pageNum: tableVariables.pagination.pageNum,
      pageSize: tableVariables.pagination.pageSize
    }
    pageGroupUsers(params).then((res: any) => {
      tableVariables.data = res.records
      tableVariables.pagination.count = res.total
    })
  }
  return {
    tableVariables,
    getTableData,
    resetPageNum,
    createColumns
  }
}
