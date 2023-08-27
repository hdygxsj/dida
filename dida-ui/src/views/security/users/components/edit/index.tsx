import Modal from '@/components/modal'
import { NForm, NFormItem, NInput, NModal, NSelect } from 'naive-ui'
import { PropType, defineComponent, reactive, ref, toRefs } from 'vue'
import { useModal } from './use-modal'
import { useDialog } from 'naive-ui'
const props = {
  show: {
    type: Boolean as PropType<boolean>,
    default: false
  }
}
export default defineComponent({
  emits: ['confirm', 'update:show', 'cancel'],
  setup(props, ctx) {
    const vars = reactive({
      show: false,
      mode: 'add',
      form: {
        username: '',
        roles: []
      },
      formRef: ref()
    })
    useDialog().create({
      content:'dasd'
    })
    const { onClose, onConfirm, onAddOpen, onEditOpen } = useModal(vars, ctx)
    return {
      ...toRefs(vars),
      onClose,
      onConfirm,
      onAddOpen,
      onEditOpen
    }
  },
  render() {
    return (
      <Modal
        v-model:show={this.show}
        title='新增用户'
        onCancel={this.onClose}
        onConfirm={this.onConfirm}
      >
        <NForm model={this.form} ref='formRef'>
          <NFormItem path='username' label='用户名'>
            <NInput
              v-model:value={this.form.username}
              placeholder={''}
              disabled={this.mode == 'edit'}
            ></NInput>
          </NFormItem>
          <NFormItem path='roles' label='角色'>
            <NSelect v-model:value={this.form.roles} placeholder={''}></NSelect>
          </NFormItem>
        </NForm>
      </Modal>
    )
  }
})
