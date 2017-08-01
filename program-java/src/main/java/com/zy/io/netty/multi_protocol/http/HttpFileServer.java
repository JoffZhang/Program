package com.zy.io.netty.multi_protocol.http;

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
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.Socket;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/27.
 */
public class HttpFileServer {

    private static final String DEFAULT_PATH = "/program-java/src/main/java/com/zy/io/netty";


    private void run(final int port,final String url) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("http-decoder",new HttpRequestDecoder());
                            //HttpObjectAggregator ： 将多个消息转换为单一的FullHttpRequest或者FullHttpResponse,
                            // 原因是HTTP解码器在每个HTTP消息中会生成多个消息对象。
                            //1HttpRequest / HttpResponse  2 HttpContent  3 LastHttpContent
                            socketChannel.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            socketChannel.pipeline().addLast("http-encoder",new HttpResponseEncoder());
                            //ChunkedWriteHandler : 支持异步发送大的码流（如文件传输），但不占用过多内存。防止发生Java内存溢出错误。
                            socketChannel.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
                            //用于文件服务器的业务逻辑处理。
                            socketChannel.pipeline().addLast("fileServerHandler",new HttpFileServerHandler(url));
                        }
                    });
            ChannelFuture f = b.bind("localhost",port).sync();
            System.out.println("HTTP文件目录服务器启动，网址："+ "http://localhost:"+port+url);
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    public static void main(String[] args) {
        int port= 8080;
        if(args != null && args.length>0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        String url = DEFAULT_PATH;
        if(args.length > 1){
            url = args[1];
        }
        new HttpFileServer().run(port,url);
    }

}
