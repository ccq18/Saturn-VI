package ccq18.saturn.vi.sever;

import ccq18.saturn.vi.HttpStatus;
import ccq18.saturn.vi.HttpUtil;
import ccq18.saturn.vi.model.Response;
import ccq18.saturn.vi.sever.handle.HelloReq;
import ccq18.saturn.vi.sever.handle.ReqDemo;
import ccq18.saturn.vi.sever.handle.ReqHandle;
import ccq18.saturn.vi.model.Request;
import lombok.extern.slf4j.Slf4j;
import ccq18.saturn.vi.sever.handle.NotFound;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.regex.Pattern;

// nc ip port

/**
 * 该类为多线程类，用于服务端
 */
@Slf4j
public class ServerThread implements Runnable {

    private Socket client = null;

    public ServerThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        PrintStream out = null;
        try {
            out = new PrintStream(client.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            //读取请求头
            Request request = HttpUtil.readRequest(client.getInputStream());
            log.info("request:{}", request);
            if (request.getHeaders().size() <= 0) {
                return;
            }
            Response response;
            try {
                ReqHandle reqhandle = this.match(request);
                String content = reqhandle.handle(request);
                response =new Response(HttpStatus.OK, content);
            } catch (Exception e) {
                response =new Response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
                log.info("error {}", e);
            }
            String[] resp = response.getResp();
            for (String i : resp) {
                out.println(i);
                log.info("resp:{}", i);
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ReqHandle match(Request request) {
        if (Pattern.matches("/hello", request.getUri())) {
            return new HelloReq();
        }
        if (Pattern.matches("/", request.getUri())) {
            return new ReqDemo();
        }
        return new NotFound();
    }
}