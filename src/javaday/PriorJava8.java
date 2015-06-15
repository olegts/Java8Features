package javaday;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Oleg Tsal-Tsalko
 */
@Retention(RetentionPolicy.SOURCE)
public @interface PriorJava8 {
    String value() default "";
}
