package com.zy.io.netty.multi_protocol.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import javax.activation.MimetypesFileTypeMap;

import static io.netty.handler.codec.http.HttpHeaders.*;
import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpMethod.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

/**
 * @Author : ZhangYun
 * @Description :
 * @Date :  2017/7/27.
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private String localDir;
    public HttpFileServerHandler(String localDir) {
        this.localDir = localDir;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        if(!fullHttpRequest.getDecoderResult().isSuccess()){
            sendError(channelHandlerContext,BAD_REQUEST);
            return;
        }
        if(fullHttpRequest.getMethod() != GET){
            sendError(channelHandlerContext,METHOD_NOT_ALLOWED);
        }
        final String uri = fullHttpRequest.getUri();
        final String path = sanitizeUri(uri);
        if(path == null){
            sendError(channelHandlerContext,FORBIDDEN);
            return;
        }
        File file = new File(path);
        if(file.isHidden() || !file.exists()){
            sendError(channelHandlerContext,NOT_FOUND);
            return;
        }
        if(file.isDirectory()){
            sendDirListToClient(channelHandlerContext,file,uri);
            return;
        }
        //文件合法性判断
        if(!file.isFile()){
            sendError(channelHandlerContext,FORBIDDEN);
            return;
        }
        if(file.isFile()){
            sendFileToClient(channelHandlerContext,fullHttpRequest,file,uri);
        }
        channelHandlerContext.close();


    }

    private void sendFileToClient(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest, File file, String uri) throws IOException {
        ByteBuf buffer = Unpooled.copiedBuffer(Files.readAllBytes(file.toPath()));
        /**
         * 注：直接将文件的字节流写到FullHttpResponse中，在文件不大的情况下可以这样做，在文件比较大的情况下这样做很容易出现内存溢出之类的问题。需要考虑使用http trunked协议机制来传输大文件
         */
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1,OK,buffer);
        MimetypesFileTypeMap mimeTypeMap = new MimetypesFileTypeMap();
        resp.headers().set(CONTENT_TYPE,mimeTypeMap.getContentType(file));
        channelHandlerContext.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);

        /**随机文件读写类
         RandomAccessFile randomAccessFile = null;
         try {
         randomAccessFile = new RandomAccessFile(file,"r");//以只读方式打开文件
         }catch (FileNotFoundException e){
         sendError(channelHandlerContext,NOT_FOUND);
         return;
         }
         long fileLength = randomAccessFile.length(); //获取文件长度
         HttpResponse response = new DefaultHttpResponse(HTTP_1_1,OK);  //构造成功的应答消息。
         setContentLength(response,fileLength);//在消息头设置contentLength和contentType .
         setContentTypeHeader(response,file);
         if(isKeepAlive(fullHttpRequest)){//判断是否Keep-Alive
         response.headers().set(CONNECTION, Values.KEEP_ALIVE);
         }
         channelHandlerContext.write(response);
         ChannelFuture sendFileFuture;
         //通过Netty的ChunkedFile 对象直接将文件写入到发送缓冲区中。
         sendFileFuture= channelHandlerContext.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), channelHandlerContext.newProgressivePromise());
         //添加GenericFutureListener.
         sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
        @Override
        public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long l, long l1) throws Exception {

        }

        @Override
        public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {

        }
        });
         */
        //如果使用chunked编码，最后需要发送一个编码结束的空消息体，将LastHttpContent的EMPTY_LAST_CONTENT发送到缓冲区中，标识所有的消息体已经发送完毕，同时调用flush方法将之前在发送缓冲区的消息刷新到SocketChannel中发送给对方。
        //如果是非Keep-Alive的，最后一包消息发送完成之后，服务端要主动关闭链接。
    }

    private void sendDirListToClient(ChannelHandlerContext channelHandlerContext, File dir, String uri) throws IOException {
        StringBuffer sb=new StringBuffer("");
        String dirpath=dir.getPath();
        sb.append("<!DOCTYPE HTML>"+CRLF);
        sb.append("<html><head><title>");
        sb.append(dirpath);
        sb.append("目录：");
        sb.append("</title></head><body>"+CRLF);
        sb.append("<h3>");
        sb.append("当前目录:"+dirpath);
        sb.append("</h3>");
        sb.append("<table>");
        sb.append("<tr><td colspan='3'>上一级:<a href=\"../\">..</a>  </td></tr>");
        if(uri.equals("/")){
            uri="";
        }else
        {
            if(uri.charAt(0)=='/'){
                uri=uri.substring(0);
            }
            if(!uri.endsWith("/"))
                uri+="/";
        }

        String fnameShow;
        for (File f:dir.listFiles()) {
            if(f.isHidden()||!f.canRead()){
                continue;
            }
            String fname=f.getName();
            Calendar cal=Calendar.getInstance();
            cal.setTimeInMillis(f.lastModified());
            String lastModified=sdf.format(cal.getTime());
            sb.append("<tr>");
            if(f.isFile()){
                fnameShow="<font color='green'>"+fname+"</font>";
            }else
            {
                fnameShow="<font color='red'>"+fname+"</font>";
            }
            sb.append("<td style='width:200px'> "+lastModified+"</td><td style='width:100px'>"+Files.size(f.toPath())+"</td><td><a href=\""+uri+fname+"\">"+fnameShow+"</a></td>");
            sb.append("</tr>");

        }
        sb.append("</table>");
        ByteBuf buffer=Unpooled.copiedBuffer(sb.toString(),CharsetUtil.UTF_8);
        FullHttpResponse resp=new DefaultFullHttpResponse(HTTP_1_1,OK,buffer);
        resp.headers().set(CONTENT_TYPE, "text/html;charset=utf-8");
        channelHandlerContext.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private static final String CRLF = "\r\n";
    private void sendError(ChannelHandlerContext channelHandlerContext, HttpResponseStatus status) throws UnsupportedEncodingException {
        ByteBuf buffer = Unpooled.copiedBuffer(("系统服务出错："+status.toString()+CRLF).getBytes("UTF-8"));
        FullHttpResponse resp = new DefaultFullHttpResponse(HTTP_1_1,status,buffer);
        resp.headers().set(CONTENT_TYPE,"text/html;charset=utf-8");
        channelHandlerContext.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private void setContentTypeHeader(HttpResponse response, File file) {
    }

    /**
     * URI校验
     * @param uri
     * @return
     */
    private String sanitizeUri(String uri) {
        try{
            uri = URLDecoder.decode(uri,"UTF-8");//对URL进行解码
        } catch (UnsupportedEncodingException e) {
            try{
                uri = URLDecoder.decode(uri,"ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        //合法性判断
        uri = uri.replace('/',File.separatorChar);
        if(uri.contains(File.separator+'.')
                || uri.contains('.'+File.separator) || uri.startsWith(".")
                || uri.endsWith(".") ){//|| INSECURE_URI.matcher(uri).matches()
            return null;
        }
        return System.getProperty("user.dir") + File.separator+uri;
    }

    private static final Pattern ALLOW_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
    private static SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");


    private static void sendListing(ChannelHandlerContext ctx,File dir){
        //构造响应消息体
        StringBuilder buf = new StringBuilder();
        String dirpath=dir.getPath();
        buf.append("<!DOCTYPE HTML>"+CRLF);
        buf.append("<html><head><title>");
        buf.append(dirpath);
        buf.append("目录：");
        buf.append("</title></head><body>"+CRLF);
        buf.append("<h3>");
        buf.append("当前目录:"+dirpath);
        buf.append("</h3>");
        buf.append("<ul>");
        buf.append("<li>链接：<a href=\"../\">..</a></li>"+CRLF);
        for(File f : dir.listFiles()){
            if(f.isHidden() || !f.canRead()){
                continue;
            }
            String fname = f.getName();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(f.lastModified());
            String lastModified = sdf.format(cal.getTime());
            if(f.isFile()){
                buf.append("<li style='color:rad'>链接：<a href=\""+f.getPath()+fname+">"+lastModified+"---"+fname+"</a>");
            }else{
                buf.append("<li style='color:yellow'>链接：<a href=\""+f.getPath()+fname+">"+lastModified+"---"+fname+"</a>");
            }
        }
        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);//分配对应消息的缓冲对象
        //创建成功的HTTP响应消息
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,OK);
        //设置消息头的类型
        response.headers().set(CONTENT_TYPE,"text/html;charset=UTF-8");
        //将缓冲区的响应消息放到HTTP的应答消息中
        response.content().writeBytes(buffer);
        //释放缓冲股权。
        buffer.release();
        //将相应消息发送到缓冲区并刷新到SocketChannel中
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

}
