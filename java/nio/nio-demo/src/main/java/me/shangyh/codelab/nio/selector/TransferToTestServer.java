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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 *
 * TODO TransferToTestServer说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2020-11-03  13:49
 *
 */
public class TransferToTestServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();
        SocketChannel socketChannel = null;
        channel.configureBlocking(false);
        channel.bind(new InetSocketAddress("localhost", 19099));

        Selector selector = Selector.open();
        channel.register(selector, SelectionKey.OP_ACCEPT);

        boolean isRunning = true;
        while (isRunning) {
            selector.select();
            var set = selector.selectedKeys();
            var iterator = set.iterator();
            while (iterator.hasNext()) {
                var key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_WRITE);
                }
                if (key.isWritable()) {
                    SocketChannel socketChannel2 = (SocketChannel) key.channel();
                    FileInputStream file =
                            new FileInputStream("C:\\Users\\Shangyh\\BookLib\\cobol\\test.pdf");
                    FileChannel fileChannel = file.getChannel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(100000);
                    while (fileChannel.position() < fileChannel.size()) {
                        fileChannel.read(byteBuffer);
                        byteBuffer.flip();
                        while (byteBuffer.hasRemaining()) {
                            socketChannel2.write(byteBuffer);
                        }
                        byteBuffer.clear();
                    }
                    System.out.println("结束写操作");
                    socketChannel.close();
                    file.close();
                    // var file = new RandomAccessFile("C:\\Users\\Shangyh\\BookLib\\cobol\\test.pdf",
                    //         "rw");
                    // System.out.println("file length=" + file.length());
                    // FileChannel fileChannel = file.getChannel();
                    // long fileSize = file.length();
                    // long transfered = fileChannel.transferTo(0, file.length(), socketChannel);
                    // while(transfered<fileSize){
                    //     transfered+=fileChannel.transferTo(transfered, fileSize-transfered, fileChannel);
                    // }
                    // fileChannel.close();
                    // file.close();
                    // socketChannel.close();
                    // isRunning = false;
                }
            }
        }
        channel.close();
    }
}
