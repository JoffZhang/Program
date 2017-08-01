package com.zy.io.four_kinds_of_io.bio.time_server;

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

            while(true){
                socket = server.accept();
                Thread thread = new Thread(new TimeServerHandler(socket));
                thread.start();
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
