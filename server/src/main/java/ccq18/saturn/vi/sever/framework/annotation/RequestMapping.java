package ccq18.saturn.vi.sever.framework.annotation;

import ccq18.saturn.vi.sever.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    //导出时对应的表头名

    RequestMethod[] method() default {};
    String[] path() default {};



}
