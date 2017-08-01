package com.zy.io.netty.time_server;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;


/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/20.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    //ChannelHandlerAdapter : 它用于对网络事件进行读写操作，通常我们只关注channelRead和exceptionCaught方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("The time server reveive order : "+ body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
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
