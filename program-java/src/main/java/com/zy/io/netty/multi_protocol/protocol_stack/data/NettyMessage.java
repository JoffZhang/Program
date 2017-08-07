package com.zy.io.netty.multi_protocol.protocol_stack.data;

import java.io.Serializable;

/**消息体定义
 * @Author : ZhangYun
 * @Description :由于心跳消息、握手请求和握手应答消息都可以统一由NettyMessage承载，所以不需要为这几类控制消息做单独的数据类型定义。
 * @Date :  2017/8/2.
 */
public final class NettyMessage{

    private Header header ; //消息头
    private Object body; //消息体

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
