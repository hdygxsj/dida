/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { defineComponent, ref, provide, nextTick, computed } from 'vue'

import {
  darkTheme,
  GlobalThemeOverrides,
  NConfigProvider,
  NDialogProvider,
  NMessageProvider,
  useDialog,
  zhCN
} from 'naive-ui'
import { useThemeStore } from '@/store/theme/theme'
import themeList from '@/themes'

const App = defineComponent({
  name: 'App',
  components: {
    NConfigProvider,
    NMessageProvider,
    NDialogProvider
  },
  setup() {
    // window.$dialog = useDialog()
    const isRouterAlive = ref(true)
    const themeStore = useThemeStore()
    const reload = () => {
      isRouterAlive.value = false
      nextTick(() => {
        isRouterAlive.value = true
      })
    }
    const currentTheme = computed(() =>
      themeStore.darkTheme ? darkTheme : undefined
    )

    provide('reload', reload)
    return {
      reload,
      isRouterAlive,
      currentTheme
    }
  },
  render() {
    const themeOverrides: GlobalThemeOverrides =
      themeList[this.currentTheme ? 'dark' : 'light']
    return (
      <NConfigProvider
        locale={zhCN}
        theme={this.currentTheme}
        theme-overrides={themeOverrides}
        style={{ width: '100%', height: '100%' }}
      >
        <NMessageProvider>
          <NDialogProvider>
            {this.isRouterAlive ? <router-view /> : ''}
          </NDialogProvider>
        </NMessageProvider>
      </NConfigProvider>
      // <div>?</div>
    )
  }
})

export default App
