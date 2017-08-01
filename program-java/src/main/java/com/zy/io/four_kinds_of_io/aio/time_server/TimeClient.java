package com.zy.io.four_kinds_of_io.aio.time_server;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/10.
 */
public class TimeClient {

    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length >0 ){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //
            }
        }
       new Thread(new AsyncTimeClientHandler("localhost",port),"AIO-AsyncTimeClientHandler-001").start();
        //实际项目中，我们不需要独立的创建异步连接对象，因为底层都是通过JDK的系统回调实现的。
    }
}
