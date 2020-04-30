package ccq18.saturn.vi.sever;


import ccq18.saturn.vi.ThreadPool;
import ccq18.saturn.vi.model.Request;
import ccq18.saturn.vi.reflect.ScanPackage;
import ccq18.saturn.vi.reflect.ScanedClass;
import ccq18.saturn.vi.reflect.ScanedMethod;
import ccq18.saturn.vi.sever.annotation.Controller;
import ccq18.saturn.vi.sever.annotation.RequestMapping;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class Server {

    static Injector injector = Guice.createInjector(new ConfigModule());


    public static void main(String[] args) throws Exception {
        int port = 2006;
        int nThreads = 20;//线程池
        run(port, nThreads, "ccq18.saturn.vi.sever.controller",null);
    }

    public static void run(int port, int nThreads, String packageName,Class notFound) throws Exception {

        Object object = Server.injector.getInstance(notFound);
        if(object instanceof RequestHandle){
            log.error("notFound 必须是RequestHandle");
        }
        Method method  = notFound.getMethod("handle", Request.class);
        UriHandle notfoundHandle =  new UriHandle(null, method, object);
//        Method[] methods = notFound.getMethods();
//        Method method = null;
//        for (Method md : methods) {
//            if (md.getName().equals("handle")) {
//                method = md;
//            }
//        }



        List<UriHandle> uriHandles = scanController(packageName);
        // 服务端口监听客户端请求的TCP连接
        ServerSocket server = new ServerSocket(port);
        ExecutorService newThreadPool = ThreadPool.newThreadPool(nThreads);

        log.info("sever port:{}", port);
        Socket client = null;
        boolean f = true;
        while (f) {
            //等待客户端的连接，如果没有获取连接
            client = server.accept();
            log.info("client connect");
            log.info("client:{}", client.getInetAddress().getHostAddress());
            //为每个客户端连接开启一个线程
            newThreadPool.execute(new ServerThread(client, uriHandles, notfoundHandle));
        }
        server.close();
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