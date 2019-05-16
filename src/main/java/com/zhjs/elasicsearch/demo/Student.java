package com.zhjs.elasicsearch.demo;

import lombok.Data;

import java.util.List;

/**
 * Created by zhjs on 2019/5/8.
 */
@Data
public class Student {

    private String id;

    private String name;

    private Integer age;

    private String sex;

    private List<String> hobbies;

}
