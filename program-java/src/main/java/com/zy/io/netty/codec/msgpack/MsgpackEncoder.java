package com.zy.io.netty.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Author : ZhangYun
 * @Description :编码器
 * @Date :  2017/7/25.
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object>{
    //继承 MessageToByteEncoder负责将Object类型的POJO对象编码为byte数组，然后写入到ByteBuffer
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        MessagePack msgPack = new MessagePack();
        byte[] raw = msgPack.write(o);
        byteBuf.writeBytes(raw);
    }
}
