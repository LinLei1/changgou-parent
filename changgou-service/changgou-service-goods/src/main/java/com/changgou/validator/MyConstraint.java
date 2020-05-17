package com.changgou.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义数据校验注解
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Myvalidator.class) //指定实现自定义数据校验的逻辑类
public @interface MyConstraint {
    /**
     * 想要执行数据校验,以下三个属性必须有
     * @return
     */
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
