package com.study.batch_demo.param;

import java.util.List;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/28 11:02
 */
public class OutputTemplate {
    private String templateFileName;
    private String title;
    private List<OutputField> details;

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OutputField> getDetails() {
        return details;
    }

    public void setDetails(List<OutputField> details) {
        this.details = details;
    }
}
