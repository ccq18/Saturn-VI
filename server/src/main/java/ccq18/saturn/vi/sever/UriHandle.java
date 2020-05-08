package ccq18.saturn.vi.sever;

import ccq18.saturn.vi.sever.framework.annotation.RequestMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;
@Data
@AllArgsConstructor
public class UriHandle {
    private RequestMapping mapping;
    private Method method;
    private Object clazz;
}
