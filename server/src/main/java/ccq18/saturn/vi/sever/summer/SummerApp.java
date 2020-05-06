package ccq18.saturn.vi.sever.summer;

import ccq18.saturn.vi.reflect.ScanPackage;
import ccq18.saturn.vi.reflect.ScanedClass;
import ccq18.saturn.vi.reflect.ScanedMethod;
import ccq18.saturn.vi.sever.RequestHandle;
import ccq18.saturn.vi.sever.Server;
import ccq18.saturn.vi.sever.UriHandle;
import ccq18.saturn.vi.sever.summer.annotation.Controller;
import ccq18.saturn.vi.sever.summer.annotation.RequestMapping;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SummerApp {
    static Injector injector = Guice.createInjector(new ConfigModule());

    public static void main(String[] args) throws Exception {
        int port = 2006;
        int nThreads = 20;//线程池
        run(port, nThreads, "ccq18.saturn.vi.sever.controller", null);
    }

    public static boolean run(int port, int nThreads, String packageName, Class notFound) throws Exception {

        Object notfoundHandle = injector.getInstance(notFound);
        if (notfoundHandle instanceof RequestHandle) {
            log.error("notFound 必须是RequestHandle");
        }
        List<UriHandle> uriHandles = scanController(packageName);


        return Server.run(port, nThreads, new RequestHandleIml(uriHandles, (RequestHandle) notfoundHandle));
    }

    public static List<UriHandle> scanController(String packageName) throws Exception {
        List<ScanedClass> classes = ScanPackage.scanAnnotation(packageName, Controller.class);
        List<UriHandle> uriHandles = new ArrayList();
        for (ScanedClass scanedClass : classes) {
            log.info("class:{}", scanedClass.getScanedClass());
            List<ScanedMethod> scanedMethods = scanedClass.getMethodsByAnnotion(RequestMapping.class);

            Object clazz = injector.getInstance(scanedClass.getScanedClass());
            log.info("clazz:{}", clazz);
            for (ScanedMethod scanedMethod : scanedMethods) {
                log.info("method:{}", scanedMethod.getMethod().getName());
                RequestMapping mapping = (RequestMapping) scanedMethod.getAnnotation();
                Method method = scanedMethod.getMethod();
                log.info("{},{},path:{}", clazz, mapping.method(), mapping.path());
                uriHandles.add(new UriHandle(mapping, method, clazz));

            }
        }
        return uriHandles;
    }
}
