package com.zy.io.four_kinds_of_io.nio.time_server;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/13.
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
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }
}
