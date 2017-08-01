package com.zy.io.four_kinds_of_io.aio.time_server;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/19.
 */
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        //从attachment获取成员变量AsynchronoursServerSocketChannel,并调用其accept方法。
        /**
            为什么已经接受客户端成功，还需要再次调用accept方法？？？？？
                调用AsynchronousServerSocketChannel的accept后，如果有新的客户端连接接入，系统将调用我们传入的CompletionHandler实例的completed方法，标识新的客户端已经接入成功。
                因为一个AsynchronoursServerSocketChannel可以接收成千上万客户端，所以需要继续调用它的accept方法，接受其他的客户端链接，最终形成一个循环。每当有一个客户读连接成功后，再异步接收新的客户端连接。
         **/
        attachment.asynchronousServerSocketChannel.accept(attachment,this);
        ByteBuffer buffer = ByteBuffer.allocate(1024);//链路建立，接受客户端的请求消息。创建缓冲区1MB
        result.read(buffer,buffer,new ReadCompletionHandler(result));//通过read方法异步都操作。
        /**
            read(ByteBuffer dst, A attachment, CompletionHandler<Integer,? super A> handler)
            dst：接收缓冲区，用于从异步Channel中读取数据包
            attachment: 异步Channel携带的附件，通知回掉的时候作为入参使用；
            handler: 接收通知回调的业务Handler,本例为ReadCompletionHandler
         **/
    }


    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}
