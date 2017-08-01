package com.zy.io.netty.multi_protocol.http_xml.example.e_client;

import com.zy.io.netty.multi_protocol.http_xml.example.e_request.HttpXmlRequestEncoder;
import com.zy.io.netty.multi_protocol.http_xml.example.e_response.HttpXmlResponseDecoder;
import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.net.InetSocketAddress;

/**
 * @Author : ZhangYun
 * @Description :
 * 功能：
 *  发起HTTP连接请求
 *  构造订购请求消息，将其编码成XML，通过HTTP协议发送给服务端；
 *  接收HTTP服务端的应答消息，将XML应答消息反序列化为订购消息POJO对象；
 *  关闭HTTP链接。
 * @Date :  2017/7/31.
 */
public class HttpXmlClient {

    private void connect(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //负责将二进制码流解码成为HTTP的应答消息
                            socketChannel.pipeline().addLast("http-decoder",new HttpResponseDecoder());
                            //将1个HTTP请求消息的多个部分合并成一个完成的HTTP消息
                            socketChannel.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
                            // XML解码器
                            socketChannel.pipeline().addLast("xml-decoder",new HttpXmlResponseDecoder(Order.class,true));

                            //注：需要注意顺序====编码的时候是按照从未到头的顺序调度执行的。
                            socketChannel.pipeline().addLast("http-encoder",new HttpRequestEncoder());
                            socketChannel.pipeline().addLast("xml-encoder",new HttpXmlRequestEncoder());
                            socketChannel.pipeline().addLast("xmlClientHandler",new HttpXmlClientHandle());
                        }
                    });
            // 发起异步连接操作
            ChannelFuture f = b.connect(new InetSocketAddress(port)).sync();
            // 等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅退出，释放NIO线程组
            group.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        new HttpXmlClient().connect(port);
    }

}
