/*
 * Copyright 2021 Shang Yehua
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.study.batch_demo;

import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.io.IOException;
import com.alibaba.fastjson.JSON;
import com.study.batch_demo.muses.filemerger.LineMatcher;
import com.study.batch_demo.muses.filemerger.MatchResult;
import com.study.batch_demo.muses.filemerger.MultiFileJoiner;
import com.study.batch_demo.param.InputField;
import com.study.batch_demo.param.InputTemplate;
import com.study.batch_demo.param.OutputTemplate;
import com.study.batch_demo.reader.MultiFileItemReader;
import com.study.batch_demo.writer.TextItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * TODO Tester说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-05  10:33
 *
 */
@SpringBootApplication
public class Tester {

    static final String DELIMITER = ",";

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(Tester.class, args);


        Resource temp = applicationContext.getResource("left_file_template.json");
        InputTemplate leftInputTemplate =
                JSON.parseObject(temp.getInputStream(), InputTemplate.class);

        temp = applicationContext.getResource("right_file_template.json");
        InputTemplate righInputTemplate =
                JSON.parseObject(temp.getInputStream(), InputTemplate.class);


        DelimitedLineTokenizer joinedTokenizer =
                createTokenizer(leftInputTemplate, righInputTemplate);
        LineMatcher matcher = createLineMatcher(leftInputTemplate, righInputTemplate);

        MultiFileJoiner muiltiFileJoiner =
                new MultiFileJoiner(new String[] {"input-data", "input-data2"}, matcher, "UTF-8",
                        file -> applicationContext.getResource(file).getInputStream());
                        
        ItemReader itemReader = new MultiFileItemReader(muiltiFileJoiner,joinedTokenizer);

        temp = applicationContext.getResource("outputTemplate.json");
        OutputTemplate outputTemplate =
                JSON.parseObject(temp.getInputStream(), OutputTemplate.class);

        JobBuilderFactory jobBuilderFactory = applicationContext.getBean(JobBuilderFactory.class);
        StepBuilderFactory stepBuilderFactory =
                applicationContext.getBean(StepBuilderFactory.class);

        ItemWriter textItemWriter = new TextItemWriter(
                new FileSystemResource("target/test-outputs/output1.txt"), outputTemplate);
        TaskletStep step = stepBuilderFactory.get("file2FileStep").chunk(50).reader(itemReader)
                .writer(textItemWriter).build();

        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        Job job = jobBuilderFactory.get("file2File").start(step).build();
        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis()).toJobParameters();
        try {
            JobExecution run = jobLauncher.run(job, jobParameter);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    private static DelimitedLineTokenizer createTokenizer(InputTemplate leftInputTemplate,
            InputTemplate righInputTemplate) {
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(DELIMITER);
        delimitedLineTokenizer.setStrict(false);
        List<InputField> leftFields = leftInputTemplate.getFields();
        List<InputField> rightFields = righInputTemplate.getFields();

        String[] names = new String[leftFields.size() + rightFields.size()];
        int[] columns = new int[names.length];

        for (int i = 0; i < leftFields.size(); i++) {
            names[i] = leftFields.get(i).getFieldName();
            columns[i] = leftFields.get(i).getFileLocation() - 1;
        }

        int rightFieldsIdxBase = leftFields.size();
        int rightFieldsLocBase = leftInputTemplate.getFieldCount();
        for (int i = 0; i < rightFields.size(); i++) {
            names[i + rightFieldsIdxBase] = rightFields.get(i).getFieldName();
            columns[i + rightFieldsIdxBase] =
                    rightFields.get(i).getFileLocation() + rightFieldsLocBase;
        }

        delimitedLineTokenizer.setIncludedFields(columns);
        delimitedLineTokenizer.setNames(names);
        return delimitedLineTokenizer;
    }

    private static DelimitedLineTokenizer createTokenizer(InputTemplate inputTemplate) {
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

    private static LineMatcher createLineMatcher(InputTemplate leftInputTemplate,
            InputTemplate righInputTemplate) {
        DelimitedLineTokenizer leftTokenizer = createTokenizer(leftInputTemplate);
        DelimitedLineTokenizer rightTokenizer = createTokenizer(righInputTemplate);
        String leftKeyFieldName = findKeyFieldName(leftInputTemplate);
        String rightKeyFieldName = findKeyFieldName(righInputTemplate);
        return lines -> {
            MatchResult result = new MatchResult();
            String driverLine = lines[0];
            FieldSet leftFieldSet = leftTokenizer.tokenize(driverLine);
            String rightLine = lines[1];
            if (rightLine == null) {
                rightLine = createEmptyLine(righInputTemplate);
                result.setMatchedDetails(new boolean[] {false, true});
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
        };
    }

    private static String createEmptyLine(InputTemplate inputTemplate) {
        StringJoiner joiner = new StringJoiner(DELIMITER);
        for (int i = 0; i < inputTemplate.getFieldCount(); i++) {
            joiner.add(" ");
        }
        return joiner.toString();
    }

    //FIXME 可能为null,后果未知
    private static String findKeyFieldName(InputTemplate inputTemplate) {
        for (InputField field : inputTemplate.getFields()) {
            if (field.isKey()) {
                return field.getFieldName();
            }
        }
        return null;
    }


}
