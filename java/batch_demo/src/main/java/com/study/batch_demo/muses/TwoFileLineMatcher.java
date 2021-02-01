package com.study.batch_demo.muses;

import com.study.batch_demo.param.InputField;
import com.study.batch_demo.param.InputTemplate;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class TwoFileLineMatcher implements LineMatcher{

    static final String DELIMITER = ",";

    private final InputTemplate leftInputTemplate;
    private final InputTemplate righInputTemplate;

    private final DelimitedLineTokenizer leftTokenizer;
    private final DelimitedLineTokenizer rightTokenizer;
    private final String leftKeyFieldName;
    private final String rightKeyFieldName;

    public TwoFileLineMatcher(InputTemplate leftInputTemplate,
                              InputTemplate righInputTemplate) {
        this.leftInputTemplate = leftInputTemplate;
        this.righInputTemplate = righInputTemplate;
        leftTokenizer = createTokenizer(leftInputTemplate);
        rightTokenizer = createTokenizer(righInputTemplate);
        leftKeyFieldName = findKeyFieldName(leftInputTemplate);
        rightKeyFieldName = findKeyFieldName(righInputTemplate);
    }

    @Override
    public MatchResult matchLines(String[] lines) {
        MatchResult result = new MatchResult();
        String driverLine = lines[0];
        FieldSet leftFieldSet = leftTokenizer.tokenize(driverLine);
        String rightLine = lines[1];
        if (rightLine == null) {
            rightLine = createEmptyLine(righInputTemplate);
            result.setMatchedDetails(new boolean[] {false, false});
        } else {
            FieldSet rightFieldSet = rightTokenizer.tokenize(rightLine);
            String leftKey = leftFieldSet.readRawString(leftKeyFieldName);
            String rightKey = rightFieldSet.readRawString(rightKeyFieldName);
            if (Objects.equals(leftKey, rightKey)) {
                result.setMatchedDetails(new boolean[] {true, true});
            } else {
                rightLine = createEmptyLine(righInputTemplate);
                result.setMatchedDetails(new boolean[] {false, false});
            }
        }
        result.setJoinedLine(driverLine + DELIMITER + rightLine);
        return result;
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

    private String createEmptyLine(InputTemplate inputTemplate) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        for (int i = 0; i < inputTemplate.getFieldCount(); i++) {
            joiner.add(" ");
        }
        return joiner.toString();
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
