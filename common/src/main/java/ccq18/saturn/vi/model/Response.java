package ccq18.saturn.vi.model;

import ccq18.saturn.vi.HttpStatus;
import lombok.Data;

import java.util.Map;

@Data
public class Response {
    private String content;
    private HttpStatus httpStatus;
    private String contentType;

    public Response(HttpStatus httpStatus, String contentType, String content) {
        this.httpStatus = httpStatus;
        this.content = content;
        this.contentType = contentType;
    }

    public String[] getResp() {
        return new String[]{
                "HTTP/1.1" + httpStatus.getValue() + "" + httpStatus.getReasonPhrase(),
                "Content-Type: " + contentType,//text/html; charset=utf-8
                "Content-Length: " + content.getBytes().length,
                "",
                content,
        };
    }
}
