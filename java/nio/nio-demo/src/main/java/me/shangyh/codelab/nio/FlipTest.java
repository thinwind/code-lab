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
 * TODO FlipTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  10:15
 *
 */
public class FlipTest {
    public static void main(String[] args) {
        CharBuffer charBuffer = CharBuffer.allocate(50);
        System.out.println(
                "Init: position=" + charBuffer.position() + "\tlimit=" + charBuffer.limit());
        charBuffer.put("曾梦想仗剑走天涯，后来bug多没去");
        System.out.println(
                "after put: position=" + charBuffer.position() + "\tlimit=" + charBuffer.limit());
        charBuffer.position(0);
        System.out.println("after position reset: position=" + charBuffer.position() + "\tlimit="
                + charBuffer.limit());
        System.out.println("开始读取:");
        for (int i = 0; i < charBuffer.limit(); i++) {
            showChar(charBuffer.get());
        }
        System.out.print("#end\n");
        charBuffer.clear();

        // 手动flip
        charBuffer.put("生活不止眼前的枸杞，还有未来的拖鞋");
        System.out.println(
                "after put: position=" + charBuffer.position() + "\tlimit=" + charBuffer.limit());
        charBuffer.limit(charBuffer.position());
        charBuffer.position(0);
        System.out.println("after position reset: position=" + charBuffer.position() + "\tlimit="
                + charBuffer.limit());
        System.out.println("开始读取:");
        for (int i = 0; i < charBuffer.limit(); i++) {
            showChar(charBuffer.get());
        }
        System.out.print("#end\n");
        
        //flip
        
    }

    static void showChar(char charVal) {
        if (charVal == 0) {
            System.out.print(" ");
        } else {
            System.out.print(charVal);
        }
    }
}
