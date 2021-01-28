package com.study.batch_demo.config;

import com.study.batch_demo.entity.Test;
import com.study.batch_demo.listener.File2FileListener;
import com.study.batch_demo.listener.TestStepExecutionListener;
import com.study.batch_demo.mapper.TestFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;


/**
 * @author zhudan <zhudan@ebchinatech.com>
 * @since 2021/1/21 14:51
 */
@Configuration
@EnableBatchProcessing
public class File2FileBatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    public Job file2FileJob(Step file2FileStep, JobExecutionListener file2FileListener) {
        return jobBuilderFactory.get("file2File")
                .listener(file2FileListener)
                .start(file2FileStep)
                .build();
    }


    public Step file2FileStep(ItemReader file2FileReader, ItemWriter file2FileWriter,
                              StepExecutionListener stepListener) {
        return stepBuilderFactory.get("file2FileStep")
                .listener(stepListener)
                .chunk(50)
                .reader(file2FileReader)
                .writer(file2FileWriter)
                .build();
    }


    public ItemWriter file2FileWriter(){

        return new FlatFileItemWriterBuilder<Test>()
                .name("file2FileWriter")
                .resource(new FileSystemResource("target/test-outputs/output.txt"))
                .lineAggregator(new PassThroughLineAggregator<>())
                .formatted()
                .format("%-20s%10.2f")
                .names(new String[] {"code", "count"})
                .build();
    }


    public ItemReader file2FileReader(LineTokenizer fixedLengthTokenizer) {
        return new FlatFileItemReaderBuilder<Test>()
                .name("file2FileReader")
                .resource(new FileSystemResource("/Users/zhudan/Desktop/项目/科技项目/compare_file1.txt"))
                .lineTokenizer(fixedLengthTokenizer)
                .fieldSetMapper(new TestFieldSetMapper())
                .build();
    }


    public FixedLengthTokenizer fixedLengthTokenizer() {
        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("count", "code");
        tokenizer.setColumns(new Range(29, 32), new Range(38, 52));
        tokenizer.setStrict(false);
        return tokenizer;
    }


    public JobExecutionListener file2FileListener() {
        return new File2FileListener();
    }


    public StepExecutionListener stepListener(){
        return new TestStepExecutionListener();
    }


    public TaskExecutor file2fileTaskExecutor(){
        return new SimpleAsyncTaskExecutor("file2file");
    }
}
