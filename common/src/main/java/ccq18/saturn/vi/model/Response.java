package ccq18.saturn.vi.model;

import ccq18.saturn.vi.HttpStatus;
import lombok.Data;

import java.util.Map;

@Data
public class Response {
    private String content;
    private HttpStatus httpStatus;

    public Response(HttpStatus httpStatus, String content) {
        this.httpStatus = httpStatus;
        this.content = content;
    }

    public String[] getResp() {
        return new String[]{
                "HTTP/1.1" + httpStatus.getValue() +""+ httpStatus.getReasonPhrase(),
                "Content-Type: text/html; charset=utf-8",
                "Content-Length: " + content.getBytes().length,
                "",
                content,
        };
    }
}
