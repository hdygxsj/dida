import { addGroupUsers } from '@/service/modules/group'
import { SetupContext } from 'vue'

export const useModal = (
  vars: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
  const handleOpen = (code: string) => {
    vars.show = true
    vars.code = code
  }
  const handleClose = () => {
    vars.show = false
    ctx.emit('update:show', false)
  }
  const handleConfirm = () => {
    let params = {
      code: vars.code,
      username: vars.username
    }
    addGroupUsers(params).then(() => {
      window.$message.success('添加成功')
      handleClose()
      ctx.emit('confirm')
    })
  }
  return {
    handleClose,
    handleOpen,
    handleConfirm
  }
}
