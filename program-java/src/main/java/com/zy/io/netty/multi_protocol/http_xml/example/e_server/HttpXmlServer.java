package com.zy.io.netty.multi_protocol.http_xml.example.e_server;

import com.zy.io.netty.multi_protocol.http_xml.example.e_request.HttpXmlRequestDecoder;
import com.zy.io.netty.multi_protocol.http_xml.example.e_response.HttpXmlResponseEncoder;
import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Order;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

/**
 * @Author : ZhangYun
 * @Description :服务端
 *  功能：
 *      接收Http客户端连接
 *      接收Http客户端XML请求消息，并将其转码为POJO对象
 *      对POJO对象进行业务处理，构造应答消息返回
 *      通过HTTP+XML的格式返回应答消息
 *      主动关闭HTTP连接
 * @Date :  2017/7/31.
 */
public class HttpXmlServer {


    private void run(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            socketChannel.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            socketChannel.pipeline().addLast("xml-decoder",new HttpXmlRequestDecoder(Order.class, true));
                            socketChannel.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            socketChannel.pipeline().addLast("xml-encoder",new HttpXmlResponseEncoder());
                            socketChannel.pipeline().addLast("xmlServerHandler",new HttpXmlServerHandler());
                        }
                    });
            ChannelFuture f = b.bind(new InetSocketAddress(port)).sync();
            System.out.println("HTTP 订购服务器启动，网址是： "+ "http://localhost:"+port);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length >0){
            try{
                port = Integer.valueOf(port);
            }catch (NumberFormatException e){

            }
        }
        new HttpXmlServer().run(port);
    }

}
