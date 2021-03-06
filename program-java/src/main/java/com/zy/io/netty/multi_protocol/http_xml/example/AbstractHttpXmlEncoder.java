package com.zy.io.netty.multi_protocol.http_xml.example;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/28.
 */
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    IBindingFactory factory = null;
    StringWriter writer = null;
    final static  String CHARSET_NAME = "UTF-8";
    final static Charset UTF_8 = Charset.forName(CHARSET_NAME);


    protected ByteBuf encode0(ChannelHandlerContext channelHandlerContext, Object body) throws Exception {
        //在此将业务的Order实例序列化为XML字符串。
        factory = BindingDirectory.getFactory(body.getClass());
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(body,CHARSET_NAME,null,writer);
        String xmlStr = writer.toString();
        writer.close();
        writer = null;
        //将XML字符串包装成Netty的ByteBuf并返回，实现了HTTP请求消息的XML编码。
        ByteBuf encodeBuf = Unpooled.copiedBuffer(xmlStr,UTF_8);
        return encodeBuf;
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(writer != null){
            writer.close();
            writer = null;
        }
    }
}
