package com.zl.gaozhiliang152jianyi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @version 1.0
 * @desc:
 * @date 2021/7/21 8:30 下午
 * @auth ALLEN
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Access {
    CommonIdentifier level() default CommonIdentifier.Admin;
}

