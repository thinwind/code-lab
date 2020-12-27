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
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * TODO InterruptTest说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-06  20:11
 *
 */
public class InterruptTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", 9909));
        serverSocketChannel.configureBlocking(false);
        var mainThread = Thread.currentThread();

        Selector selector = Selector.open();
        SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        var thread = new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    mainThread.interrupt();                    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
            }
        };
        thread.start();
        
        var isRunning = true;
        while(isRunning){
            System.out.println("begin: "+System.currentTimeMillis());
            int keyCount = selector.select();
            mainThread.interrupted();
            System.out.println("  end: "+System.currentTimeMillis()+" keyCount="+keyCount);
            var keys = selector.keys();
            var selectedKeys = selector.selectedKeys();
            var iterator = selectedKeys.iterator();
            while(iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel serverChanel = (ServerSocketChannel) key.channel();
                    ServerSocket serverSocket = serverChanel.socket();
                    Socket socket = serverSocket.accept();
                    socket.close();
                }
            }
        }
        serverSocketChannel.close();
    }
}
