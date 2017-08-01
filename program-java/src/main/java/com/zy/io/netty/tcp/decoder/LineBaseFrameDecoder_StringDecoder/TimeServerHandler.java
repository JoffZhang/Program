package com.zy.io.netty.tcp.decoder.LineBaseFrameDecoder_StringDecoder;

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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        /*
        msg就是删除回车换行符之后的请求消息，不需要额外考虑读半包问题，也不需要对请求消息进行编码。
         */
        System.out.println("The time server reveive order : "+ body+" ; the counter is : "+ ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        currentTime = currentTime+System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       ctx.close();//发生异常，关闭ChannelHandlerContext，释放和ChannelHandlerContext相关联的句柄等资源。
    }


}
