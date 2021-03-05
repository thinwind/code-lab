/* 
 * Copyright 2021 Shang Yehua
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
package me.shangyh.codelab.nio.jnio;

import java.nio.ByteBuffer;

/**
 *
 * TODO ByteBufferTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-02-04  15:32
 *
 */
public class ByteBufferTest {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)'H').put((byte)'e').put((byte)'l')
        .put((byte)'l').put((byte)'o');
        System.out.println(new String(buffer.array()));
        buffer.put(0,(byte)'M').put((byte)'w');
        System.out.println(new String(buffer.array()));
    }
}
