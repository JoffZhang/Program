package com.zy.io.netty.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/25.
 */
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf>{

    //先从数据报byteBuf中获取需要解码的byte数组，然后调用MessagePack的read方法将其反序列化为Object对象，然后将解码后对象加入到list列表里。
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        final byte[] array;
        final int length = byteBuf.readableBytes();
        array = new byte[length];
        byteBuf.getBytes(byteBuf.readerIndex(),array,0 ,length);
        MessagePack msgPack = new MessagePack();
        list.add(msgPack.read(array));
    }
}
