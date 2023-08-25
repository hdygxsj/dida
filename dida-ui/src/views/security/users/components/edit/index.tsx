import Modal from '@/components/modal'
import { NForm, NFormItem, NInput, NModal } from 'naive-ui'
import { defineComponent, reactive, ref, toRefs } from 'vue'
import { useModal } from './use-modal'

export default defineComponent({
  name: 'EditModal',
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
            ></NInput>
          </NFormItem>
          <NFormItem path='roles' label='角色'>
            {/* <NInput v-model:value={this.form.name} placeholder={''}></NInput> */}
          </NFormItem>
        </NForm>
      </Modal>
    )
  }
})
