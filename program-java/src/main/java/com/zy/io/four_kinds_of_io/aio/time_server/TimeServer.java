package com.zy.io.four_kinds_of_io.aio.time_server;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/18.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port= 8080;
        if(args!= null && args.length >0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){

            }
        }
        //创建异步时间服务器处理类
        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        //启动线程将AsyncTimeServerHandler拉起
        new Thread(timeServer,"AIO-AsyncTimeServerHandler-001").start();
    }
}
