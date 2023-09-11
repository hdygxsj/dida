import Modal from '@/components/modal'
import {
  NButton,
  NDynamicInput,
  NForm,
  NFormItem,
  NInput,
  NModal,
  NRadio,
  NRadioGroup,
  NSelect,
  NSpace,
  NSwitch,
  useMessage
} from 'naive-ui'
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
        switchKey: '',
        type: {
          name: '',
          descp: '',
          switchType: 0,
          defaultValue: '',
          options: reactive<Array<any>>([])
        }
      },
      formRef: ref()
    })
    const handleDeleteOption = (e: any) => {
      vars.form.type.options.splice(vars.form.type.options.indexOf(e), 1)
    }
    const handleAddOption = () => {
      vars.form.type.options.push({
        label: '',
        value: ''
      })
    }
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
      getTitle,
      handleAddOption,
      handleDeleteOption
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
          <NFormItem path='switchKey' label='编码'>
            <NInput
              v-model:value={this.form.switchKey}
              placeholder={''}
            ></NInput>
          </NFormItem>
          <NFormItem path='type.name' label='名称'>
            <NInput
              v-model:value={this.form.type.name}
              placeholder={''}
            ></NInput>
          </NFormItem>
          <NFormItem path='type.descp' label='描述'>
            <NInput
              v-model:value={this.form.type.descp}
              placeholder={''}
            ></NInput>
          </NFormItem>
          <NFormItem path='type.switchType' label='开关类型'>
            <NRadioGroup v-model:value={this.form.type.switchType}>
              <NRadio key={0} value={0}>
                普通开关
              </NRadio>
              <NRadio key={1} value={1}>
                多选开关
              </NRadio>
            </NRadioGroup>
          </NFormItem>
          {this.form.type.switchType == 1 && (
            <NFormItem path='type.options' label='选项'>
              <NDynamicInput
                v-model:value={this.form.type.options}
                preset='pair'
                keyPlaceholder='选项名'
                valuePlaceholder='选项值-唯一'
              ></NDynamicInput>
            </NFormItem>
          )}
          <NFormItem path='type.default' label='默认值'>
            {this.form.type.switchType == 0 && (
              <NSwitch v-model:value={this.form.type.defaultValue}></NSwitch>
            )}
            {this.form.type.switchType == 1 && (
              <NSelect
                v-model:value={this.form.type.defaultValue}
                options={this.form.type.options?.map((e) => {
                  return { label: e.key, value: e.value }
                })}
              ></NSelect>
            )}
          </NFormItem>
        </NForm>
      </Modal>
    )
  }
})
