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
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * TODO ClientTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-01  11:31
 *
 */
public class ClientTest {
    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket("localhost", 18088);

        OutputStream outputStream = socket.getOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(outputStream);

        String strA = "服务端你好 A\n";
        String strB = "服务端你好 B\n";
        String strC = "服务端你好 C\n";

        int allStrLength = (strA + strB + strC).getBytes("UTF-8").length;
        output.writeInt(allStrLength);
        output.flush();
        output.write(strA.getBytes("UTF-8"));
        output.write(strB.getBytes("UTF-8"));
        output.write(strC.getBytes("UTF-8"));
        output.flush();

        InputStream inputStream = socket.getInputStream();
        ObjectInputStream input = new ObjectInputStream(inputStream);
        // 1st
        int byteLength = input.readInt();
        byte[] byteArray = new byte[byteLength];
        input.readFully(byteArray);
        String result = new String(byteArray, "UTF-8");
        System.out.println("1st String from Server:" + result);
        
        // 2nd
        strA = "服务端你好 D\n";
        strB = "服务端你好 E\n";
        strC = "服务端你好 F\n";

        allStrLength = (strA + strB + strC).getBytes("UTF-8").length;
        output.writeInt(allStrLength);
        output.flush();
        output.write(strA.getBytes("UTF-8"));
        output.write(strB.getBytes("UTF-8"));
        output.write(strC.getBytes("UTF-8"));
        output.flush();

        byteLength = input.readInt();
        byteArray = new byte[byteLength];
        input.readFully(byteArray);
        result = new String(byteArray, "UTF-8");
        System.out.println("2nd String from Server:" + result);
        
        input.close();
        output.close();
        socket.close();
    }
}
