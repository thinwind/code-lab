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
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 *
 * TODO TransferToTestClient说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  14:00
 *
 */
public class TransferToTestClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("localhost", 19099));
        var selector = Selector.open();
        channel.register(selector, SelectionKey.OP_CONNECT);
        var pdfFile = new RandomAccessFile("c:\\tmp\\test.pdf", "rw");
        var pdf = pdfFile.getChannel();
        var isRunning = true;
        while (isRunning) {
            System.out.println("begin selector");
            if (channel.isOpen()) {
                selector.select();
                System.out.println(" end selector");
                var set = selector.selectedKeys();
                var iterator = set.iterator();
                while (iterator.hasNext()) {
                    var key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        while (!channel.finishConnect()) {
                        }
                        channel.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()){
                        // ByteBuffer byteBuffer = ByteBuffer.allocate(50000);
                        // int readLength = channel.read(byteBuffer);
                        // while(readLength != -1){
                        //     byteBuffer.flip();
                        //     pdf.write(byteBuffer);
                        //     byteBuffer.clear();
                        //     readLength = channel.read(byteBuffer);
                        // }
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        pdf.transferFrom(socketChannel, 0,Integer.MAX_VALUE);
                        System.out.println("读取结束");
                        socketChannel.close();
                        channel.close();
                        pdf.close();
                        pdfFile.close();   
                    }
                }
            } else {
                break;
            }
        }
    }
}
