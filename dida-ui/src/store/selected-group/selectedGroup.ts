import { defineStore } from 'pinia'
export const useSelectedGroup = defineStore({
  id: 'selected-group',
  state: (): any => ({
    code: ''
  }),
  persist: true,
  getters: {
    getCode(): string {
      return this.code
    }
  },
  actions: {
    setCode(code: string): void {
      this.code = code
    }
  }
})
