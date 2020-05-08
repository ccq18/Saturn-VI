package ccq18.saturn.vi.sever.framework;

import ccq18.saturn.vi.model.Request;
import ccq18.saturn.vi.sever.RequestHandle;
import ccq18.saturn.vi.sever.RequestMethod;
import ccq18.saturn.vi.sever.UriHandle;
import ccq18.saturn.vi.sever.framework.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class RequestHandleIml implements RequestHandle {

    private List<UriHandle> uriHandles;
    private RequestHandle notfoundHandle;

    public RequestHandleIml(List<UriHandle> uriHandles, RequestHandle notfoundHandle) {
        this.uriHandles = uriHandles;
        this.notfoundHandle = notfoundHandle;
    }

    public UriHandle match(Request request) {
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
        return null;
    }

    @Override
    public Object handle(Request request) throws InvocationTargetException, IllegalAccessException {
        UriHandle reqhandle = this.match(request);
        if (reqhandle == null) {
            return this.notfoundHandle.handle(request);
        }
        log.info("reqhandle:{}", reqhandle);
        return reqhandle.getMethod().invoke(reqhandle.getClazz(), request);

    }
}
