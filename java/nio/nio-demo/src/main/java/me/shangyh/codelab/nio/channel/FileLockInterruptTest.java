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
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 *
 * TODO FileLockInterruptTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-05  16:14
 *
 */
public class FileLockInterruptTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile raf = new RandomAccessFile("c:\\tmp\\a.txt", "rw");
        FileChannel channel = raf.getChannel();
        
        Thread t=new Thread(){
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 30; i++) {
                        System.out.println("i="+i);
                    }
                    System.out.println("start to lock:");
                    channel.lock(0, 10, false); 
                } catch (Exception e) {
                    //TODO: handle exception
                    e.printStackTrace();
                }
            }
        };
        
        t.start();
        Thread.sleep(10);
        t.interrupt();
        System.out.println("interrupted");
        Thread.sleep(5000);
        channel.close();
        raf.close();
    }
}
