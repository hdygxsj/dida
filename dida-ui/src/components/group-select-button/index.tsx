import {
  NButton,
  NConfigProvider,
  NMessageProvider,
  NModal,
  NSelect,
  useMessage
} from 'naive-ui'
import { defineComponent, reactive, toRefs } from 'vue'
import Modal from '../modal'
import { listMyGroup } from '@/service/modules/group'

export default defineComponent({
  emits: ['select'],
  props: {
    initCode: {
      default: ''
    }
  },
  setup(props, ctx) {
    window.$message = useMessage()
    const emptyArray: Array<any> = []
    const vars = reactive({
      code: 'null',
      name: '请选择用户组',
      show: false,
      search: null,
      options: emptyArray,
      selectedGroup: ''
    })
    const handleConfirm = () => {
      debugger
      let option = vars.options.filter((e) => e.code == vars.selectedGroup)[0]
      vars.code = option.code
      vars.name = option.name
      handleClose()
      ctx.emit('select', option.code)
    }
    const handleClose = () => {
      vars.show = false
    }
    const handleGetGroups = (callback: Function | null) => {
      let params = {
        search: vars.search
      }
      listMyGroup(params).then((res: any) => {
        vars.options = res?.map((e: any) => {
          return {
            code: e.code,
            name: e.name,
            value: e.code,
            label: `${e.name}-${e.code}`
          }
        })
        if (callback) {
          callback()
        }
      })
    }
    const initCurGroup = () => {
      debugger
      if (vars.options.length > 0) {
        if (props.initCode) {
          let option = vars.options.filter((e) => e.code == props.initCode)[0]
          if (option) {
            vars.code = option.code
            vars.name = option.name
            ctx.emit('select', vars.code)
            return
          }
        }
        vars.code = vars.options[0].code
        vars.name = vars.options[0].name
        ctx.emit('select', vars.code)
        // vars.selectGroup = vars.code
      } else {
        window.$message.warning('当前用户没有用户组，许多操作无法执行')
        ctx.emit('select', vars.code)
      }
    }
    handleGetGroups(initCurGroup)
    const handOpen = () => {
      vars.selectedGroup = vars.code
      handleGetGroups(null)
      vars.show = true
    }
    return {
      ...toRefs(vars),
      handleClose,
      handleConfirm,
      handOpen
    }
  },
  render() {
    return (
      <NMessageProvider>
        <Modal
          v-model:show={this.show}
          title='选择用户组'
          onCancel={this.handleClose}
          onConfirm={this.handleConfirm}
        >
          <NSelect
            v-model:value={this.selectedGroup}
            options={this.options}
          ></NSelect>
        </Modal>
        <NButton
          quaternary
          onClick={this.handOpen}
        >{`${this.name}-${this.code}`}</NButton>
      </NMessageProvider>
    )
  }
})
