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

import java.nio.CharBuffer;

/**
 *
 * TODO LimitTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-02  10:45
 *
 */
public class LimitTest {
    public static void main(String[] args) {
        char[] chars = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        System.out.println("buffer capacity:" + charBuffer.capacity() + "\tbuffer limit:"
                + charBuffer.limit());
        System.out.println();
        charBuffer.limit(3);
        System.out.println("after limit modified:");
        System.out.println("buffer capacity:" + charBuffer.capacity() + "\tbuffer limit:"
                + charBuffer.limit());
        charBuffer.put(0, '0');
        charBuffer.put(1, '1');
        charBuffer.put(2, '2');
        
        // java.lang.IndexOutOfBoundsException
        // charBuffer.put(3, '3');
        // charBuffer.put(4, '4');
        // charBuffer.put(5, '5');
        // charBuffer.put(6, '6');
        
        // java.lang.IndexOutOfBoundsException
        // charBuffer.charAt(3);
        
        // java.lang.IllegalArgumentException: newLimit > capacity: (8 > 7)
        // charBuffer.limit(chars.length+1);
        
        // starts from 0
        // charBuffer.limit(0);
    }
}
