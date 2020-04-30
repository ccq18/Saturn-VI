package ccq18.saturn.vi.reflect;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class ScanedField {
    Field field;
    Annotation annotation;
}
