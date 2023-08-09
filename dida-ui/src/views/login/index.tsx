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
import { defineComponent, ref, provide, nextTick } from 'vue'

import {
    NButton,
    NConfigProvider, NMessageProvider, NSpace
} from 'naive-ui'
import './index.css'

import bgImg from './assets/image/bg.png';
const Login = defineComponent({
    name: 'Login',
    setup() {
        const isRouterAlive = ref(true)
        const reload = () => {
            isRouterAlive.value = false
            nextTick(() => {
                isRouterAlive.value = true
            })
        }
        provide('reload', reload)
        return {
            reload,
            isRouterAlive,

        }
    },
    render() {
        return (
            <div class="bg">
                <img src={bgImg} style="width:100%;height:100%;overflow-y:hidden" draggable="false"></img>
                <div class="loginForm">
                    <div class="title-welcome">Welcome to Dida</div>
                    <div class="title-name"> 分布式开关平台 </div>
                    <div><input class="login-input" placeholder='请输入你的登陆账号' autocomplete="off"></input></div>
                    <div><input class="login-input" placeholder='请输入你的登陆密码' type="password" autocomplete="off"></input></div>
                    <div><button class="login-bt">登&nbsp;录</button></div>
                    {/* <div>@copyright</div> */}

                </div>
                <div class="head">
                    <NSpace size="large">
                        {/* <div class="head-text">sda</div> */}
                        <NButton class="login-regist">注册</NButton>
                    </NSpace>
                </div>
            </div>


        )
    }

})

export default Login