package com.zy.io.four_kinds_of_io.aio.time_server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/19.
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {//将AsynchronousSocketChannel通过参数，当作成员变量使用。主要用于读取半包消息和发送应答。
        if(this.channel == null){
            this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {//读取到消息后的处理
        attachment.flip();//为从缓冲区读取数据做准备。
        byte[] body = new byte[attachment.remaining()];//根据缓冲区的可读字节数组创建byte
        attachment.get(body);
        try {
            String req = new String(body, "UTF-8");//创建请求消息
            System.out.println("The time server receive order : "+req);
            //对消息进行判断
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req)?new Date(System.currentTimeMillis()).toString():"BAD ORDER";
            doWrite(currentTime);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void doWrite(String currentTime) {
        if(currentTime != null && currentTime.trim().length()>0){
            byte[] bytes = currentTime.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            channel.write(writeBuffer,writeBuffer,new CompletionHandler<Integer,ByteBuffer>(){

                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    //如果没有发送完成，继续发送
                    if(buffer.hasRemaining()){
                        channel.write(buffer,buffer,this);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {

                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
//failed()  ---->   当发生异常的时候，对异常Throwable进行判断：如果I/O异常，就关闭链路，释放资源；如果是其他，按照自己业务逻辑处理。
    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
