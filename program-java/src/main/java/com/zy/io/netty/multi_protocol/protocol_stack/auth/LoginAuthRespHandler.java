package com.zy.io.netty.multi_protocol.protocol_stack.auth;

import com.zy.io.netty.multi_protocol.protocol_stack.data.Header;
import com.zy.io.netty.multi_protocol.protocol_stack.data.NettyMessage;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author : ZhangYun
 * @Description :服务端的握手接入和安全认证的代码
 * @Date :  2017/8/2.
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {

    //nodeCheck whitekList主要用于提升握手的可靠性。
    private Map<String ,Boolean> nodeCheck = new ConcurrentHashMap<>();     //重复登陆保护
    private String[] whitekList = {"127.0.0.1"};                           //IP认证白名单列表

    /**
     * 用于用户接入认证，首先根据客户端的源地址（/127.0.0.1：12088）进行重复登录判断，如果客户端已经登陆成功，拒绝重复登录，
     * 以防止由于客户端重复登陆导致的句柄泄露。随后通过ChannelHandlerContext 的channel接口获取客户端InetSocketAddress地址，
     * 从中去的发送方的源地址信息，通过源地址进行白名单校验，校验通过握手成功，否则握手失败。最后通过buildResponse构造握手应答消息返回给客户端
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //如果是握手请求消息，处理，其他消息透传
        if(message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()){
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            //重复登陆，拒绝
            if(nodeCheck.containsKey(nodeIndex)){
                loginResp = buildResponse((byte) -1);
            }else{
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                boolean isOK = false;
                for(String WIP : whitekList){
                    if(WIP.equals(ip)){
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
                if(isOK){
                    nodeCheck.put(nodeIndex,true);
                }
            }
            System.out.println("The login response is : " + loginResp + " body [ "+ loginResp.getBody() + " ]");
            ctx.writeAndFlush(loginResp);
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildResponse(byte result) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());//握手应答消息
        message.setHeader(header);
        message.setBody(result);
        return message;
    }

    /*
        当发生异常关闭链路的时候，需要将客户端的信息从登陆注册表中去除，以保证后续客户端可以重连成功。
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(ctx.channel().remoteAddress().toString());//出现异常删除缓存
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
