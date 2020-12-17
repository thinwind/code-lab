package me.shangyh.codelab.nio.channel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

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
/**
 *
 * TODO FileLock说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-04  17:34
 *
 */
public class FileLockTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        RandomAccessFile raf = new RandomAccessFile("c:\\tmp\\a.txt", "rw");
        FileChannel channel = raf.getChannel();
        
        System.out.println("A start to lock...");
        channel.lock(1,2,false);
        System.out.println("A lock success");
        Thread.sleep(Integer.MAX_VALUE);
        
        channel.close();
        raf.close();
    }
}
