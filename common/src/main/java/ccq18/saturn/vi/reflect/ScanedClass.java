package ccq18.saturn.vi.reflect;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ScanedClass {
    private Class scanedClass;
    private Annotation[] annotations;


    public List<ScanedMethod> getMethodsByAnnotion(Class annotation) {
        return ScanPackage.getMethodsByAnnotion(scanedClass,annotation);

    }



    public Annotation getAnnotionByType(Class annotation) {
        return ScanPackage.getAnnotionByType(scanedClass,annotation);
    }

    public List<ScanedField> getFieldsByAnnotion(Class annotation) {
        return ScanPackage.getFieldsByAnnotion(scanedClass,annotation);
    }
}
