package com.example.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;


public class DataCheck {
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private DataCheck() {
    }

    /**
     * 校验数据(抛出异常)
     *
     * @param t 数据
     */
    public static <T> void valid(T t) {
        List<String> list = validMessage(t);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new RuntimeException(StrUtil.join(",", list));
        }
    }

    /**
     * 校验数据(抛出异常)
     *
     * @param t      数据
     * @param groups 校验分组
     */
    public static <T> void valid(T t, Class<?>... groups) {
        List<String> list = validMessage(t, groups);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new RuntimeException(StrUtil.join(",", list));
        }
    }

    /**
     * 校验数据
     * 返回所有异常信息
     *
     * @param t 数据
     * @return 所有异常信息
     */
    public static <T> List<String> validMessage(T t) {
        List<String> list = new ArrayList<>();
        validator.validate(t, Default.class).forEach(v -> {
            list.add(v.getMessage());
        });
        return list;
    }

    /**
     * 校验数据
     * 返回所有异常信息
     *
     * @param t      数据
     * @param groups 校验分组
     * @return 所有异常信息
     */
    public static <T> List<String> validMessage(T t, Class<?>... groups) {
        List<String> list = new ArrayList<>();
        validator.validate(t, groups).forEach(v -> {
            list.add(v.getMessage());
        });
        return list;
    }

    /**
     * 是否校验通过
     *
     * @param t 数据
     * @return 是否通过
     */
    public static <T> boolean validBool(T t) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, Default.class);
        return violationSet.size() == 0;
    }

    /**
     * 是否校验通过
     *
     * @param t      数据
     * @param groups 校验分组
     * @return 是否通过
     */
    public static <T> boolean validBool(T t, Class<?>... groups) {
        Set<ConstraintViolation<T>> violationSet = validator.validate(t, groups);
        return violationSet.size() == 0;
    }

    /**
     * 校验数据是否全部b不为空
     *
     * @param data 需要校验的对象
     * @return 是否都不为空
     */
    public static boolean checkNotNull(Object data) {
        AtomicLong total = new AtomicLong();
        Map<String, Object> map = BeanUtil.beanToMap(data);
        map.forEach((s, o) -> {
            if (o == null) {
                total.getAndIncrement();
            }
        });
        return total.get() == 0;
    }

    /**
     * 校验数据是否全部b不为空
     *
     * @param data 需要校验的对象
     * @param num  不为空的属性个数
     * @return 判断不为空的属性是否大于等于total
     */
    public static boolean check(Object data, int num) {
        AtomicLong total = new AtomicLong();
        Map<String, Object> map = BeanUtil.beanToMap(data);
        map.forEach((s, o) -> {
            if (o != null) {
                total.getAndIncrement();
            }
        });

        return total.get() >= num;
    }

}
