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
package me.shangyh.codelab.nio.selector;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 *
 * TODO ProviderTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  10:07
 *
 */
public class ProviderTest {
    public static void main(String[] args) throws IOException {
        SelectorProvider provider1 = SelectorProvider.provider();
        System.out.println(provider1);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        SelectorProvider provider2 = serverSocketChannel.provider();
        System.out.println(provider2);
    }
}
