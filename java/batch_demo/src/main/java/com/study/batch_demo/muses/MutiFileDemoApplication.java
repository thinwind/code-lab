package com.study.batch_demo.muses;

import java.util.List;
import java.io.IOException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import com.study.batch_demo.mapper.DynamicFieldSetMapper;
import com.study.batch_demo.param.InputField;
import com.study.batch_demo.param.InputTemplate;
import com.study.batch_demo.param.OutputTemplate;
import com.study.batch_demo.tools.ParseFile;
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
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@SpringBootApplication
public class MutiFileDemoApplication {

    public static void main(String[] args) throws IOException {
        // String path = args[0];
        // String path =
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MutiFileDemoApplication.class, args);
        JobLauncher jobLauncher = applicationContext.getBean(JobLauncher.class);

        Resource temp=applicationContext.getResource("inputTemplate.json");
        InputTemplate leftInputTemplate = JSON.parseObject(temp.getInputStream(),InputTemplate.class);
        // InputTemplate leftInputTemplate = ParseFile.parseJson("inputTemplate.json", InputTemplate.class);

        
        JobBuilderFactory jobBuilderFactory = applicationContext.getBean(JobBuilderFactory.class);
        StepBuilderFactory stepBuilderFactory = applicationContext.getBean(StepBuilderFactory.class);

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setDelimiter(",");
        delimitedLineTokenizer.setStrict(false);

        List<InputField> fields = leftInputTemplate.getFields();
        String[] names = new String[fields.size()];
        int[] columns = new int[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            names[i] = fields.get(i).getFieldName();
            columns[i] = fields.get(i).getFileLocation()-1;
        }
        delimitedLineTokenizer.setIncludedFields(columns);
        delimitedLineTokenizer.setNames(names);

        FlatFileItemReader file2FileReader = new FlatFileItemReaderBuilder().name("file2FileReader")
                .resource(new FileSystemResource("/Users/zhudan/Desktop/项目/科技项目/batch_demo/src/main/resources/input-data"))
                .lineTokenizer(delimitedLineTokenizer)
                .fieldSetMapper(new DynamicFieldSetMapper())
                .build();

        OutputTemplate outputTemplate = ParseFile.parseJson("/Users/zhudan/Desktop/项目/科技项目/batch_demo/src/main/resources/outputTemplate.json", OutputTemplate.class);
        ItemWriter textItemWriter = new TextItemWriter(new FileSystemResource("target/test-outputs/output1.txt"), outputTemplate);
        TaskletStep step = stepBuilderFactory.get("file2FileStep").chunk(50)
                .reader(file2FileReader)
                .writer(textItemWriter)
                .build();

        Job job = jobBuilderFactory.get("file2File").start(step).build();
        JobParameters jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
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

}
