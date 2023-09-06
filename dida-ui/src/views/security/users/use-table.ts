import { COLUMN_WIDTH_CONFIG } from '@/common/column-width-config'
import {
  deleteUser,
  pageUsers,
  resetPasswordByAdmin
} from '@/service/modules/user'
import {
  DeleteOutlined,
  TrademarkCircleOutlined,
  KeyOutlined
} from '@vicons/antd'
import TooltipButton from '@/components/tooltip-button'
import { NSpace, useDialog } from 'naive-ui'
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
    deleteUser(username).then(() => {
      window.$message.success('删除成功')
      getTableData()
    })
  }
  const handleUserAdd = (password: any) => {
    resetPageNum()
    window.$dialog.create({
      title: '创建成功',
      content: `用户的初始密码是${password},你将是最后一次见到这个密码，请立即修改密码`
    })
  }
  const handleRestPassword = (username: any) => {
    resetPasswordByAdmin(username).then((res: any) => {
      window.$dialog.create({
        title: '修改成功',
        content: `用户的新密码是${res},你将是最后一次见到这个密码，请立即修改密码`
      })
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
      {
        title: '是否启用',
        key: 'type',
        resizable: true,
        minWidth: 200,
        maxWidth: 600,
        render: (row: any) => {
          if (row.type === 1) {
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
        ...COLUMN_WIDTH_CONFIG.operation(3),
        render: (row: any) => {
          return h(NSpace, null, {
            default: () => [
              h(
                TooltipButton,
                {
                  type: 'info',
                  size: 'small',
                  text: '重制密码',
                  onClick: () => handleRestPassword(row.username)
                },
                {
                  icon: () => h(KeyOutlined)
                }
              ),
              h(
                TooltipButton,
                {
                  type: 'info',
                  size: 'small',
                  text: '设置角色',
                  onClick: () => vars.editModalRef.handleEditOpen(row.username)
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
    getTableData,
    handleUserAdd
  }
}
