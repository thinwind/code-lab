package com.study.batch_demo.param;

import java.util.List;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/26 11:13
 */
public class InputTemplate {
    private String templateName;
    private String sourceType;
    private String sourceName;
    private List<InputField> fields;
    private int fieldCount;

    public InputTemplate(String templateName, String sourceType, 
        String sourceName, List<InputField> fields,int fieldCount) {
        this.templateName = templateName;
        this.sourceType = sourceType;
        this.sourceName = sourceName;
        this.fields = fields;
        this.fieldCount = fieldCount;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public List<InputField> getFields() {
        return fields;
    }

    public void setFields(List<InputField> fields) {
        this.fields = fields;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }
    
}
