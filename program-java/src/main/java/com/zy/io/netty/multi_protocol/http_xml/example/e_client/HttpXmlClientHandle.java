package com.zy.io.netty.multi_protocol.http_xml.example.e_client;

import com.zy.io.netty.multi_protocol.http_xml.example.HttpXmlRequest;
import com.zy.io.netty.multi_protocol.http_xml.example.HttpXmlResponse;
import com.zy.io.netty.multi_protocol.http_xml.jibx.OrderFactory;
import com.zy.io.netty.multi_protocol.http_xml.jibx.pojo.Order;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author : ZhangYun
 * @Description :业务逻辑编排类
 * @Date :  2017/7/31.
 */
public class HttpXmlClientHandle extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Order order = OrderFactory.create(123);
        HttpXmlRequest request = new HttpXmlRequest(null,order);
        ctx.writeAndFlush(request);//发送构造的HttpXmlRequest
    }

    /**
     * 用于接收服务端的应答消息，从接口分析，它接收到的已经是自动解码后的HttpXmlResponse对象了
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("The client receive response of http header is : "+ msg.getHttpResponse().headers().names());
        System.out.println("The client receive response of http body is : "+ msg.getResult());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
