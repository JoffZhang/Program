package com.zy.io.netty.codec.msgpack;

import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @Author : ZhangYun
 * @Description :java序列化产生超大码流
 *
 * @Date :  2017/7/25.
 */
@Message
public class UserInfo{

    private String userName;
    private int age;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
