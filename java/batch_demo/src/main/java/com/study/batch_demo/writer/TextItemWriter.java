package com.study.batch_demo.writer;

import com.study.batch_demo.param.OutputField;
import com.study.batch_demo.param.OutputTemplate;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.support.AbstractFileItemWriter;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.Map;

/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/27 15:48
 */
public class TextItemWriter extends AbstractFileItemWriter<FieldSet> {
    private String format;
    private OutputTemplate outputTemplate;

    public TextItemWriter(Resource resource, OutputTemplate outputTemplate) {
        setResource(resource);
        setExecutionContextName(ClassUtils.getShortName(TextItemWriter.class));
        setOutputTemplate(outputTemplate);
        setFormat(outputTemplate.getDetails());
    }

    public void setFormat(String format) {
        this.format = format;
    }

    private void setFormat(List<OutputField> outputFields){
        StringBuilder format = new StringBuilder();
        for (OutputField outputField : outputFields) {
            format.append("%");
            if (outputField.getAlign().equals("left")){
                format.append("-");
            }
            format.append(outputField.getLength());
            format.append("s");
        }
        this.format = format.toString();
    }

    public void setOutputTemplate(OutputTemplate outputTemplate) {
        this.outputTemplate = outputTemplate;
    }

    @Override
    protected String doWrite(List<? extends FieldSet> list) {
        StringBuilder lines = new StringBuilder();
        for (FieldSet item : list) {
            List<OutputField> outputFields = outputTemplate.getDetails();
            String[] columns = new String[outputFields.size()];
            for (int i = 0; i < outputFields.size(); i++) {
                OutputField outputField = outputFields.get(i);
                columns[i] = item.readString(outputField.getSourceFieldName());
            }
            String value = String.format(format, columns);
            lines.append(value).append(this.lineSeparator);
        }
        return lines.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.append) {
            this.shouldDeleteIfExists = false;
        }
    }
}
