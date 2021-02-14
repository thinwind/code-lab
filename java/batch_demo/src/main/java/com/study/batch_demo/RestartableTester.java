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
import com.study.batch_demo.muses.LineMatcher;
import com.study.batch_demo.muses.MultiFileReader;
import com.study.batch_demo.muses.TwoFileLineMatcher;
import com.study.batch_demo.param.InputTemplate;
import com.study.batch_demo.param.OutputTemplate;
import com.study.batch_demo.writer.TextItemWriter;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

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

        LineMatcher matcher = new TwoFileLineMatcher(leftInputTemplate, righInputTemplate);

        // win系统系统换行符是 \r\n，长度是2
        // linux 一般是\r，mac(unix) 一般是 \n，长度都是1
        MultiFileReader itemReader = new MultiFileReader(new String[] {"input-data", "input-data2"}, matcher, "UTF-8",
                2,file -> applicationContext.getResource(file).getInputStream());
        itemReader.setName("two_file_reader");

        OutputTemplate outputTemplate = loadTemplate(applicationContext,"outputTemplate.json",OutputTemplate.class);

        JobBuilderFactory jobBuilderFactory = applicationContext.getBean(JobBuilderFactory.class);
        StepBuilderFactory stepBuilderFactory =
                applicationContext.getBean(StepBuilderFactory.class);

        ItemWriter textItemWriter = new TextItemWriter(
                new FileSystemResource("target/test-outputs/output3.txt"), outputTemplate);
        TaskletStep step = stepBuilderFactory.get("file2FileStep").chunk(3).reader(itemReader)
                .writer(textItemWriter).build();

        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);
        Job job = jobBuilderFactory.get("joinfile5").start(step).build();
        JobParameters jobParameter = new JobParametersBuilder()
                .addLong("time", 123456789014L).toJobParameters();
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

}
