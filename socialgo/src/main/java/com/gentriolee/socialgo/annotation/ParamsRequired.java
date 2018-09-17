package com.gentriolee.socialgo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by gentriolee
 */

@Target(ElementType.PARAMETER)
public @interface ParamsRequired {
    /**
     * 必传参数
     */
}
