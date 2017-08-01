package com.zy.io.netty.multi_protocol.http_xml.example;

import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Author : ZhangYun
 * @Description :请求消息，包含两个成员变量FullHttpRequest 和编码对象 Object,用用实现和协议栈之间的解耦
 * @Date :  2017/7/28.
 */
public class HttpXmlRequest {
    //它包含两个成员变量FullHttpRequest和编码对象Object，用于实现和协议栈之间的解耦。
    private FullHttpRequest request;
    private Object body;

    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
