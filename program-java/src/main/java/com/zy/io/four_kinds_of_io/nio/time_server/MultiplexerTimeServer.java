package com.zy.io.four_kinds_of_io.nio.time_server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/13.
 */

public class MultiplexerTimeServer implements Runnable{

    private Selector selector;

    private ServerSocketChannel serverSocketChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     *
     * @param port
     */
    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(port));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time_server server is start in port : "+ port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        this.stop = true;
    }

    public void run() {
        while(!stop){//循环遍历selector
            try {
                selector.select(1000);//休眠时间为1s，无论是否有读写等事件发生，selector每隔1s都会被唤醒一次。
                Set<SelectionKey> keys = selector.selectedKeys();//当有处于就绪状态的Channel，selector将返回就绪状态的SeelectionKey集合
                Iterator<SelectionKey> iterator = keys.iterator();//通过对就绪状态channel集合进行迭代，可以进行网络的异步读写操作
                SelectionKey key = null;
                while(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try{
                        handleInput(key);
                    }catch (IOException e){
                        if( key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //多路复用器关闭后，所有注册在上面的channel和pipe等资源都会被自动去注册并关闭，所以不需要重复释放资源。
        if(selector != null){
            try{
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key ) throws IOException {
        if(key.isValid()){
            //处理新接入的请求消息
            if(key.isAcceptable()){//根据SelectionKey的操作位进行判断即可获知网络事件的类型
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();//通过ServerSocketChannel的accept接收客户端的连接请求并创建SocketChannel实例，完成上述，相当于完成TCP的三次握手，TCP物理链路正式建立。
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                //Read the data读取客户端请求消息

                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);//先创建一个ByteBuffer,由于事先不知客户端发送的码流大小，我们开辟一个1k的缓冲区。

                    int readBytes = sc.read(readBuffer);//读取请求码流。由于已将SocketChannel设置为异步非阻塞模式，因此它的read是非阻塞的。
                    if(readBytes > 0){//读到了字节，对字节进行编解码
                        readBuffer.flip();//flip将缓冲区当前的limit设置为position，position设置为0，用于后续对缓冲区的读写操作。
                        byte[] bytes = new byte[readBuffer.remaining()];//根据缓冲区刻度的字节数创建字节数组
                        readBuffer.get(bytes);//调用ByteBuffer的get将缓冲区刻度的字节数组复制到新创建的字节数组中，
                        String body = new String(bytes,"UTF-8");
                        System.out.println("The time_server server receive order: "+body);
                        String currentTime = "Query time_server order ".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"BAD order";
                        doWrite(sc,currentTime);
                    }else if(readBytes < 0){//链路已关闭，需要关闭SocketChannel,释放资源。
                        //对端链路关闭
                        key.cancel();
                        sc.close();
                    }else{//=0 没读到，属于正常场景，忽略
                        //读取到0字节，忽略
                    }

            }
        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException {
        if(response != null && response.trim().length()>0){//将应答消息异步发送给客户端
            byte[] bytes = response.getBytes();//将字符串编码成字节数组
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);//根据字节数组的容量创建ByteBuffer
            writeBuffer.put(bytes);//将字节数组复制到缓冲区
            writeBuffer.flip();//对缓冲区进行flip操作
            sc.write(writeBuffer);//将缓冲区中的字节数组发送出去。
            //由于SocketChannel是异步非阻塞的，它并不保证一次能够吧需要发送的字节数组发送完，会出现‘写半包’，
            // 我们需要注册写操作，不断轮询Selector将没有发送完毕的ByteBuffer发送完毕，可通过 writeBuffer.hasRemaining()
            //判断是否消息发送完毕
        }
    }
}
