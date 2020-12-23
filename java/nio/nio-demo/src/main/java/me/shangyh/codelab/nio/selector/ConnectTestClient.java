/*
 * Copyright 2020 Shang Yehua
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package me.shangyh.codelab.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 *
 * TODO ConnectTestClient说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  11:10
 *
 */
public class ConnectTestClient {
    public static void main(String[] args) {
        boolean result = false;
        long start = 0;
        long end = 0;
        try {
            SocketChannel channel = SocketChannel.open();
            channel.configureBlocking(false);
            start = System.currentTimeMillis();
            result = channel.connect(new InetSocketAddress("localhost", 9099));
            end = System.currentTimeMillis();
            System.out.println("正常，连接耗时：" + (end - start) + "\t连接结果：" + result);
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
            end = System.currentTimeMillis();
            System.out.println("异常，连接耗时：" + (end - start) + "\t连接结果：" + result);
        }
    }
}
