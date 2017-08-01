package com.zy.io.four_kinds_of_io.aio.time_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/18.
 */
public class AsyncTimeServerHandler implements Runnable{

    private int port;
    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        this.port = port;
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();//创建一个异步服务端通道
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));//绑定监听端口
            System.out.println("The time_server server is start in port : "+ port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        latch = new CountDownLatch(1);//初始化CountDownLatch对象，它作用是在完成一组正在执行的操作之前，允许当前的线程一直阻塞。
        doAccept();//用于接受客户端的连接
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doAccept() {
        asynchronousServerSocketChannel.accept(this,new AcceptCompletionHandler());
        //由于是异步操作，我们传递一个    CompletionHandler<AsynchronoursSocketChannel,? supper A>类型的handler实例来接收accept操作成功的通知消息。
    }
}
