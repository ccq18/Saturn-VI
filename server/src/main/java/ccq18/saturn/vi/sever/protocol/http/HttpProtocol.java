package ccq18.saturn.vi.sever.protocol.http;

import ccq18.saturn.vi.HttpStatus;
import ccq18.saturn.vi.HttpUtil;
import ccq18.saturn.vi.MediaType;
import ccq18.saturn.vi.model.Request;
import ccq18.saturn.vi.model.Response;
import ccq18.saturn.vi.sever.RequestHandle;
import ccq18.saturn.vi.sever.protocol.Protocol;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

@Slf4j
public class HttpProtocol implements Protocol {
    private RequestHandle requestHandle;

    public HttpProtocol(RequestHandle requestHandle) {
        this.requestHandle = requestHandle;

    }

    public void handle(OutputStream outputStream, InputStream inputStream) {
        PrintStream out = null;
        out = new PrintStream(outputStream);
        try {
            //读取请求头
            Request request = HttpUtil.readRequest(inputStream);
            log.info("request:{}", request);
            if (request.getHeaders().size() <= 0) {
                return;
            }
            Response response;
            try {
                Object content = requestHandle.handle(request);
                if (content instanceof String) {
                    response = new Response(HttpStatus.OK, MediaType.TEXT_HTML_UTF8, (String) content);
                } else if (content instanceof Response) {
                    response = (Response) content;
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
        }
    }
}
