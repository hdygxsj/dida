import Card from '@/components/card'
import { getGroup } from '@/service/modules/group'
import { NButton, NPageHeader, NSpace } from 'naive-ui'
import { defineComponent, reactive, toRefs } from 'vue'
import { useRouter } from 'vue-router'
import type { Router } from 'vue-router'

export default defineComponent({
  setup(props, ctx) {
    const router: Router = useRouter()
    const vars = reactive({
      code: String(router.currentRoute.value.params.code) || '',
      name: '',
      descp: ''
    })
    const init =()=>{
        getGroup(vars.code).then((res:any)=>{
            vars.name = res.name
            vars.descp = res.descp
        })
    }
    init()
    const onBack = () => {
      router.go(-1)
    }
    return {
      ...toRefs(vars),
      onBack
    }
  },
  render() {
    return (
      <NSpace vertical>
       
          <NSpace vertical>
            <NPageHeader
              title={this.code}
              onBack={this.onBack}
              subtitle={this.name}
            >
              {/* {this.descp} */}
            </NPageHeader>
          </NSpace>
      
        <Card>
          <NSpace justify='space-between'>
            <NSpace>
              <NButton type='primary'>新增</NButton>
            </NSpace>
            <NSpace></NSpace>
          </NSpace>
        </Card>
        <Card>s</Card>
      </NSpace>
    )
  }
})
