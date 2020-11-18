package jp.anno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StandJsonParam {
    String module() default "";
    String methods() default "";
}
