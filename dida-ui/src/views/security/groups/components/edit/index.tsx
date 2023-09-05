import Modal from '@/components/modal'
import { PropType, defineComponent, reactive, ref, toRefs } from 'vue'
import { useModal } from './use-modal'

import { NForm, NFormItem, NInput } from 'naive-ui'
import { useForm } from './use-form'

const props = {
  show: {
    type: Boolean as PropType<boolean>,
    default: false
  }
}
export default defineComponent({
  props,
  emits: ['update:show', 'cancel', 'confirm'],
  setup(props, ctx) {
    const vars = reactive({
      show: false,
      mode: 'add',
      form: {
        code: '',
        name: '',
        descp: ''
      },
      formRef: ref()
    })
    const {
      handleAddOpen,
      handleClose,
      handleEditOpen,
      getTitle,
      handleConfirm
    } = useModal(vars, ctx)
    const { rules } = useForm()
    return {
      ...toRefs(vars),
      handleAddOpen,
      handleClose,
      handleEditOpen,
      getTitle,
      rules,
      handleConfirm
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
        <NForm model={this.form} ref='formRef' rules={this.rules}>
          <NFormItem path='code' label='简称'>
            <NInput v-model:value={this.form.code} placeholder={''}></NInput>
          </NFormItem>
          <NFormItem path='name' label='全称'>
            <NInput v-model:value={this.form.name} placeholder={''}></NInput>
          </NFormItem>
          <NFormItem path='descp' label='描述'>
            <NInput v-model:value={this.form.descp} placeholder={''}></NInput>
          </NFormItem>
        </NForm>
      </Modal>
    )
  }
})
