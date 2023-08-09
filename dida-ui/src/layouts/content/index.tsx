import { NLayout, NLayoutContent, NLayoutHeader, useMessage } from "naive-ui";
import { defineComponent, onMounted, toRefs } from "vue";
import { useRoute } from "vue-router";
import Navbar from "./components/navbar";
import { useDataList } from "./use-dataList";


const Content = defineComponent({
    name: 'Content',
    setup(props, ctx) {
        const route = useRoute()
        window.$message = useMessage()
        const {
            state,
            changeMenuOption,
        } = useDataList()
        onMounted(() => {
            changeMenuOption(state)
        })
        return {
            ...toRefs(state),
        }
    },
    render() {
        return (
            <NLayout position='absolute' style={{ height: '100%' }}>
                <NLayoutHeader style={{ height: '65px' }}>
                    <Navbar headerMenuOptions={this.menuOptions}></Navbar>

                </NLayoutHeader>
                <NLayout position='absolute' style='top:65px'>
                    <NLayoutContent native-scrollbar={false}
                        style='padding: 16px 22px'
                        contentStyle={'height: 100%'}>
                        {/* das */}
                        <router-view key={this.$route.fullPath} />
                    </NLayoutContent>
                </NLayout>
            </NLayout>

        )
    }
})
export default Content;