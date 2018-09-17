package com.gentriolee.socialgo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by gentriolee
 */

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Unsupported {

    /**
     * 暂不支持
     */
}
