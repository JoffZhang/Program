package com.zy.io.netty.codec.protobuf.test.example;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/26.
 */
public class TestSubscribeReqProto {

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);//parseFrom将二进制byte数组解码为原始对象
    }

    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();//toByteArray 编码为byte数组
    }
    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        //通过Builder构建器对SubscribeReq的属性进行设置。
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();//创建实例
        builder.setSubReqID(1);
        builder.setUserName("Lisi");
        builder.setProductName("Netty book");
        List<String> address = new ArrayList<>();
        address.add("Nanjing YuHuaTai");
        address.add("BeiJing LiuLiChang");
        address.add("ShenZhen HongShuLin");
        builder.addAllAddress(address);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("Before encode : "+ req.toString());
        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("After decode ：" + req2.toString());
        System.out.println("Assert equal : -> " + req2.equals(req));
    }


}
