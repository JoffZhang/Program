package com.zy.io.netty.multi_protocol.http_xml.example;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * @Author : ZhangYun
 * @Description :应答消息
 * @Date :  2017/7/31.
 */
//它包含两个成员变量：FullHttpResponse和Object，Object就是业务需要发送的应答POJO对象。
public class HttpXmlResponse {
    private FullHttpResponse httpResponse;
    private Object result;//业务需要发送的应答POJO

    public HttpXmlResponse(FullHttpResponse httpResponse, Object result) {
        this.httpResponse = httpResponse;
        this.result = result;
    }

    public FullHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public void setHttpResponse(FullHttpResponse httpResponse) {
        this.httpResponse = httpResponse;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
