package com.zy.io.netty.multi_protocol.http_xml.example.e_response;

import com.zy.io.netty.multi_protocol.http_xml.example.AbstractHttpXmlDecoder;
import com.zy.io.netty.multi_protocol.http_xml.example.HttpXmlResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/31.
 */
public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {

    protected HttpXmlResponseDecoder(Class<?> clazz) {
        super(clazz);
    }

    public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws Exception {
        //通过DefaultFullHttpResponse和HTTP应答消息反序列化后的POJO对象构造HttpXmlResponse，并将其添加到解码结果列表中。
        HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse(msg,decode0(ctx,msg.content()));
        out.add(resHttpXmlResponse);
    }
}
