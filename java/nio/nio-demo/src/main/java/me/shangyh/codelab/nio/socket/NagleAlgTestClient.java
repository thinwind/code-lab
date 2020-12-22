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
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * TODO NagleAlgTestClient说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  08:48
 *
 */
public class NagleAlgTestClient {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("localhost", 9099);
        socket.setTcpNoDelay(false);

        InputStream inputStream = socket.getInputStream();
        long start = System.currentTimeMillis();
        byte[] byteArray = new byte[1];
        int readLength = inputStream.read(byteArray);
        while(readLength != -1){
            String newStr = new String(byteArray,0,readLength);
            System.out.println(newStr);
            readLength = inputStream.read(byteArray);
        }
        
        long end = System.currentTimeMillis();
        System.out.println(end-start);
        socket.close();
    }
}
