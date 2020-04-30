package ccq18.saturn.vi.sever.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

}
