import { SetupContext } from 'vue'

export const useModal = (
  data: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
  const onAddOpen = ()=>{
    data.show = true
    data.mode = 'add'
  }
  const onEditOpen = ()=>{
    data.show = true
    data.mode = 'edit'
  }
  const onConfirm = () => {
    onClose()
  }
  const onClose = () => {
    data.show = false
    ctx.emit('update:show', false)
  }
  return { onClose, onConfirm,onAddOpen,onEditOpen}
}
