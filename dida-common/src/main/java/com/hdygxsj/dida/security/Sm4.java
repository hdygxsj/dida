/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hdygxsj.dida.security;

import cn.hutool.crypto.symmetric.SM4;
import com.hdygxsj.dida.exceptions.DidaRuntimeException;

import java.nio.charset.StandardCharsets;

public class Sm4 {


    private static final SM4 sm4 = new SM4("567502e0e087c22f".getBytes(StandardCharsets.UTF_8));

    /**
     * 加密
     */
    public static final int ENCRYPT = 0;

    /**
     * 解密
     */
    public static final int  DECRYPT = 1;

    public static String execute(String text,int mode){
        if(mode == 0){
            return sm4.encryptHex(text);
        }else if(mode == 1){
            return sm4.decryptStr(text,StandardCharsets.UTF_8);
        }
        throw new DidaRuntimeException("不支持的密码学操作");
    }

}
