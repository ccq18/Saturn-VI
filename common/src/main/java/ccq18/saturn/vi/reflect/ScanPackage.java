package ccq18.saturn.vi.reflect;

import com.google.common.reflect.ClassPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ScanPackage {
    public static List<ScanedClass> scanAnnotation(String packageName, Class annotationType) {
        List<ScanedClass> scanedClasses = new ArrayList<>();
        try {
            for (ClassPath.ClassInfo classInfo : ClassPath.from(ScanPackage.class.getClassLoader()).getTopLevelClassesRecursive(packageName)) {
                try {
                    Class clazz = classInfo.load();
                    Annotation[] annotations = clazz.getAnnotationsByType(annotationType);
                    if (annotations.length > 0) {
                        scanedClasses.add(new ScanedClass(clazz, annotations));
                    }
                } catch (Throwable e) { // Do something

                }

            }
        } catch (IOException e) {
            return null;
        }
        return scanedClasses;
    }

    public static Map<Class, List<ScanedClass>> scanAnnotation(String packageName, Class[] annotationTypes) throws IOException {
        Map<Class, List<ScanedClass>> scanedMap = new HashMap<>(annotationTypes.length);
        for (Class annotationType : annotationTypes) {
            scanedMap.put(annotationType, new ArrayList<>());
        }
        for (ClassPath.ClassInfo classInfo : ClassPath.from(ScanPackage.class.getClassLoader()).getTopLevelClassesRecursive(packageName)) {
            for (Class annotationType : annotationTypes) {
                try {
                    Class clazz = classInfo.load();
                    Annotation[] annotations = clazz.getAnnotationsByType(annotationType);
                    if (annotations.length > 0) {
                        scanedMap.get(annotationType).add(new ScanedClass(clazz, annotations));
                    }

                } catch (Throwable e) { // Do something

                }
            }

        }
        return scanedMap;
    }


    public static List<ScanedMethod> getMethodsByAnnotion(Class scanedClass, Class annotation) {
        List<ScanedMethod> methods = new ArrayList<>();
        for (Method method : scanedClass.getMethods()) {

            Annotation anno = getAnnotion(method.getAnnotations(), annotation);
            log.info("{},{}",method.getName(),anno);
            if (anno != null) {
                methods.add(new ScanedMethod(method, anno));
            }
        }
        return methods;

    }

    private static Annotation getAnnotion(Annotation[] annotations, Class annotation) {

        for (Annotation anno : annotations) {
            if (anno.annotationType().equals(annotation)) {
                return anno;
            }else  if(anno.annotationType().isAnnotationPresent(annotation)){
                Annotation anno1 = anno.annotationType().getAnnotation(annotation);

                if (anno1!=null) {

                    try {

                        PropertyUtils.copyProperties(anno,anno1);
                    } catch (Exception e) {

                    }
                    log.info("anno1:{},{}",anno,anno1.getClass().getDeclaredMethods());
                    return anno1;
                }
            }


        }
        return null;
    }




    public static Annotation getAnnotionByType(Class scanedClass, Class annotation) {
        return getAnnotion(scanedClass.getAnnotations(), annotation);
    }

    public static List<ScanedField> getFieldsByAnnotion(Class scanedClass, Class annotation) {
        List<ScanedField> fields = new ArrayList<>();
        for (Field field : scanedClass.getFields()) {
            Annotation anno = getAnnotion(field.getAnnotations(), annotation);
            if (anno != null) {
                fields.add(new ScanedField(field, anno));
            }

        }
        return fields;
    }
}
