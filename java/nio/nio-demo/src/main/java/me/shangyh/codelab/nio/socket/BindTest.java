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
package me.shangyh.codelab.nio.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

/**
 *
 * TODO BindTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-01  16:17
 *
 */
public class BindTest {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress("172.23.32.1", 8099));
        System.out.println(server.getInetAddress().getHostName());
        System.out.println(server.getInetAddress().getHostAddress());
        System.out.println(server.getInetAddress().getLoopbackAddress());
    }
}
