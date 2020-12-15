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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * TODO FileChannelTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  19:13
 *
 */
public class FileChannelTest {
    public static void main(String[] args) throws IOException {
        FileOutputStream fos = new FileOutputStream("c:\\tmp\\a.txt",true);
        FileChannel fileChannel = fos.getChannel();
        ByteBuffer buffer1 = ByteBuffer.wrap("abcde".getBytes("UTF-8"));
        ByteBuffer buffer2 = ByteBuffer.wrap("01234".getBytes("UTF-8"));
        fileChannel.write(buffer1);
        fileChannel.position(2);
        System.out.println("filechannel pos="+fileChannel.position());
        buffer2.position(1);
        buffer2.limit(3);
        fileChannel.write(buffer2);
        fileChannel.close();
        fos.close();
    }
}
