import { addNamespace } from '@/service/modules/namespace'
import { addUser } from '@/service/modules/user'
import { useDialog } from 'naive-ui'
import { SetupContext } from 'vue'

export const useModal = (
  state: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
  const handleAddOpen = () => {
    state.show = true
    state.mode = 'add'
  }
  const handleEditOpen = (form: any) => {
    state.show = true
    state.mode = 'edit'
    state.form = { ...form }
  }
  const handleConfirm = () => {
    debugger
    const params = {
      ...state.form
    }
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
