package ccq18.saturn.vi.demo3.annos;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface Mapping {
    //导出时对应的表头名

    RequestMethod[] method() default {};
    String[] path() default {};



}
