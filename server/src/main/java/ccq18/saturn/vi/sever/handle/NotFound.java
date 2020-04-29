package ccq18.saturn.vi.sever.handle;

import ccq18.saturn.vi.model.Request;

public class NotFound implements ReqHandle {

    @Override
    public String handle(Request request) {
        return "404 not found";
    }
}
