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
package me.shangyh.codelab.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * TODO SelectorTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  10:29
 *
 */
public class SelectorTest {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        Selector selector2 = Selector.open();
        
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        System.out.println(serverSocketChannel.isBlocking());
        serverSocketChannel.bind(new InetSocketAddress("localhost", 9099));
        serverSocketChannel.configureBlocking(false);
        System.out.println(serverSocketChannel.isBlocking());

        SelectionKey key1 = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        SelectionKey key2 = serverSocketChannel.register(selector2, SelectionKey.OP_ACCEPT);
        // Exception in thread "main" java.lang.IllegalArgumentException
        // ServerSocketChannel 只支持 OP_ACCEPT
        // SelectionKey key3 = serverSocketChannel.register(selector2, SelectionKey.OP_CONNECT);
        
        System.out.println(key1.hashCode());
        System.out.println(key2.hashCode());
        // System.out.println(key3.hashCode());
    }
}
