package ccq18.saturn.vi.sever.framework.annotation;

import ccq18.saturn.vi.sever.RequestMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(
        method = {RequestMethod.GET}
)
public @interface GetMapping {
    //导出时对应的表头名
    String[] path() default {};

}
