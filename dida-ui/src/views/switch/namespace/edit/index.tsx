import Modal from '@/components/modal'
import { NForm, NFormItem, NInput, NModal, NSelect, useMessage } from 'naive-ui'
import { PropType, defineComponent, reactive, ref, toRefs } from 'vue'
import { useModal } from './use-modal'
const props = {
  show: {
    type: Boolean as PropType<boolean>,
    default: false
  }
}

export default defineComponent({
  emits: ['confirm', 'update:show', 'cancel'],
  props,
  setup(props, ctx) {
    window.$message = useMessage()
    const vars = reactive({
      show: false,
      groupCode: '',
      mode: 'add',
      form: {
        code: '',
        name: '',
        descp: ''
      },
      formRef: ref(),
      initPassword: (password: any) => {}
    })

    const {
      handleClose,
      handleConfirm,
      handleAddOpen,
      handleEditOpen,
      getTitle
    } = useModal(vars, ctx)
    return {
      ...toRefs(vars),
      handleClose,
      handleConfirm,
      handleAddOpen,
      handleEditOpen,
      getTitle
    }
  },
  render() {
    return (
      <Modal
        v-model:show={this.show}
        title={this.getTitle()}
        onCancel={this.handleClose}
        onConfirm={this.handleConfirm}
      >
        <NForm model={this.form} ref='formRef'>
          <NFormItem path='username' label='编码'>
            <NInput v-model:value={this.form.code} placeholder={''}></NInput>
          </NFormItem>
          <NFormItem path='username' label='名称'>
            <NInput v-model:value={this.form.name} placeholder={''}></NInput>
          </NFormItem>
          <NFormItem path='username' label='描述'>
            <NInput v-model:value={this.form.descp} placeholder={''}></NInput>
          </NFormItem>
        </NForm>
      </Modal>
    )
  }
})
