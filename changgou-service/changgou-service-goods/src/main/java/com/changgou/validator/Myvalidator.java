package com.changgou.validator;

import com.changgou.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *自定义数据校验逻辑实现类
 */
public class Myvalidator implements ConstraintValidator<MyConstraint,Object> {

    @Autowired
    BrandService brandService;
    /**
     * 做一些初始化操作
     * @param constraintAnnotation
     */
    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        System.out.println("my validator init");
    }

    /**
     *
     * @param value: 参与校验的字段
     * @param constraintValidatorContext
     * @return
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        //在这里调用brandService 查询数据库进行权限校验
        return false;
    }
}
