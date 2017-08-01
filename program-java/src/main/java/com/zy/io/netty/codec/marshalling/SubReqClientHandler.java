package com.zy.io.netty.codec.marshalling;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/26.
 */
public class SubReqClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for(int i = 0 ; i<10;i++){
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println("Receive server response : ["+msg+"]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
    private SubscribeReq subReq(int i){
        SubscribeReq req = new SubscribeReq();
        req.setSubReqID(i);
        req.setUserName("Lisi");
        req.setProductName("Netty book for Marshalling");
        List<String> address = new ArrayList<>();
        address.add("Nanjing YuHuaTai");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        req.setAddress(address);
        return req;
    }

}
