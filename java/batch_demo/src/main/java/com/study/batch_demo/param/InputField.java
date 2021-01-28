package com.study.batch_demo.param;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/26 11:15
 */
public class InputField {
    private String fieldName;
    private Integer fileLocation;
    private String fieldType;

    public InputField(String fieldName, Integer fileLocation, String fieldType) {
        this.fieldName = fieldName;
        this.fileLocation = fileLocation;
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(Integer fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }
}
