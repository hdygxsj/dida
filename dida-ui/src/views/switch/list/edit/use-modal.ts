import { addNamespace } from '@/service/modules/namespace'
import { addSwitch } from '@/service/modules/switch'
import { addUser } from '@/service/modules/user'
import { useDialog } from 'naive-ui'
import { SetupContext } from 'vue'

export const useModal = (
  state: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
  const handleAddOpen = (groupCode: string, namespace: string) => {
    debugger
    state.show = true
    state.mode = 'add'
    state.groupCode = groupCode
    state.namespace = namespace
  }
  const handleEditOpen = (form: any, groupCode: string, namespace: string) => {
    state.show = true
    state.mode = 'edit'
    state.form = { ...form }
    state.groupCode = groupCode
    state.namespace = namespace
  }
  const handleConfirm = () => {
    debugger

    const params = {
      ...state.form
    }
    addSwitch(
      state.groupCode,
      state.namespace,
      params.switchKey,
      JSON.stringify(params.type)
    ).then((res: any) => {
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
      return `新增开关`
    } else {
      return `修改修改`
    }
  }
  return { handleClose, handleConfirm, handleAddOpen, handleEditOpen, getTitle }
}
