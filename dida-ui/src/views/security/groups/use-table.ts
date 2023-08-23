import { listGroups } from '@/service/modules/group'
import { reactive } from 'vue'

export function useTable() {
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
        title: '用户组简称',
        key: 'code',
        resizable: true,
        minWidth: 200,
        maxWidth: 600
      },
      {
        title: '名称',
        key: 'name',
        resizable: true,
        minWidth: 200,
        maxWidth: 600
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
        width: 300
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
  const resetPageNum = ()=>{
    variables.pagination.pageNum = 1
    getTableData()
  }
  return {
    variables,
    getTableData,
    createColumns,
    resetPageNum
  }
}
