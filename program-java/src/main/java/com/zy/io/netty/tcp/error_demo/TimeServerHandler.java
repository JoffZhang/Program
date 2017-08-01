package com.zy.io.netty.tcp.error_demo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;


/**
 * @Author : ZhangYun
 * @Description :未考虑TCP粘包导致功能异常
 * 当压力上来，或者发送大报文后，就会存在粘包/拆包问题。
 * @Date :  2017/7/20.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    private int counter;

    //每读到一条消息后，就计数一次，然后发送应答消息给客户端。按设计，服务端接收到的消息应该跟客户端发送的消息总数相同，
    //而且请求消息删除回车换行符后应该为“QUERY TIME ORDER"
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8").substring(0,req.length-System.getProperty("line.separator").length());
        System.out.println("The time server reveive order : "+ body+" ; the counter is : "+ ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        currentTime = currentTime+System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();//作用：将消息发送队列的消息写入到SocketChannel中发送给对方。
        //从性能考虑，为了防止频繁地唤醒Selector进行消息发送，Netty的write方法并不直接将消息写入SocketChannel中，调用write方法只是把待发送的消息放到发送缓冲数组中，再通过调用flush方法，将发送缓冲区的消息全部到SocketChannel中
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();//发生异常，关闭ChannelHandlerContext，释放和ChannelHandlerContext相关联的句柄等资源。
    }


}
