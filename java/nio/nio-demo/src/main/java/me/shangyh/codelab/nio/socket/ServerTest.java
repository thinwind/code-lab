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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * TODO ServerTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-01  11:11
 *
 */
public class ServerTest {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(18088);
        System.out.println("server listening...");
        Socket socket = server.accept();
        System.out.println("client accepted");
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream input = new ObjectInputStream(inputStream);

        // 1st
        int byteLength = input.readInt();
        byte[] byteArray = new byte[byteLength];
        input.readFully(byteArray);
        String result = new String(byteArray, "UTF-8");
        System.out.println("1st String from Client:" + result);

        OutputStream outputStream = socket.getOutputStream();
        String strA = "客户端你好 A\n";
        String strB = "客户端你好 B\n";
        String strC = "客户端你好 C\n";

        int allStrLength = (strA + strB + strC).getBytes("UTF-8").length;
        ObjectOutputStream output = new ObjectOutputStream(outputStream);
        output.writeInt(allStrLength);
        output.flush();
        output.write(strA.getBytes("UTF-8"));
        output.write(strB.getBytes("UTF-8"));
        output.write(strC.getBytes("UTF-8"));
        output.flush();

        // 2nd
        byteLength = input.readInt();
        byteArray = new byte[byteLength];
        input.readFully(byteArray);
        result = new String(byteArray, "UTF-8");
        System.out.println("2nd String from Client:" + result);
        
        strA = "客户端你好 D\n";
        strB = "客户端你好 E\n";
        strC = "客户端你好 F\n";

        allStrLength = (strA + strB + strC).getBytes("UTF-8").length;
        output.writeInt(allStrLength);
        output.flush();
        output.write(strA.getBytes("UTF-8"));
        output.write(strB.getBytes("UTF-8"));
        output.write(strC.getBytes("UTF-8"));
        output.flush();
        
        input.close();
        output.close();
        
        socket.close();
        server.close();
    }
}
