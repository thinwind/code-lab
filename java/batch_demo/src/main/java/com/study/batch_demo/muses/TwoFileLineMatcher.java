package com.study.batch_demo.muses;

import com.study.batch_demo.param.InputField;
import com.study.batch_demo.param.InputTemplate;
import org.springframework.batch.item.file.transform.DefaultFieldSetFactory;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;

import java.util.List;
import java.util.Objects;

public class TwoFileLineMatcher implements LineMatcher<FieldSet>{

    static final String DELIMITER = ",";

    private InputTemplate leftInputTemplate;
    private InputTemplate rightInputTemplate;

    private final DelimitedLineTokenizer leftTokenizer;
    private final DelimitedLineTokenizer rightTokenizer;
    private final String leftKeyFieldName;
    private final String rightKeyFieldName;

    public TwoFileLineMatcher(InputTemplate leftInputTemplate,
                              InputTemplate rightInputTemplate) {
        this.leftInputTemplate = leftInputTemplate;
        this.rightInputTemplate = rightInputTemplate;
        leftTokenizer = createTokenizer(leftInputTemplate);
        rightTokenizer = createTokenizer(rightInputTemplate);
        leftKeyFieldName = findKeyFieldName(leftInputTemplate);
        rightKeyFieldName = findKeyFieldName(rightInputTemplate);
    }

    @Override
    public MatchResult<FieldSet> matchLines(String[] lines) {
        MatchResult<FieldSet> result = new MatchResult<FieldSet>();
        FieldSet finalSet;
        String driverLine = lines[0];
        FieldSet leftFieldSet = leftTokenizer.tokenize(driverLine);
        String rightLine = lines[1];
        if (rightLine == null) {
            result.setMatchedDetails(new boolean[] {false, false});
            finalSet = joinFieldSetWithEmpty(leftFieldSet);
        } else {
            FieldSet rightFieldSet = rightTokenizer.tokenize(rightLine);
            String leftKey = leftFieldSet.readRawString(leftKeyFieldName);
            String rightKey = rightFieldSet.readRawString(rightKeyFieldName);
            if (Objects.equals(leftKey, rightKey)) {
                result.setMatchedDetails(new boolean[] {true, true});
                finalSet = joinFieldSet(leftFieldSet, rightFieldSet);
            } else {
                finalSet = joinFieldSetWithEmpty(leftFieldSet);
                result.setMatchedDetails(new boolean[] {false, false});
            }
        }
        result.setJoinedLine(finalSet);
        return result;
    }

    private FieldSet joinFieldSetWithEmpty(FieldSet leftFieldSet) {
        //初始化右文件域，用作留空
        List<InputField> fields = rightInputTemplate.getFields();
        String[] rightNames = new String[fields.size()-1];
        for (int i = 0,j=0; i < fields.size();i++) {
            if(rightKeyFieldName.equals(fields.get(i))){
                continue;
            }
            rightNames[j] = fields.get(i).getFieldName();
        }

        String[] leftNames = leftFieldSet.getNames();
        String[] names=new String[leftNames.length+rightNames.length];
        String[] rawValues = new String[names.length];
        for (int i = 0; i < leftNames.length; i++) {
            names[i] = leftNames[i];
            rawValues[i] = leftFieldSet.readRawString(leftNames[i]);
        }
        for (int i = 0,j=leftNames.length; i < rightNames.length; i++,j++) {
            names[j] = rightNames[i];
            rawValues[j] = "";
        }
        return new DefaultFieldSetFactory().create(rawValues,names);
    }


    private FieldSet joinFieldSet(FieldSet leftFieldSet, FieldSet rightFieldSet) {
        String[] leftNames = leftFieldSet.getNames();
        String[] rightNames = rightFieldSet.getNames();
        String[] names=new String[leftNames.length+rightNames.length - 1];
        String[] rawValues = new String[names.length];
        for (int i = 0; i < leftNames.length; i++) {
            names[i] = leftNames[i];
            rawValues[i] = leftFieldSet.readRawString(leftNames[i]);
        }

        for (int i = 0,j=rightNames.length; i < rightNames.length; i++) {
            if(rightKeyFieldName.equals(rightNames[i])){
                continue;
            }
            names[j] = rightNames[i];
            rawValues[j] = rightFieldSet.readRawString(rightNames[i]);
            j++;
        }
        return new DefaultFieldSetFactory().create(rawValues,names);
    }

    private DelimitedLineTokenizer createTokenizer(InputTemplate inputTemplate) {
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(DELIMITER);
        delimitedLineTokenizer.setStrict(false);

        List<InputField> fields = inputTemplate.getFields();
        String[] names = new String[fields.size()];
        int[] columns = new int[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            names[i] = fields.get(i).getFieldName();
            columns[i] = fields.get(i).getFileLocation() - 1;
        }
        delimitedLineTokenizer.setIncludedFields(columns);
        delimitedLineTokenizer.setNames(names);
        return delimitedLineTokenizer;
    }

    //FIXME 可能为null,后果未知
    private String findKeyFieldName(InputTemplate inputTemplate) {
        for (InputField field : inputTemplate.getFields()) {
            if (field.isKey()) {
                return field.getFieldName();
            }
        }
        return null;
    }
}
