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
 * TODO FileChannelReadTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  19:51
 *
 */
public class FileChannelReadTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        FileInputStream fis = new FileInputStream("c:\\tmp\\b.txt");
        FileChannel channel = fis.getChannel();
        for (int i = 0; i < 3; i++) {
            new Thread() {

                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(3);
                    try {
                        int length = channel.read(buffer);
                        while (length != -1) {
                            var array = buffer.array();
                            System.out.println(new String(array, "utf-8"));
                            buffer.clear();
                            length = channel.read(buffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }.start();
            
            new Thread() {

                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(3);
                    try {
                        int length = channel.read(buffer);
                        while (length != -1) {
                            var array = buffer.array();
                            System.out.println(new String(array, "utf-8"));
                            buffer.clear();
                            length = channel.read(buffer);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }.start();
        }
        Thread.sleep(3000);
        channel.close();
        fis.close();
    }
}
