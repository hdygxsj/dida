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

package com.hdygxsj.dida.exceptions;

import cn.hutool.core.util.StrUtil;

public class Assert {

    private Assert() {
    }

    public static void isTrue(boolean exp, RuntimeException exception) {
        if (!exp) {
            throw exception;
        }
    }

    public static void isTrue(boolean exp, String msg) {
        if (!exp) {
            throw new DidaRuntimeException(msg);
        }
    }

    public static void notNull(Object obj, String msg) {
        if (obj == null) {
            throw new DidaRuntimeException(msg);
        }
    }
    public static void notNull(Object obj, RuntimeException exception) {
        if (obj == null) {
            throw exception;
        }
    }

    public static void notBlank(String str, String message) {
        if(StrUtil.isBlank(str)){
            throw new DidaRuntimeException(message);
        }
    }
}
