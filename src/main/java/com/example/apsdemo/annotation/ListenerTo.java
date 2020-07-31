package com.example.apsdemo.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ListenerTo {
    String path() default "";
    String type() default "";
}
