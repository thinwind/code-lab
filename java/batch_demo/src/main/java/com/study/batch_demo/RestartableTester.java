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

import com.alibaba.fastjson.JSON;
import com.study.batch_demo.muses.*;
import com.study.batch_demo.param.InputField;
import com.study.batch_demo.param.InputTemplate;
import com.study.batch_demo.param.OutputTemplate;
import com.study.batch_demo.reader.MultiFileItemReader;
import com.study.batch_demo.writer.TextItemWriter;
import org.springframework.batch.core.*;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 *
 * TODO Tester说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-00-05  10:33
 *
 */
@SpringBootApplication
public class RestartableTester {

    public static void main(String[] args) throws IOException {
        ConfigurableApplicationContext applicationContext =
                SpringApplication.run(RestartableTester.class, args);

        InputTemplate leftInputTemplate = loadTemplate(applicationContext,"left_file_template.json",InputTemplate.class);
        InputTemplate righInputTemplate = loadTemplate(applicationContext,"right_file_template.json",InputTemplate.class);

        DelimitedLineTokenizer joinedTokenizer =
                createTokenizer(leftInputTemplate, righInputTemplate);
        LineMatcher matcher = new TwoFileLineMatcher(leftInputTemplate, righInputTemplate);

        MultiFileReader itemReader = new MultiFileReader(new String[] {"input-data", "input-data2"}, matcher, "UTF-8",
                joinedTokenizer,file -> applicationContext.getResource(file).getInputStream());
        itemReader.setName("two_file_reader");

        OutputTemplate outputTemplate = loadTemplate(applicationContext,"outputTemplate.json",OutputTemplate.class);

        JobBuilderFactory jobBuilderFactory = applicationContext.getBean(JobBuilderFactory.class);
        StepBuilderFactory stepBuilderFactory =
                applicationContext.getBean(StepBuilderFactory.class);

        ItemWriter textItemWriter = new TextItemWriter(
                new FileSystemResource("target/test-outputs/output1.txt"), outputTemplate);
        TaskletStep step = stepBuilderFactory.get("file2FileStep").chunk(3).reader(itemReader)
                .writer(textItemWriter).build();

        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        Job job = jobBuilderFactory.get("joinfile5").start(step).build();
        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("time", 12345678L).toJobParameters();
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

    private static <T> T loadTemplate(
            ConfigurableApplicationContext applicationContext,String location,Class<T> cls) throws IOException {
        Resource temp = applicationContext.getResource(location);
        T template =
                JSON.parseObject(temp.getInputStream(), cls);
        return template;
    }

    private static DelimitedLineTokenizer createTokenizer(InputTemplate leftInputTemplate,
            InputTemplate righInputTemplate) {
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        //delimitedLineTokenizer.setDelimiter(DELIMITER);
        delimitedLineTokenizer.setStrict(false);
        List<InputField> leftFields = leftInputTemplate.getFields();
        List<InputField> rightFields = righInputTemplate.getFields();

        //去掉右边的keyfield
        // String[] names = new String[leftFields.size() + rightFields.size()];
        // int[] columns = new int[names.length];
        List<String> nameList=new ArrayList<>();
        List<Integer> columnList=new ArrayList<>();

        for (int i = 0; i < leftFields.size(); i++) {
            nameList.add(leftFields.get(i).getFieldName());
            columnList.add(leftFields.get(i).getFileLocation() - 1);
        }

        int rightFieldsLocBase = leftInputTemplate.getFieldCount();
        for (int i = 0; i < rightFields.size(); i++) {
            InputField field = rightFields.get(i);
            if (field.isKey()) {
                continue;
            }
            nameList.add(field.getFieldName());
            columnList.add(field.getFileLocation() + rightFieldsLocBase - 1);
        }
        int[] columns = new int[columnList.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columnList.get(i);
        }
        
        String[] names=nameList.toArray(new String[nameList.size()]);
        delimitedLineTokenizer.setIncludedFields(columns);
        delimitedLineTokenizer.setNames(names);
        return delimitedLineTokenizer;
    }

}
