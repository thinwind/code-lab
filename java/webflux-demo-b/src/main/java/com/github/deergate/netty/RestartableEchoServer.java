package com.github.deergate.netty;

import java.util.concurrent.TimeUnit;
import java.net.InetSocketAddress;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * Listing 2.2 EchoServer class
 *
 * @author <a href="mailto:norman.maurer@gmail.com">Norman Maurer</a>
 */
@Slf4j
public class RestartableEchoServer {
    private final int port;
    
    private Channel channel;
    
    private EventLoopGroup group = new NioEventLoopGroup(4);
    
    ScheduledFuture<?> scheduledFuture;
    
    ServerBootstrap b;

    public RestartableEchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args){
        new RestartableEchoServer(1234).start();
    }

    public void start(){
        final EchoServerHandler serverHandler = new EchoServerHandler();
        b = new ServerBootstrap();
            b.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(serverHandler);
                    }
                });
       doBind();
    }
    
    public synchronized void stop(){
        if (channel != null) {
            channel.close();
            channel = null;
        }
        //todo close all the connected channels
    }
    
    public synchronized void reBind(){
        if (channel != null) {
            channel.close();
            channel = null;
        }
        doBind();
    }

    private void doBind() {
        if (channel != null && channel.isActive()) {
            return;
        }

        ChannelFuture future = b.bind();
        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                if (scheduledFuture != null) {
                    scheduledFuture.cancel(true);
                }
                if (f.isSuccess()) {
                    channel = f.channel();
                    log.info("Start completed.");
                } else {
                    log.info("Start failed. Another try will be given after 5 secs...");
                    scheduledFuture = group.schedule(() -> doBind(), 5, TimeUnit.SECONDS);
                }
            }

        });

        try {
            future.sync();
            Channel ch = future.channel();
            if (ch != null) {
                ch.closeFuture().addListener(c -> {
                    log.info("Server closed. Another try will be given after 5 secs...");
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                    }
                    scheduledFuture = group.schedule(() -> doBind(), 5, TimeUnit.SECONDS);
                });
            }
        } catch (InterruptedException e) {
            log.error("连接失败", e);
        }
    }
}
