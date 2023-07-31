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

import cn.hutool.crypto.SmUtil;
import com.hdygxsj.dida.exceptions.Assert;
import com.hdygxsj.dida.security.Sm4;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

@Slf4j
public class SmUtilTest {

    @Test
    public void sm3() {
        String str = "aaaaa";
        log.info(SmUtil.sm3(str));
    }

    private static final byte[] testPrivateKey = "567502e0e087c22f".getBytes(StandardCharsets.UTF_8);

    @Test
    public void sm4() {
        String str = "admin";
        String encryptHex = Sm4.execute(str, Sm4.ENCRYPT);
        log.info(encryptHex);
        String decryptStr = Sm4.execute(encryptHex, Sm4.DECRYPT);
        log.info(decryptStr);
        Assert.isTrue(str.equals(decryptStr), "加密错误");
    }

}
