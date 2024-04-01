package com.leslie.socialink.network.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ResponseHandler {
    Class cls() default MinaResponse.class;

    Class handler() default MinaHandler.class;
}
