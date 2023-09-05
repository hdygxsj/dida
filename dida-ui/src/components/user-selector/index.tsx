import { listUser } from '@/service/modules/user'
import { NSelect } from 'naive-ui'
import { PropType, defineComponent, reactive, toRefs, watch } from 'vue'

const props = {
  username: {
    type: String,
    default: ''
  }
}
export default defineComponent({
  props,
  emits: ['update:username'],
  setup(props, ctx) {
    const vars = reactive({
      select: '',
      options: []
    })
    const handleSearch = (username: any) => {
      let params = {
        username: username
      }
      listUser(params).then((res: any) => {
        vars.options = res?.map((e: any) => {
          return {
            label: e.username,
            value: e.username
          }
        })
      })
    }
    const handleSelect = (val: any) => {
      ctx.emit('update:username', val)
    }
    watch(
      () => props.username,
      (val) => {
        vars.select = props.username
      }
    )
    return {
      ...toRefs(vars),
      handleSearch,
      handleSelect
    }
  },
  render() {
    return (
      <NSelect
        v-model:value={this.select}
        remote
        filterable
        placeholder='输入用户名'
        options={this.options}
        onSearch={this.handleSearch}
        onUpdate:value={this.handleSelect}
      ></NSelect>
    )
  }
})
