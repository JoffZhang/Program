package com.zy.io.netty.multi_protocol.http_xml.example.e_request;

import com.zy.io.netty.multi_protocol.http_xml.example.AbstractHttpXmlDecoder;
import com.zy.io.netty.multi_protocol.http_xml.example.HttpXmlRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Author : ZhangYun
 * @Description :请求消息解码类
 * 本例程没有考虑XML消息解码失败后的异常封装和处理，在商用项目中需要统一的异常处理机制，提升协议栈的健壮性和可靠性。
 * @Date :  2017/7/31.
 */
public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest> {


    protected HttpXmlRequestDecoder(Class<?> clazz) {
        super(clazz);
    }

    /**
     *
     * @param clazz     需要解码的对象的类型信息
     * @param isPrint   是否打印HTTP消息体码流的码流开关，码流开关默认关闭。
     */
    public HttpXmlRequestDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, List<Object> list) throws Exception {
        //对HTTP请求消息本身的解码结果进行判断，如果已经解码失败，再对消息体进行二次解码已经没有意义。
        if(!fullHttpRequest.getDecoderResult().isSuccess()){
            //如果HTTP消息本身解码失败，则构造处理结果异常的HTTP应答消息返回给客户端。
            sendError(channelHandlerContext,BAD_REQUEST);
            return ;
        }
        //通过HttpXmlRequest和反序列化后的Order对象构造HttpXmlRequest实例，最后将它添加到解码结果List列表中。
        HttpXmlRequest request = new HttpXmlRequest(fullHttpRequest,decode0(channelHandlerContext,fullHttpRequest.content()));
        list.add(request);
    }

    private void sendError(ChannelHandlerContext channelHandlerContext, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,status, Unpooled.copiedBuffer("Failure: "+ status.toString()+"\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
