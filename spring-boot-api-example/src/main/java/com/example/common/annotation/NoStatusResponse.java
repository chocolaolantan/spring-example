package com.example.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Responseに処理結果を付与する必要がないControllerMethodに付与する.
 * @author chocolaolantan
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface NoStatusResponse
{
}
