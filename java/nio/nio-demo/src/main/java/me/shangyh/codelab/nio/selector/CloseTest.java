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
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * TODO CloseTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-06  17:41
 *
 */
public class CloseTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 9099));
        serverSocketChannel.configureBlocking(false);

        var selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        var thread = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    selector.close();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();

        var isRunning = true;
        while(isRunning){
            System.out.println("begin: "+System.currentTimeMillis());
            selector.select();
            System.out.println("end: "+System.currentTimeMillis());
            var keys = selector.keys();
            var selectedKeys = selector.selectedKeys();
            var iterator = selectedKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key=iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    ServerSocket socket = channel.socket();
                    var clientSocket = socket.accept();
                    clientSocket.close();
                }
            }
        }
        serverSocketChannel.close();
    }
}
