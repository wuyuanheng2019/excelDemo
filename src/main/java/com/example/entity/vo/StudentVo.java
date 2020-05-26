package com.example.entity.vo;


import cn.hutool.core.collection.CollectionUtil;
import com.example.util.DataCheck;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class StudentVo {

    /**
     * 姓名
     */
    @NotNull(message = "姓名不能为空")
    @Size(min = 1, message = "姓名格式不正确")
    private String name;

    /**
     * 年龄
     * regexp 传入正则表达式
     */
    //@Pattern(regexp = "",message = "年龄格式格不正确")
    private String age;

    /**
     * 钱
     */
    //@Pattern(regexp = "",message = "")
    private String mony;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 校验数据是否符合规则
     *
     * @param studentVoList 传入的校验数据
     * @return 不符合规则的队列
     */
    public static List<StudentVo> check(List<StudentVo> studentVoList) {

        //创建失败队列
        List<StudentVo> failList = Collections.synchronizedList(new ArrayList<>());

        //使用javax进行数据格式校验，并对校验通过的队列进行分组操作
        Map<String, List<StudentVo>> collect = studentVoList.parallelStream().filter(studentVo -> {
            List<String> strings = DataCheck.validMessage(studentVo);
            if (CollectionUtil.isEmpty(strings)) {
                return true;
            }
            studentVo.setMessage(StringUtils.join(strings, ","));
            failList.add(studentVo);
            return false;
        }).collect(
                //进行分组操作，业务场景：如果多个excel行属于同一个数据，那么根据规则拆分为多个list
                Collectors.groupingBy(
                        studentVo -> studentVo.getName().hashCode() + ""
                )
        );


        //根据自定义的校验规则进行校验
        collect.forEach(
                (s, List) -> {
                    //校验规则
                    //加入失败队列
                }
        );

        return failList;
    }
}
