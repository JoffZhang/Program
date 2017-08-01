package com.zy.io.netty.tcp.decoder.LineBaseFrameDecoder_StringDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/20.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger(TimeClientHandler.class.getName());
    private int counter;
    private byte[] req;

    public TimeClientHandler() {
        req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        //客户端跟服务端链路创建成功后，循环发送100条消息，没发送一条就杀心一次，保证每条消息都会被写入Channel。按设计，服务端应该接收100跳查询指令请求消息。
        for(int i=0;i<100;i++){
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }

    }
    //当服务端返回应答消息时，channelRead方法被调用
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        //客户端每接收到服务端一条应答消息，就打印一次计数器，按设计客户端应该打印100次服务端系统时间。
        System.out.println("NOW IS : "+ body + " ; the counter is : "+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        logger.warning("Unexpected exception from downstream : "+ cause.getMessage());
        ctx.close();
    }
}
