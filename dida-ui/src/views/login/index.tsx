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
import { defineComponent, ref, provide, nextTick, toRefs, h } from 'vue'

import { NButton, NInput, NSpace, useMessage, NImage } from 'naive-ui'
import './index.css'

import bgImg from './assets/image/bg.png'
import { useForm } from './use-form'
import { GithubOutlined } from '@vicons/antd'
import { OAuth2Provider } from '@/service/modules/login/types'
const Login = defineComponent({
  name: 'Login',
  components: {
    GithubOutlined
  },
  setup() {
    window.$message = useMessage()
    const isRouterAlive = ref(true)
    const reload = () => {
      isRouterAlive.value = false
      nextTick(() => {
        isRouterAlive.value = true
      })
    }
    const {
      variables,
      handleLogin,
      gotoOAuth2Page,
      handleLoginByToken,
      handleGetOAuth2Provider,
      oauth2Providers
    } = useForm()
    provide('reload', reload)
    handleGetOAuth2Provider()
    handleLoginByToken()
    return {
      ...toRefs(variables),
      handleLogin,
      reload,
      isRouterAlive,
      gotoOAuth2Page,
      oauth2Providers
    }
  },
  render() {
    return (
      <div class='bg'>
        <img
          src={bgImg}
          style='width:100%;height:100%;overflow-y:hidden'
          draggable='false'
        ></img>
        <div class='loginForm'>
          <div class='title-welcome'>Welcome to Dida</div>
          <div class='title-name'> 分布式开关平台 </div>
          <div>
            <NInput
              class='login-input'
              placeholder='请输入你的登陆账号'
              v-model:value={this.loginForm.username}
            ></NInput>
          </div>
          <div>
            <NInput
              class='login-input'
              placeholder='请输入你的登陆密码'
              type='password'
              v-model:value={this.loginForm.password}
            ></NInput>
          </div>
          <div>
            <button class='login-bt' onClick={this.handleLogin}>
              登&nbsp;录
            </button>
          </div>
          <NSpace class='login-oauth2' justify='center'>
            {this.oauth2Providers?.map((e: OAuth2Provider) => {
              return e.iconUri ? (
                <div onClick={() => this.gotoOAuth2Page(e)}>
                  <NImage preview-disabled width='30' src={e.iconUri}></NImage>{' '}
                </div>
              ) : (
                <NButton
                  quaternary
                  color='#fff'
                  onClick={() => this.gotoOAuth2Page(e)}
                >
                  {e.provider}
                </NButton>
              )
            })}
          </NSpace>
        </div>
        <NSpace size='large' class='head'>
          {/* <div class="head-text">sda</div> */}
          <NButton class='login-regist'>注册</NButton>
        </NSpace>
      </div>
    )
  }
})

export default Login
