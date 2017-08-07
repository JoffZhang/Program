/**
 * @Author : ZhangYun
 * @Description :握手成功之后,由客户端主动发送心跳,服务端接收到新条消息后,返回心跳应答.由于心跳消息的目的是为了检测链路的可用性,因此不需要携带消息体.
 * @Date :  2017/8/2.
 */
package com.zy.io.netty.multi_protocol.protocol_stack.heartbeat;