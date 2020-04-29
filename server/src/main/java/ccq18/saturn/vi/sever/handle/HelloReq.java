package ccq18.saturn.vi.sever.handle;

import ccq18.saturn.vi.model.Request;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloReq implements ReqHandle {
    @Override
    public String handle(Request request) {
        return "<h1>hello</h1>\n" +
                "<a href=\"/\">index</a>\n"
                ;

    }
}
