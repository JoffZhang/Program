package com.zy.io.four_kinds_of_io.nio.time_server;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/18.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length >0){
            try{
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){}
        }
        new Thread(new TimeClientHandle("localhost",port),"TimeClient-001").start();
    }
}
