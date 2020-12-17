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

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * TODO FileChannelReadPosTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  14:18
 *
 */
public class FileChannelReadPosTest {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream("c:\\tmp\\c.txt");
        FileChannel channel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(3);
        long length = channel.read(byteBuffer, 1);
        System.out.println(length);
        
        byteBuffer.clear();
        length = channel.read(byteBuffer, 5);
        System.out.println(length);
        
        channel.close();
        fis.close();
    }
}
