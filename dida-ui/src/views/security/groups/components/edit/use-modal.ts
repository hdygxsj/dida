import { addGroup } from '@/service/modules/group'
import { SetupContext } from 'vue'

export const useModal = (
  data: any,
  ctx: SetupContext<('update:show' | 'cancel' | 'confirm')[]>
) => {
  const onAddOpen = () => {
    
    data.show = true
    data.mode = 'add'
  }
  const onEditOpen = () => {
    data.show = true
    data.mode = 'edit'
  }
  const onConfirm = async () => {
    let error = await data.formRef.validate((errors: any) => {
      if (errors) {
        return true
      } else {
        false
      }
    })
    if (error) {
      return
    } else {
      if (data.mode === 'add') {
        addGroup({ ...data.form }).then(() => {
          window.$message.success('添加成功')
          ctx.emit('confirm')
          onClose()
        })
      }
    }
  }
  const getTitle = () => {
    if (data.mode == 'add') {
      return '添加'
    } else {
      return '编辑'
    }
  }
  const onClose = () => {
    data.show = false
    ctx.emit('update:show', false)
  }
  return {
    onAddOpen,
    onClose,
    onEditOpen,
    getTitle,
    onConfirm
  }
}
