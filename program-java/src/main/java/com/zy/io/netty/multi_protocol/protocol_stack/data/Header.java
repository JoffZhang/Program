package com.zy.io.netty.multi_protocol.protocol_stack.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/8/2.
 */
public final class Header{
    //消息验证码
    //Oxabef:固定值，表示Netty协议消息，2个字节
    //主版本号：1~255 1个字节
    //次版本号：1~255 1个字节
    private int crcCode = 0xabef0101;
    private int length ;        //消息长度
    private long sessionID;    //会话ID
    private byte type;          //消息类型
    private byte priority;      //消息优先级 0-255
    private Map<String ,Object> attachment = new HashMap<>();//附件 扩展消息头

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sesseionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
