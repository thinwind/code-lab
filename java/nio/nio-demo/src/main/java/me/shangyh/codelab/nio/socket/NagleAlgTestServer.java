/* 
 * Copyright 2020 Shang Yehua
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.shangyh.codelab.nio.socket;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * TODO NagleAlgTestServer说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  08:43
 *
 */
public class NagleAlgTestServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(9099);
        Socket socket=serverSocket.accept();
        System.out.println("Default nodelay:"+socket.getTcpNoDelay());
        socket.setTcpNoDelay(true);
        System.out.println("After set nodelay:"+socket.getTcpNoDelay());
        OutputStream outputStream=socket.getOutputStream();
        for(int i=0;i<1000;i++){
            outputStream.write("1".getBytes("utf-8"));
        }
        
        socket.close();
        serverSocket.close();
    }
}
