package com.zy.io.netty.tcp.error_demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/20.
 */
public class TimeServer {


    private void bind(int port) {
        //配置服务端的NIO线程组
        //NioEventLoopGroup 线程组，包含一组NIO线程，专门用于网络事件处理，实际上他们就是Reactor线程组。
        //这里创建俩个：一个用于服务端接受客户端的连接，一个进行SocketChannel的网络读写。
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();//ServerBootstrap ： 用于启动NIO服务端的辅助启动类，目的是降低服务端的开发难度。
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)  //设置创建的Channel为NioServerSocketChannel，它的功能对应对NIO类库的ServerSocketChannel类
                    .option(ChannelOption.SO_BACKLOG,1024)  //配置TCP参数
                    .childHandler(new ChildChannelHandler());   //绑定I/O事件的处理类ChildChannelHandler.它的作用类似与Reactor模式中的Handler类，主要用于处理网络I/O事件，例如记录日志/对消息进行编解码等。
            //服务端启动辅助类配置完成后，调用它的bind方法绑定监听端口，随后调用它的同步阻塞方法sync等待绑定操作完成。
            //绑定端口，同步等待成功
            ChannelFuture f = b.bind(port).sync();//ChannelFuture 功能类似于java.util.concurrent.Future,主要用于异步操作的通知回调。
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();//进行阻塞，等待服务端链路关闭之后main函数才退出。
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

        protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new TimeServerHandler());
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
        new TimeServer().bind(port);
    }

}
