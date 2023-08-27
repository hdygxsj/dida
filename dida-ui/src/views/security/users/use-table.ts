import { COLUMN_WIDTH_CONFIG } from '@/common/column-width-config'
import { deleteUser, pageUsers } from '@/service/modules/user'
import { DeleteOutlined, TrademarkCircleOutlined } from '@vicons/antd'
import TooltipButton from '@/components/tooltip-button'
import { NSpace } from 'naive-ui'
import { h, reactive } from 'vue'

export const useTable = (vars: any, ctx: any) => {
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
  const handlerDeleteUser = (username: string) => {
    debugger
    deleteUser(username).then(()=>{
      window.$message.success('删除成功')
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
        title: '超级用户',
        key: 'superUser',
        resizable: true,
        minWidth: 200,
        maxWidth: 600,
        render: (row: any) => {
          if (row.superUser) {
            return '是'
          } else {
            return '否'
          }
        }
      },
      //   {
      //     title: '是否启用',
      //     key: 'type',
      //     resizable: true,
      //     minWidth: 200,
      //     maxWidth: 600
      //   }
      {
        title: '操作',
        ...COLUMN_WIDTH_CONFIG.operation(2),
        render: (row: any) => {
          return h(NSpace, null, {
            default: () => [
              h(
                TooltipButton,
                {
                  type: 'info',
                  size: 'small',
                  text: '设置角色',
                  onClick: () => vars.editModalRef.onEditOpen(row.username)
                },
                {
                  icon: () => h(TrademarkCircleOutlined)
                }
              ),
              h(
                TooltipButton,
                {
                  type: 'error',
                  size: 'small',
                  text: '删除',
                  onClick: () => handlerDeleteUser(row.username)
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
      pageNum: variables.pagination.pageNum,
      pageSize: variables.pagination.pageSize,
      ...variables.searchForm
    }
    pageUsers(params).then((res: any) => {
      variables.data = res.records
      variables.pagination.count = res.total
    })
  }

  const resetPageNum = () => {
    variables.pagination.pageNum = 1
    getTableData()
  }

  return {
    variables,
    createColumns,
    resetPageNum,
    getTableData
  }
}
