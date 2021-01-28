package com.study.batch_demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/22 09:10
 */
// @Entity
// @Table(name="test")
public class Test {
    @Id
    @GeneratedValue
    private Integer id;
    private String code;
    private float count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public Test() {
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", count=" + count +
                '}';
    }
}
