export const useForm = () => {
  const rules = {
    code: {
      required: true,
      message: '请输入姓名',
      trigger: 'blur'
    },
    name: {
      required: true,
      message: '请输入名称',
      trigger: ['input', 'blur']
    }
  }
  
  return {
    rules
  }
}
