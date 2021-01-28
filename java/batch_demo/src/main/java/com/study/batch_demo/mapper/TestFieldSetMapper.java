package com.study.batch_demo.mapper;

import com.study.batch_demo.entity.Test;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/22 09:09
 */
public class TestFieldSetMapper implements FieldSetMapper<Test> {
    @Override
    public Test mapFieldSet(FieldSet fieldSet) throws BindException {
        Test test = new Test();
        test.setCode(fieldSet.readString(1));
        test.setCount(fieldSet.readFloat(0));
        return test;
    }
}
