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

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;

import java.util.Collections;

public class MybatisGenerator {

    public static void main(String[] args) {
        String password = System.getenv("PASSWORD");
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/dida","root",password)
                .globalConfig(builder -> {
                    builder.author("hdygxsj")
                            .enableSpringdoc()
                            .outputDir("/Users/hdygxsj/IdeaProjects/dida/dida-api/src/main/java");
                })
                .packageConfig(builder -> {
                    builder.parent("com.hdygxsj.dida.api")
                            .pathInfo(Collections.singletonMap(OutputFile.xml,"/Users/hdygxsj/IdeaProjects/dida/dida-api/src/main/resources/mapper"))
                            .entity("domain.entity");

                })
                .strategyConfig(builder ->
                        builder.addTablePrefix("dida_")
                                .mapperBuilder().enableFileOverride()
                                .entityBuilder().enableLombok().formatFileName("%sDO")
                                .serviceBuilder())
                .templateConfig(builder->{
                    builder.service("")
                    .serviceImpl("")
                    .controller("");
                })
                .execute();

    }
}
