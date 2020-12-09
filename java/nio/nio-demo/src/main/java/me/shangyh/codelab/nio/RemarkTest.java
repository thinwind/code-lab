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

import java.nio.CharBuffer;

/**
 *
 * TODO RemarkTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  08:57
 *
 */
public class RemarkTest {
    public static void main(String[] args) {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println("buffer capacity:" + charBuffer.capacity() + "\tbuffer position:"
                + charBuffer.position());
        System.out.println();
        charBuffer.position(2);
        charBuffer.mark();
        System.out.println("after position modified:");
        System.out.println("buffer capacity:" + charBuffer.capacity() + "\tbuffer position:"
                + charBuffer.position());
                
        charBuffer.put('0');
        System.out.println("after put");
        System.out.println("buffer capacity:" + charBuffer.capacity() + "\tbuffer position:"
        + charBuffer.position());
        
        System.out.println("\nafter reset:");
        charBuffer.reset();
        System.out.println("buffer capacity:" + charBuffer.capacity() + "\tbuffer position:"
        + charBuffer.position());
        
        for (int i = 0; i < chars.length; i++) {
            System.out.println(i+"\t"+chars[i]);
        }
    }
}
