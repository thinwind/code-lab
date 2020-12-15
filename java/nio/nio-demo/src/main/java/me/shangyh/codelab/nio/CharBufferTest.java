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

import java.io.IOException;
import java.nio.CharBuffer;

/**
 *
 * TODO CharBufferTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  11:03
 *
 */
public class CharBufferTest {
    public static void main(String[] args) throws IOException {
        CharBuffer charBuffer = CharBuffer.allocate(8);
        charBuffer.append("ab123456");
        charBuffer.position(2);
        charBuffer.put("cde");
        charBuffer.rewind();
        for (int i = 0; i < charBuffer.limit(); i++) {
            System.out.print(charBuffer.get());
        }

        System.out.println();
        charBuffer.position(2);
        CharBuffer charBuffer2 = CharBuffer.allocate(4);
        charBuffer.read(charBuffer2);
        System.out.println("after read:");
        System.out.println("charBuffer2 position=" + charBuffer2.position());
        charBuffer2.rewind();

        for (int i = 0; i < charBuffer2.limit(); i++) {
            System.out.print(charBuffer2.get());
        }

        System.out.println();

        charBuffer.position(1);
        CharBuffer charBuffer3 = charBuffer.subSequence(0, 2);
        System.out.println("charBuffer3 position=" + charBuffer3.position() + "\tlimit="
                + charBuffer3.limit() + "\tcapacity=" + charBuffer3.capacity());
        
        for (int i = charBuffer3.position(); i < charBuffer3.limit(); i++) {
            System.out.print(charBuffer3.get());
        }
        
        System.out.println();
        System.out.println(charBuffer.array()==charBuffer2.array());
        System.out.println(charBuffer.array()==charBuffer3.array());
    }
}
