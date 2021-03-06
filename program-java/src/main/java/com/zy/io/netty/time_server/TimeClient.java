package com.zy.io.netty.time_server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/20.
 */
public class TimeClient {


    private void connect(int port, String host) {
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,Boolean.TRUE)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        //initChannel  ：当NioSocketChannel成功之后，在进行初始化时，将它的ChannelHandler设置到ChannelPipeline中，用于处理网络I/O事件
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();//发起异步连接，然后调用同步方法等待连接成功。
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅的退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new TimeClient().connect(port,"localhost");
    }

}
