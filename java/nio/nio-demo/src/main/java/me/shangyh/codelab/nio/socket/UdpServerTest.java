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
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 *
 * TODO UdpServerTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  14:06
 *
 */
public class UdpServerTest {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket(8088);
        byte[] byteArray = new byte[12];
        DatagramPacket myPacket = new DatagramPacket(byteArray, 10);
        socket.receive(myPacket);
        socket.close();
        System.out.println("data length:"+myPacket.getLength());
        System.out.println(new String(myPacket.getData(),0,myPacket.getLength()));
    }
}
