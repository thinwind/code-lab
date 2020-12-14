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
package me.shangyh.codelab.nio;

import java.nio.ByteBuffer;

/**
 *
 * TODO DirectVsHeap说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-05  11:03
 *
 */
public class DirectVsHeap {
    public static void main(String[] args) {
        heapOps();
        directOps();
    }

    static void directOps() {
        long start = System.nanoTime();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE-2);
        for (int i = 0; i < Integer.MAX_VALUE-2; i++) {
            byteBuffer.put((byte) 123);
        }
        long end = System.nanoTime();
        System.out.println("Direct consumer:"+((end-start)/(Math.pow(10, 8))));
    }
    
    static void heapOps() {
        long start = System.nanoTime();
        ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.MAX_VALUE-2);
        for (int i = 0; i < Integer.MAX_VALUE-2; i++) {
            byteBuffer.put((byte) 1);
        }
        long end = System.nanoTime();
        System.out.println("Heap consumer:"+((end-start)/(Math.pow(10, 8))));
    }
}
