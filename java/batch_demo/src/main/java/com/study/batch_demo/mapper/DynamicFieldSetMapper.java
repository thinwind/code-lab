package com.study.batch_demo.mapper;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.util.Map;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/26 17:10
 */
public class DynamicFieldSetMapper implements FieldSetMapper {
    @Override
    public Object mapFieldSet(FieldSet fieldSet) throws BindException {
        return fieldSet;
    }
}
