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
  const handleEditOpen = (username: string) => {
    state.show = true
    state.mode = 'edit'
    state.form.username = username
  }
  const handleConfirm = () => {
    const params = {
      ...state.form
    }
    addUser(params).then((res: any) => {
      ctx.emit('confirm', res.password)
      handleClose()
    })
  }
  const handleClose = () => {
    state.show = false
    ctx.emit('update:show', false)
  }
  return { handleClose, handleConfirm, handleAddOpen, handleEditOpen }
}
