/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
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
package com.github.deergate.netty;

import java.util.concurrent.TimeUnit;
import java.net.InetSocketAddress;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * TODO NettyClient说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-08-02  10:26
 *
 */
@Slf4j
public class NettyReconnectClient {

    private final String host;
    private final int port;

    public NettyReconnectClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private static final int WORKER_CNT = 4;

    private Channel channel;

    private EventLoopGroup group = new NioEventLoopGroup(WORKER_CNT);

    Bootstrap b;

    ScheduledFuture<?> scheduledFuture;
    
    public static void main(String[] args) {
        NettyReconnectClient client=new NettyReconnectClient("127.0.0.1",1234);
        client.start();
    }

    public synchronized void start() {
        b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(host, port))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new EchoClientHandler());
                    }
                });
        doConnect();
    }

    private void doConnect() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = b.connect();
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(true);
                }
                if (f.isSuccess()) {
                    channel = f.channel();
                    log.info("Connect completed.");
                } else {
                    log.info("Connect failed. Another try will be given after 5 secs...");
                    scheduledFuture = group.schedule(() -> doConnect(), 5, TimeUnit.SECONDS);
                }
            }

        });

        try {
            future.sync();
            Channel ch = future.channel();
            if (ch != null) {
                ch.closeFuture().addListener(c -> {
                    log.info("Channel closed. Another try will be given after 5 secs...");
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                    }
                    scheduledFuture = group.schedule(() -> doConnect(), 5, TimeUnit.SECONDS);
                });
            }
        } catch (InterruptedException e) {
            log.error("连接失败", e);
        }
    }
}
