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

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 *
 * TODO BufferTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-01  17:57
 *
 */
public class BufferTest {
    public static void main(String[] args) {
        
        byte[] bytes = {1, 2, 3};
        short[] shorts = {1, 2, 3, 4};
        int[] ints = {1, 2, 3, 4, 5};
        float[] floats = {1, 2, 3, 4, 5, 6};
        long[] longs = {1, 2, 3, 4, 5, 6, 7};
        double[] doubles = {1, 2, 3, 4, 5, 6, 7, 8};
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};

        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        ShortBuffer shortBuffer = ShortBuffer.wrap(shorts);
        IntBuffer intBuffer = IntBuffer.wrap(ints);
        FloatBuffer floatBuffer = FloatBuffer.wrap(floats);
        LongBuffer longBuffer = LongBuffer.wrap(longs);
        DoubleBuffer doubleBuffer = DoubleBuffer.wrap(doubles);
        CharBuffer charBuffer = CharBuffer.wrap(chars);

        printClsName(byteBuffer);
        printClsName(shortBuffer);
        printClsName(intBuffer);
        printClsName(floatBuffer);
        printClsName(longBuffer);
        printClsName(doubleBuffer);
        printClsName(charBuffer);

        System.out.println();

        printCapacity("byteBuffer", byteBuffer);
        printCapacity("shortBuffer", shortBuffer);
        printCapacity("intBuffer", intBuffer);
        printCapacity("floatBuffer", floatBuffer);
        printCapacity("longBuffer", longBuffer);
        printCapacity("doubleBuffer", doubleBuffer);
        printCapacity("charBuffer", charBuffer);
        
    }

    static void printClsName(Object obj) {
        System.out.println(obj.getClass().getName());
    }
    
    static void printCapacity(String prefix,Buffer buffer){
        System.out.println(prefix+".capacity="+buffer.capacity());
    }
    
}
