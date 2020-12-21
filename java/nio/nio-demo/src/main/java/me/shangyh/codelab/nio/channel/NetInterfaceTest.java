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
package me.shangyh.codelab.nio.channel;

import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 *
 * TODO NetInterfaceTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-00  16:38
 *
 */
public class NetInterfaceTest {
    public static void main(String[] args) throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface=networkInterfaces.nextElement();
            System.out.println("> getName:"+networkInterface.getName());
            System.out.println("> getDisplayName:"+networkInterface.getDisplayName());
            System.out.println("> index:"+networkInterface.getIndex());
            System.out.println("> isUp:"+networkInterface.isUp());
            System.out.println("> isLoopback:"+networkInterface.isLoopback());
            System.out.println();
        }
    }
}
