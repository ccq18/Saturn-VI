package ccq18.saturn.vi.demo3;

import ccq18.saturn.vi.demo3.annos.*;
import ccq18.saturn.vi.reflect.ScanPackage;
import ccq18.saturn.vi.reflect.ScanedClass;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Singleton
@Slf4j
public class Sample {

    @Inject
    private HelloPrinter printer;


    public void hello() {

        printer.print();
    }

    public static void main(String[] args) throws IOException {


        log.info("start");
        log.info("end");
        Map<Class, List<ScanedClass>> scanedMap = ScanPackage.scanAnnotation("ccq18.saturn.vi.demo3",  new Class[]{
                Controller.class,
                Controller2.class,
                Controller3.class,
                Controller4.class,
                Controller5.class,
                Controller6.class,
                Mapping.class,
        });
        log.info("end{}",scanedMap.get(Controller.class).size());
        log.info("end{}",scanedMap.size());

    }



}