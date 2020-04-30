package ccq18.saturn.vi.demo3;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import javax.inject.Singleton;

//import com.google.inject.Guice;
//import com.google.inject.Inject;
//import com.google.inject.Injector;

@Singleton
class HelloPrinter {
    @Inject
    private Demo1 demo1;
    @Inject
    @Named("config.aaa")
    private String aa;

    public void print() {
        System.out.println(aa);
        demo1.print();
        System.out.println("Hello, World");
    }

}