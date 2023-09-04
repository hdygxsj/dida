import { addUser } from '@/service/modules/user'
import { useDialog } from 'naive-ui'
import { SetupContext } from 'vue'

export const useModal = (
  state: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {

  const onAddOpen = () => {
    state.show = true
    state.mode = 'add'
  }
  const onEditOpen = (username: string) => {
    state.show = true
    state.mode = 'edit'
    state.form.username = username
  }
  const onConfirm = () => {
    
    const params = {
      ...state.form
    }
    addUser(params).then((res: any) => {
      ctx.emit('confirm', res.password)
      onClose()
    })
  }
  const onClose = () => {
    state.show = false
    ctx.emit('update:show', false)
  }
  return { onClose, onConfirm, onAddOpen, onEditOpen }
}
