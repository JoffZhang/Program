package com.zy.io.netty.codec.java;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @Author : ZhangYun
 * @Description :java序列化产生超大码流
 *
 * @Date :  2017/7/25.
 */
public class UserInfo implements Serializable {

    /**
     * 默认序列号
     */
    private static final long serialVersionUID = 1L;

    private String userName;
    private int userID;

    /**
     * 使用基于ByteBuffer的通用二进制编解码技术对UserInfo对象进行编码，编码结果仍为byte数组，可与传统的JDK序列化后后的码流大小进行对比。
     * @return
     */
    public byte[] codeC(){
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return  result;
    }
    public byte[] codeC(ByteBuffer buffer){
        buffer.clear();
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userID);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    public UserInfo buildUserName(String userName){
        this.userName = userName;
        return this;
    }
    public UserInfo buildUserID(int userID){
        this.userID = userID;
        return this;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
