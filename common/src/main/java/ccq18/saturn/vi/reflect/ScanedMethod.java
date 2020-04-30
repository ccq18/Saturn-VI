package ccq18.saturn.vi.reflect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class ScanedMethod {
    Method method;
    Annotation annotation;
}
