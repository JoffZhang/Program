package com.zy.io.netty.multi_protocol.protocol_stack.codec;

import com.zy.io.netty.multi_protocol.protocol_stack.data.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/2.
 */
public final class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

    NettyMarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() {
        this.marshallingEncoder = MarshallingCodecFactory.buildMarshalling();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {
        if (msg == null || msg.getHeader() == null)
            throw new Exception("The encode data is null");
        //写入数据，按照NettyMessage的格式
        ByteBuf sendBuf = Unpooled.buffer();
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(ctx,value,sendBuf);
         }

        key = null;
        keyArray = null;
        value = null;
        if(msg.getBody() != null ){
            marshallingEncoder.encode(ctx,msg.getBody(),sendBuf);
        }else{
            sendBuf.writeInt(0);
        }
        sendBuf.setInt(4,sendBuf.readableBytes());
        out.add(sendBuf);
    }
}
