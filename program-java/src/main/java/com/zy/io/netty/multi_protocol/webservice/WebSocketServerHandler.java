package com.zy.io.netty.multi_protocol.webservice;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.PARTIAL_CONTENT;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/1.
 */
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        //传统的HTTP接入
        //第一次握手请求消息由HTTP协议承载，所以它是一个HTTP消息，执行handlerHttpRequest方法处理WebSocket握手请求。
        if(msg instanceof FullHttpRequest){
            handleHttpRequest(channelHandlerContext,(FullHttpRequest)msg);
        }
        //以上链路创建成功后，客户端通过文本框提交请求消息给服务端，WebSocketServerHandler接收到的已经是解码后的WebSocketFrame消息。
        //WebSocket接入
        else if(msg instanceof WebSocketFrame){
            handleWebSocketFrame(channelHandlerContext,(WebSocketFrame)msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 对WebSocket请求消息进行处理
     * @param channelHandlerContext
     * @param frame
     */
    private void handleWebSocketFrame(ChannelHandlerContext channelHandlerContext, WebSocketFrame frame) {
        //首先需要对控制帧进行判断
        //判断是否是关闭链路的指令      如果是关闭链路的消息，就调用WebSocketServerHandshaker的close方法关闭WebSocket连接；
        if(frame instanceof CloseWebSocketFrame){

            handshaker.close(channelHandlerContext.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        //判断是否是Ping消息       如果是维持链路的PING消息，则构造PING消息返回。
        if(frame instanceof PingWebSocketFrame){
            channelHandlerContext.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //本例程只支持文本，不支持二进制消息
        if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException(String.format("%s frame types not supported",frame.getClass().getName()));
        }

        //返回应答消息
        //从TextWebSocketFrame中获取请求消息字符串，对它处理后通过构造新的TextWebSocketFrame消息返回给客户端，由于握手应答时动态增加了TextWebSocketFrame的编码类，所以可以直接发送TextWebSocketFrame对象。
        String request = ((TextWebSocketFrame) frame).text();
        if(logger.isLoggable(Level.FINE)){
            logger.fine(String.format("%s received %s",channelHandlerContext.channel(),request));
        }
        channelHandlerContext.channel().write(new TextWebSocketFrame(request+" , 欢迎使用Netty WebSocket 服务，现在时刻："+new Date().toString()));
    }

    /**
        握手请求简单校验通过后，开始构造握手工厂，创建握手处理类WebSocketServerHandShaker,通过它构造握手响应消息返回给客户端，
        handshaker.handshake
             if(ctx == null) {
                 ctx = p.context(HttpServerCodec.class);
                 if(ctx == null) {
                 promise.setFailure(new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
                 return promise;
                 }

                 p.addBefore(ctx.name(), "wsdecoder", this.newWebsocketDecoder());
                 p.addBefore(ctx.name(), "wsencoder", this.newWebSocketEncoder());
                 encoderName = ctx.name();
             } else {
                 p.replace(ctx.name(), "wsdecoder", this.newWebsocketDecoder());
                 encoderName = p.context(HttpResponseEncoder.class).name();
                 p.addBefore(encoderName, "wsencoder", this.newWebSocketEncoder());
             }
         同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码
         添加WebSocket Encoder Decoder后，服务端就可自动对WebSocket消息进行编解码了，后面的义务handler可直接对WebSocket对象进行操作。

     * @param channelHandlerContext
     * @param request
     */
    private void handleHttpRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) {
        //如果HTTP解码失败，返回HTTP异常
        //首先对握手请求消息进行判断，如果消息头中没有包含UpGrade字段或者它的值不是websocket，则返回HTTP 400
        if(!request.getDecoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))){
            sendHttpResponse(channelHandlerContext,request,new DefaultFullHttpResponse(HTTP_1_1,BAD_REQUEST)) ;
            return;
        }
        //构造握手相应返回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",null,false);
        handshaker = wsFactory.newHandshaker(request);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(channelHandlerContext.channel());
        }else {
            handshaker.handshake(channelHandlerContext.channel(),request);
        }
    }

    private void sendHttpResponse(ChannelHandlerContext channelHandlerContext, FullHttpRequest request, DefaultFullHttpResponse defaultFullHttpResponse) {
        //返回应答给客户端
        if(defaultFullHttpResponse.getStatus().code() != 200){
            ByteBuf byteBuf = Unpooled.copiedBuffer(defaultFullHttpResponse.getStatus().toString(), CharsetUtil.UTF_8);
            defaultFullHttpResponse.content().writeBytes(byteBuf);
            byteBuf.release();
            setContentLength(defaultFullHttpResponse,defaultFullHttpResponse.content().readableBytes());
        }
        //如果是非Keep-Alive,关闭连接
        ChannelFuture f = channelHandlerContext.channel().writeAndFlush(defaultFullHttpResponse);
        if(!isKeepAlive(request) || defaultFullHttpResponse.getStatus().code() != 200){
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
