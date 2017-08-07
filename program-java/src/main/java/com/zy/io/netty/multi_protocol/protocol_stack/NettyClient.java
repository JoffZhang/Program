package com.zy.io.netty.multi_protocol.protocol_stack;

import com.zy.io.netty.multi_protocol.protocol_stack.auth.LoginAuthReqHandler;
import com.zy.io.netty.multi_protocol.protocol_stack.codec.NettyMessageDecoder;
import com.zy.io.netty.multi_protocol.protocol_stack.codec.NettyMessageEncoder;
import com.zy.io.netty.multi_protocol.protocol_stack.heartbeat.HeartBeatReqHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author : ZhangYun
 * @Description :
 * 利用Netty的ChannelPipeline和ChannelHandler机制，可以非常方便地实现功能解耦和业务产品的定制。例如本例中的心跳定时器、握手请求、后端业务处理
 * 可以通过不同的handler实现，类似于AOP。通过Handler Chain的机制可以方便地实现切面拦截和定制，相比于AOP它的性能更高。
 * @Date :  2017/8/2.
 */
public class NettyClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    private void connect(int port, String host) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //利用NettyMessageDecoder对消息解码，为防止单条消息过大导致的内存溢出或者畸形码流导致的解码错位引起内存分配失败，我们对单挑消息最大长度进行了上限限制。
                            ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
                            //netty消息编码器，用于协议消息的自动编码。
                            ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                            //直接利用Netty的ReadTimeoutHandler机制实现心跳超时，当一定周期内（默认50s），没有读取到对方任何消息时，需要主动关闭链路。如果是客户端，重新发起连接；如果是服务端，释放资源，清除客户端登陆缓存信息，等待服务端重连。
                            ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                            //握手请求
                            ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                            //心跳消息
                            ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                        }
                    });
            //发起异步连接操作
            //我们绑定了本地，主要用于服务端重复登陆保护，另外，从产品管理角度，一般情况下不允许系统随便使用随机端口。
            ChannelFuture f = b.connect(new InetSocketAddress(host, port), new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();

            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //所有资源释放完成后，清空资源，再次发起重新操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        connect(NettyConstant.PORT, NettyConstant.REMOTEIP);//发起重连操作
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        new NettyClient().connect(NettyConstant.PORT, NettyConstant.REMOTEIP);
    }

}
/**
 * 在编解码前还有一个地方要注意一下，就是在client和server中书上有个坑
 * <p>
 * ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4, 4, -8, 0));
 * 简单介绍一下：第一个参数是指数据包的最大值，第二个是协议中“长度”字段的偏移地址，第三个是“长度字段”的长度，第四个是针对2和3的一个容量修正，因为源码里面会会把参数2和3加起来，会使frameLength过大而抛异常
 * （1）frameLength就是协议可读范围的窗口，值是当前消息包的字节数。
 * （2）lengthFieldEndOffset是“长度”字段的偏移量（因为这里在编解码的时候要同时把长度字段一起处理，所以要把调整长度）
 **/