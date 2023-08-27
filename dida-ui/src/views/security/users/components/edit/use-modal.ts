import { addUser } from '@/service/modules/user'
import { useDialog } from 'naive-ui'
import { SetupContext } from 'vue'

export const useModal = (
  data: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
 
  const onAddOpen = () => {
    data.show = true
    data.mode = 'add'
  }
  const onEditOpen = (username: string) => {
    data.show = true
    data.mode = 'edit'
    data.form.username = username
  }
  const onConfirm = () => {
    debugger
    const params = {
      ...data.form
    }
    addUser(params).then((res: any) => {
      const dialog =  useDialog()
      dialog.create({
        title:'创建成功',
        content:`用户的初始密码是${res.password},你将是最后一次见到这个密码，请立即修改密码`
      })
      ctx.emit('confirm')
      onClose()
    })
  }
  const onClose = () => {
    data.show = false
    ctx.emit('update:show', false)
  }
  return { onClose, onConfirm, onAddOpen, onEditOpen }
}
