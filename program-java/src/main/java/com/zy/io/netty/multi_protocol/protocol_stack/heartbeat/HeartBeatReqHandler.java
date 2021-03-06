package com.zy.io.netty.multi_protocol.protocol_stack.heartbeat;

import com.zy.io.netty.multi_protocol.protocol_stack.auth.MessageType;
import com.zy.io.netty.multi_protocol.protocol_stack.data.Header;
import com.zy.io.netty.multi_protocol.protocol_stack.data.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @Author : ZhangYun
 * @Description :客户端发送心跳消息
 * @Date :  2017/8/2.
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    /*
        握手成功后，握手请求Handler会继续将握手成功消息向下透传，HeartBeatReqHandler接收到后对消息进行判断，如果是握手成功消息，则启动无限循环定时器用于定期发送心跳消息。
        由于NioEventLoop是一个Schedule,因此它支持定时器的执行。心跳定时器的单位为毫秒，默认5000，即每5s发送一条心跳消息。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //握手成功，主动发送心跳消息
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
            heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler.HeartBeatTask(ctx),0,5000, TimeUnit.MILLISECONDS);
        }else if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()){//为了统一在一个Handler里处理所有的心跳消息，因此在此接收服务端发送的心跳应答消息。
            System.out.println("Client receive server heart beat message : --> "+message);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
    //心跳应答定时器
    public class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;
        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Client send heart beat message to server : ---> "+heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            message.setHeader(header);
            return  message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
