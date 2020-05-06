package ccq18.saturn.vi.sever;


import ccq18.saturn.vi.model.Request;
import ccq18.saturn.vi.model.Response;

import java.lang.reflect.InvocationTargetException;

public interface RequestHandle {
    public Object handle(Request request) throws InvocationTargetException, IllegalAccessException;
}
