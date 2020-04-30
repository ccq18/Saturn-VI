package ccq18.saturn.vi.gateway;

import lombok.extern.slf4j.Slf4j;
import ccq18.saturn.vi.HttpUtil;
import ccq18.saturn.vi.model.Request;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
 * 该类为多线程类，用于服务端
 */
@Slf4j
public class GatewayThread implements Runnable {

    private Socket client ;
    private Socket server ;
    private String host ;

    public GatewayThread(String host, Socket server,Socket client) {
        this.host = host;
        this.server = server;
        this.client = client;

    }

    @Override
    public void run() {

        OutputStream out = null;
        try {
            out = client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = client.getInputStream();
            //读取客户端request
            Request request = HttpUtil.readRequest(is);
            log.info("request:{}", request);
            if (request.getHeaders().size() <= 0) {
                return;
            }
            sendtoserver(request, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("request end ");
            try {
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //    String http, Map<String, String> headers, String contents
    private void sendtoserver(Request request, OutputStream clientout) throws IOException {

        Map<String, String> reqHeaders = request.getHeaders();
        String reqContents = request.getContent();

        request.getHeaders().put("Host", host);

        request.getHeaders().remove("If-None-Match");
        // todo 304 Not Modified  处理
        //todo 由于请求结束未关闭导致有一直被阻塞的问题
        //todo head 请求处理
        server.setSoTimeout(1000);

        OutputStream serverout = server.getOutputStream();

        //发送数据到服务端
        log.info("send request to sever");
        HttpUtil.printheader(serverout, request.getHttp(), reqHeaders);
        //get request
        HttpUtil.println(serverout, "");
        if (reqContents != null) {
            HttpUtil.println(serverout, reqContents);
        }
        log.info("get response from sever");
        InputStream is = server.getInputStream();
        log.info("get header from sever");
        //处理返回请求头
        InputStream serverIs = server.getInputStream();
        String respHttp = HttpUtil.readLine(serverIs);
        List<String> respHeaderStr = HttpUtil.readHeaderStr(serverIs);
        Map<String, String> respHeader = HttpUtil.parseHeaderStr(respHeaderStr);
        //返回请求头到前端
        log.info("get header end");
        log.info("send respHeader ,{},{}", respHttp, respHeaderStr);
        HttpUtil.printheader(clientout, respHttp, respHeader);
        log.info("send respHeader end");
//        ParseBody pb =   new ParseBody();
//        pb.parse(serverIs,clientout,respHeader);
        String acceptEncoding = respHeader.get("Content-Encoding");
        if (acceptEncoding != null && acceptEncoding.contains("gzip")) {
            ParseGzip pg = new ParseGzip();
            pg.readGzipbody(serverIs, clientout);
//            List<Byte> readdata =pg.getReaddata();
            //todo
//            log.info("readbytes :{}", readdata.size());
//            for (byte readbyte : readdata
//            ) {
//                clientout.write(readbyte);
//            }
//            log.info("print end :{}", readdata.size());
        } else {
            log.info("no gzip");
            ParseBody pb = new ParseBody();
            pb.parse(serverIs, clientout, respHeader);
        }


        if (server != null) {
            //如果构造函数建立起了连接，则关闭套接字，如果没有建立起连接，自然不用关闭
            server.close(); //只关闭socket，其关联的输入输出流也会被关闭
        }
    }


}