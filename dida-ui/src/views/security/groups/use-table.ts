import { COLUMN_WIDTH_CONFIG } from '@/common/column-width-config'
import ButtonLink from '@/components/button-link'
import TooltipButton from '@/components/tooltip-button'
import { deleteGroup, listGroups } from '@/service/modules/group'
import { DeleteOutlined } from '@vicons/antd'
import { NButton, NSpace, NTooltip } from 'naive-ui'
import { h, reactive } from 'vue'
import { useRouter } from 'vue-router'
import type { Router } from 'vue-router'

export function useTable() {
  const router: Router = useRouter()
  const variables = reactive({
    data: [],
    searchForm: {
      name: '',
      code: ''
    },
    columns: [],
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
        title: '简称',
        key: 'code',
        resizable: true,
        minWidth: 200,
        maxWidth: 600,
        render: (row: any) => {
          return h(
            ButtonLink,
            {
              onClick: () => router.push({path:`/security/groups/${row.code}`})
            },
            {
              default: () => row.code
            }
          )
        }
      },
      {
        title: '全称',
        key: 'name',
        resizable: true,
        minWidth: 200,
        maxWidth: 600,
      },
      {
        title: '描述',
        key: 'descp',
        resizable: true,
        minWidth: 200,
        maxWidth: 600
      },
      {
        title: '维护时间',
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
                  onClick: () => handletDeleteGroup(row.code)
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
      name: variables.searchForm.name,
      code: variables.searchForm.code
    }
    listGroups(params).then((res: any) => {
      variables.data = res.records
      variables.pagination.count = res.total
    })
  }
  const resetPageNum = () => {
    variables.pagination.pageNum = 1
    getTableData()
  }
  const handletDeleteGroup = (code: any) => {
    debugger
    deleteGroup(code).then(() => {
      window.$message.success('删除成功')
      getTableData()
    })
  }
  return {
    variables,
    getTableData,
    createColumns,
    resetPageNum,
    handletDeleteGroup
  }
}
