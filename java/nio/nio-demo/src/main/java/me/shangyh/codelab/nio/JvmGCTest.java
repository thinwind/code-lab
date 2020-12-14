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
package me.shangyh.codelab.nio;

import java.nio.ByteBuffer;

/**
 *
 * TODO HeapGCTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-04  17:39
 *
 */
public class JvmGCTest {
    public static void main(String[] args) throws InterruptedException {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
        byte[] filling = {1};
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            byteBuffer.put(filling);
        }

        System.out.println("put ended.");
        Thread.sleep(5000);
        System.out.println("after 5 secs");
        Thread.sleep(20000);
        System.out.println("exiting...");
    }
}
