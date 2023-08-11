
import Card from "@/components/card";
import { MenuOption, NButton, NEllipsis, NGrid, NGridItem, NMenu, NSpace } from "naive-ui";
import { defineComponent } from "vue";


const Switch = defineComponent({
    setup(props, ctx) {
        const options: MenuOption[] = [
            {
                label: 'group1',

                key: '1'
            },
            {
                label: 'group1',

                key: '2'
            },
            {
                label: 'group1',

                key: '3'
            },
            {
                label: 'group1',

                key: '4'
            },
            {
                label: 'group1',

                key: '5'
            },
            {
                label: 'group1',

                key: '6'
            },
            {
                label: 'group1',

                key: '7'
            }, {
                label: 'group1',

                key: '1'
            },
            {
                label: 'group1',

                key: '2'
            },
            {
                label: 'group1',

                key: '3'
            },
            {
                label: 'group1',

                key: '4'
            },
            {
                label: 'group1',

                key: '5'
            },
            {
                label: 'group1',

                key: '6'
            },
            {
                label: 'group1',

                key: '7'
            }, {
                label: 'group1',

                key: '1'
            },
            {
                label: 'group1',

                key: '2'
            },
            {
                label: 'group1',

                key: '3'
            },
            {
                label: 'group1',

                key: '4'
            },
            {
                label: 'group1',

                key: '5'
            },
            {
                label: 'group1',

                key: '6'
            },
            {
                label: 'group1',

                key: '7'
            },
            {
                label: 'group1',

                key: '1'
            },
            {
                label: 'group1',

                key: '2'
            },
            {
                label: 'group1',

                key: '3'
            },
            {
                label: 'group1',

                key: '4'
            },
            {
                label: 'group1',

                key: '5'
            },
            {
                label: 'group1',

                key: '6'
            },
            {
                label: 'group1',

                key: '7'
            }
        ]
        return {
            options
        }
    },
    render() {
        return (

            <NSpace >
                <NGrid cols={24} x-gap={12}>
                    <NGridItem span={4} >
                        <Card title="选择命名空间" >
                            <NButton style="width: 100%">管理命名空间</NButton>
                            <div style={{ height: '80%' }}>
                                <NMenu v-model:options={this.options} style="width: 15vw" default-value="1" /></div>
                        </Card></NGridItem>
                    <NGridItem span={20}>
                        <Card ><div style="width:75vw;min-height:85vh">a</div></Card>
                    </NGridItem>

                </NGrid>
            </NSpace>
        )
    }
})
export default Switch