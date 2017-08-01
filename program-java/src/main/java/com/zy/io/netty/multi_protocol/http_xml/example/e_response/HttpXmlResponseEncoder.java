package com.zy.io.netty.multi_protocol.http_xml.example.e_response;

import com.zy.io.netty.multi_protocol.http_xml.example.AbstractHttpXmlEncoder;
import com.zy.io.netty.multi_protocol.http_xml.example.HttpXmlResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/31.
 */
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, HttpXmlResponse httpXmlResponse, List<Object> out) throws Exception {
        ByteBuf body = encode0(channelHandlerContext,httpXmlResponse.getResult());
        FullHttpResponse response = httpXmlResponse.getHttpResponse();//对应答消息进行判断，如果业务侧已经构造了Http应答消息，则利用业务已有应答消息重复复制一个新的HTTP应答消息。
        // 无法重用业务侧自定义HTTP应答消息的主要原因，是Netty的DefaultFullHttpResponse没有提供动态设置消息体的content的接口，只能在第一次构造的时候设置内容。
        if(response  == null){
            response = new DefaultFullHttpResponse(HTTP_1_1,OK,body);
        }else{
            response = new DefaultFullHttpResponse(httpXmlResponse.getHttpResponse().getProtocolVersion(),httpXmlResponse.getHttpResponse().getStatus(),body);
        }
        response.headers().set(CONTENT_TYPE,"text/xml");//设置消息体内容格式为 text/xml
        setContentLength(response,body.readableBytes());    //在消息头中设置消息体的长度。
        out.add(response);  //将DefaultFullHttpResponse对象添加到编码结果列表中，由后续Netty的Http编码类进行二次编码。
    }
}
