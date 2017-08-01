package com.zy.io.netty.codec.java;

import java.io.*;

/**
 * @Author : ZhangYun
 * @Description :编码后码流大小测试类
 * @Date :  2017/7/25.
 */
public class TestUserInfo {
    public static void main(String[] args) throws IOException {
        UserInfo info = new UserInfo();
        info.buildUserID(100).buildUserName("Welcome to Netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(info);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("The jdk serializable length is : "+ b.length);
        bos.close();
        System.out.println("--------");
        System.out.println("The byte array serializable length is : "+info.codeC().length);

    }
    /**
     The jdk serializable length is : 134
     --------
     The byte array serializable length is : 24
     */
}
