import { PropType, defineComponent, reactive, toRefs } from 'vue'
import { useModal } from './use-modal'
import Modal from '@/components/modal'
import { NForm, NFormItem } from 'naive-ui'
import UserSelector from '@/components/user-selector'

const props = {
  show: {
    type: Boolean as PropType<boolean>,
    default: false
  }
}
export default defineComponent({
  name: 'AddModal',
  props,
  emits: ['update:show', 'cancel', 'confirm'],
  setup(props, ctx) {
    const vars = reactive({
      show: false,
      username: '',
      code: ''
    })
    const { handleOpen, handleClose, handleConfirm } = useModal(vars, ctx)
    return {
      ...toRefs(vars),
      handleOpen,
      handleClose,
      handleConfirm
    }
  },
  render() {
    return (
      <Modal
        v-model:show={this.show}
        title={`${this.code} 新增成员`}
        onCancel={this.handleClose}
        onConfirm={this.handleConfirm}
      >
        <NForm>
          {this.username}
          <NFormItem>
            <UserSelector v-model:username={this.username}></UserSelector>
          </NFormItem>
        </NForm>
      </Modal>
    )
  }
})
