import { addNamespace } from '@/service/modules/namespace'
import { addUser } from '@/service/modules/user'
import { useDialog } from 'naive-ui'
import { SetupContext } from 'vue'

export const useModal = (
  state: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
  const handleAddOpen = (groupCode: string) => {
    state.show = true
    state.mode = 'add'
    state.groupCode = groupCode
  }
  const handleEditOpen = (form: any, groupCode: string) => {
    state.show = true
    state.mode = 'edit'
    state.form = { ...form }
    state.groupCode = groupCode
  }
  const handleConfirm = () => {
    debugger
    const params = {
      ...state.form
    }
    addNamespace(state.groupCode, params).then((res: any) => {
      window.$message.success('添加成功')
      handleClose()
      ctx.emit('confirm')
    })
  }
  const handleClose = () => {
    state.show = false
    ctx.emit('update:show', false)
  }
  const getTitle = () => {
    if (state.mode == 'add') {
      return `${state.groupCode}新增命名空间`
    } else {
      return `${state.groupCode}修改命名空间`
    }
  }
  return { handleClose, handleConfirm, handleAddOpen, handleEditOpen, getTitle }
}
