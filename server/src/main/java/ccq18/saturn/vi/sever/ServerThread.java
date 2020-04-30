package ccq18.saturn.vi.sever;

import ccq18.saturn.vi.HttpStatus;
import ccq18.saturn.vi.HttpUtil;
import ccq18.saturn.vi.MediaType;
import ccq18.saturn.vi.model.Response;
import ccq18.saturn.vi.sever.annotation.RequestMapping;
import ccq18.saturn.vi.sever.annotation.RequestMethod;
import ccq18.saturn.vi.model.Request;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.List;
import java.util.regex.Pattern;

// nc ip port

/**
 * 该类为多线程类，用于服务端
 */
@Slf4j
public class ServerThread implements Runnable {

    private Socket client = null;
    private List<UriHandle> uriHandles;
    private UriHandle notfoundHandle;

    public ServerThread(Socket client, List<UriHandle> uriHandles,UriHandle notfoundHandle) {
        this.client = client;
        this.uriHandles = uriHandles;
        this.notfoundHandle = notfoundHandle;
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
                UriHandle reqhandle = this.match(request);
                log.info("reqhandle:{}", reqhandle);
                Object content = reqhandle.getMethod().invoke(reqhandle.getClazz(), request);
                if (content instanceof String) {
                    response = new Response(HttpStatus.OK, MediaType.TEXT_HTML_UTF8, (String) content);
                } else if (content instanceof Response) {
                    response = (Response)content;
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(content);
                    response = new Response(HttpStatus.OK, MediaType.APPLICATION_JSON_UTF8, json);
                }


            } catch (Exception e) {
                response = new Response(HttpStatus.INTERNAL_SERVER_ERROR, MediaType.TEXT_HTML_UTF8, e.getMessage());
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

    public UriHandle match(Request request)  {
        for (UriHandle uriHandle : uriHandles) {
            RequestMapping requestMapping = uriHandle.getMapping();
            for (String path : requestMapping.path()) {
                log.info("uri:{},path:{}", request.getUri(), path);
                if (Pattern.matches(path, request.getUri())) {
                    for (RequestMethod method : requestMapping.method()) {
                        if (method.toString().equals(request.getMethod())) {
                            return uriHandle;
                        }
                    }
                }
            }
        }
        return  this.notfoundHandle;
    }
}