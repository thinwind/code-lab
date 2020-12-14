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
import java.nio.DoubleBuffer;
import java.nio.LongBuffer;

/**
 *
 * TODO Byte2OthersTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-01  11:06
 *
 */
public class Byte2OthersTest {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(32);
        byteBuffer.putDouble(1.1D);
        byteBuffer.putDouble(1.2D);
        byteBuffer.putDouble(1.3D);
        byteBuffer.putDouble(1.4D);
        byteBuffer.flip();
        DoubleBuffer longBuffer = byteBuffer.asDoubleBuffer();
        for (int i = 0; i < longBuffer.capacity(); i++) {
            System.out.print(longBuffer.get()+" ");
        }
    }
}
