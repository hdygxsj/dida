import { NButton, NTooltip } from 'naive-ui'
import { PropType, defineComponent, h, renderSlot } from 'vue'

export default defineComponent({
  name: 'TooltipButton',
  emits: ['click'],
  props: {
    disables: {
      type: Boolean,
      default: false
    },
    text: {
      type: String
    },
    type: {
      type: String,
      default: 'info'
    }
  },
  setup(props, ctx) {
    const handleClick = () => {
      ctx.emit('click')
    }
    return {
      handleClick
    }
  },
  render(props: any) {
    const { $slots } = this
    return h(
      NTooltip,
      {},
      {
        trigger: () =>
          h(
            NButton,
            {
              circle: true,
              type: props.type,
              size: 'small',
              disabled: props.disabled,
              onClick: this.handleClick
            },
            {
              icon: () => renderSlot($slots, 'icon')
            }
          ),
        default: () => props.text
      }
    )
  }
})
