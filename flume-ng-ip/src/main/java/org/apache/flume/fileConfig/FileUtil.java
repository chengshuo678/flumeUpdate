/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.flume.fileConfig;

import java.io.*;

/**
 * 获取配置文件中的数据
 */
public class FileUtil {

    private static final String UTF_8 = "UTF-8";

    private static final String Unicode = "Unicode";

    private static final String UTF_16BE = "UTF-16BE";

    private static final String ANSI_ASCII = "ANSI|ASCII";

    private static final String GBK = "GBK";


    /**
     * 自动根据文件编码格式读取文件
     *
     * @param filePath
     * @return
     * @throws Exception
     */
    public static BufferedReader readFile(String filePath) throws Exception {
        return new BufferedReader(new InputStreamReader(new FileInputStream(filePath), codeString(filePath)));
    }

    /**
     * 读取stream文件流
     * @param fileInputStream
     * @return
     */
    public static BufferedReader readInputStream(InputStream fileInputStream) {
        return new BufferedReader(new InputStreamReader(fileInputStream));
    }

    public static String codeString(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(
                new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        String code;
        //其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
        switch (p) {
            case 0xefbb:
                code = UTF_8;
                break;
            case 0xfffe:
                code = Unicode;
                break;
            case 0xfeff:
                code = UTF_16BE;
                break;
            case 0x5c75:
                code = ANSI_ASCII;
                break;
            default:
                code = GBK;
        }

        return code;
    }

}
