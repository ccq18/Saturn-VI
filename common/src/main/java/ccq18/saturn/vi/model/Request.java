package ccq18.saturn.vi.model;

import lombok.Data;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Request {
    String method;
    String uri;
    String http;
    String httpversion;
    Map<String, String> headers;
    String content;

    public Request(String http, Map<String, String> headers, String content) {
        this.headers = headers;
        this.content = content;
        this.http = http;
        Pattern r = Pattern.compile("(.*?)\\s+(.*?)\\s+HTTP/(.*)");
        Matcher m = r.matcher(http);
        if (m.find()) {
            this.method = m.group(1);
            this.uri = m.group(2);
            this.httpversion = m.group(3);
        }else {

        }
    }


}
