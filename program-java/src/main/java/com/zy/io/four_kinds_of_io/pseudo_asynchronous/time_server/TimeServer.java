package com.zy.io.four_kinds_of_io.pseudo_asynchronous.time_server;

import com.zy.io.four_kinds_of_io.bio.time_server.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/10.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 8080;
        if(args != null && args.length >0 ){
            try {
                port = Integer.valueOf(args[0]);
            }catch (NumberFormatException e){
                //
            }
        }

        ServerSocket server = null;
        try{
            server = new ServerSocket(port);
            System.out.println("The time_server server is start in port: "+ port);
            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50,10000);//创建I/O任务线程池
            while(true){
                socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(server != null){
                System.out.println("The time_server server close");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }
    }
}
