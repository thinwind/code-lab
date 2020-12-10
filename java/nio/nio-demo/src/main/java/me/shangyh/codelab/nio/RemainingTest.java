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
 * TODO RemainingTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-04  13:26
 *
 */
public class RemainingTest {
    public static void main(String[] args) {
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        System.out.println("after init: offset="+byteBuffer.arrayOffset());
        int remaining = byteBuffer.remaining();

        for (int i = 0; i < remaining; i++) {
            System.out.print(byteBuffer.get());
        }
        System.out.println("after all read: offset="+byteBuffer.arrayOffset());
        System.out.println("\nclear:");
        
        System.out.println("after clear: offset="+byteBuffer.arrayOffset());

        byteBuffer.clear();
        
        for(;byteBuffer.hasRemaining();){
            System.out.print(byteBuffer.get());
        }
        System.out.println("\n");

        System.out.println("after all read: offset="+byteBuffer.arrayOffset());
    }
}
