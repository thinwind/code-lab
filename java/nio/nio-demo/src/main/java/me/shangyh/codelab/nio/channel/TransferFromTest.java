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
package me.shangyh.codelab.nio.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 *
 * TODO TransferFromTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  18:22
 *
 */
public class TransferFromTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFile raf1=new RandomAccessFile("c:\\tmp\\a.txt","rw");
        RandomAccessFile raf2=new RandomAccessFile("c:\\tmp\\b.txt","rw");
        FileChannel channel1=raf1.getChannel();
        FileChannel channel2=raf2.getChannel();
        
        channel2.position(3);
        long length = channel1.transferFrom(channel2, 6, 100);
        channel1.close();
        channel2.close();
        
        raf1.close();
        raf2.close();
        
        System.out.println(length);
    }
}
