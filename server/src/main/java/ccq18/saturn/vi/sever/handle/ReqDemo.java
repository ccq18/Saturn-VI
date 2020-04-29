package ccq18.saturn.vi.sever.handle;

import ccq18.saturn.vi.model.Request;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReqDemo implements ReqHandle {
    @Override
    public String handle(Request request) {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>index</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>this is index</h1>\n" +
                "<a href=\"/hello\">hello</a>\n" +
                "</body>\n" +
                "</html>";

    }
}
