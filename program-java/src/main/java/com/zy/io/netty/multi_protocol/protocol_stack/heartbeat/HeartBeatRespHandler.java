package com.zy.io.netty.multi_protocol.protocol_stack.heartbeat;

import com.zy.io.netty.multi_protocol.protocol_stack.auth.MessageType;
import com.zy.io.netty.multi_protocol.protocol_stack.data.Header;
import com.zy.io.netty.multi_protocol.protocol_stack.data.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Author : ZhangYun
 * @Description :服务端心跳应答
 * @Date :  2017/8/2.
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //返回心跳应答消息
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()){
            System.out.println("Receive client heart beat message : --->" + message);
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("Send heart beat response message to client : ---> "+ heartBeat);
            ctx.writeAndFlush(heartBeat);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.value());
        message.setHeader(header);
        return message;
    }
}
