package ccq18.saturn.vi.sever.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface RequestMapping {
    //导出时对应的表头名

    RequestMethod[] method() default {};
    String[] path() default {};



}
