package com.study.batch_demo.param;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/26 11:15
 */
public class InputField {
    private String fieldName;
    private Integer fileLocation;
    private String fieldType;
    private boolean key;

    public InputField(String fieldName, Integer fileLocation, String fieldType,boolean key) {
        this.fieldName = fieldName;
        this.fileLocation = fileLocation;
        this.fieldType = fieldType;
        this.key = key;
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

    public boolean isKey() {
        return key;
    }

    public void setKey(boolean key) {
        this.key = key;
    }
}
