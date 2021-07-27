/**
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.Chapter04.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.example.Chapter04.batch.DailyJobTimestamper;
import com.example.Chapter04.batch.LoggingStepStartStopListener;
import com.example.Chapter04.batch.RandomChunkSizePolicy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

/**
 * @author Michael Minella
 */
// @EnableBatchProcessing
// @SpringBootApplication
public class ChunkJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job chunkBasedJob() {
		return this.jobBuilderFactory.get("chunkBasedJob2")
                .start(chunkStep())
                .incrementer(new DailyJobTimestamper())
				.build();
	}

	@Bean
	public Step chunkStep() {
		return this.stepBuilderFactory.get("chunkStep")
				// .<String, String>chunk(1000) 
                .<String, String>chunk(randomCompletionPolicy())
				.reader(itemReader())
				.writer(itemWriter())
                .listener(new LoggingStepStartStopListener())
				.build();
	}

	@Bean
	public ListItemReader<String> itemReader() {
		List<String> items = new ArrayList<>(10000);

		for (int i = 0; i < 10000; i++) {
			items.add(UUID.randomUUID().toString());
		}

		return new ListItemReader<>(items);
	}

    @Bean
    @StepScope
    public FlatFileItemReader<String> fileItemReader(@Value("#{jobParameters['inputFile']}") Resource inputFile){
        return new FlatFileItemReaderBuilder<String>()
                .name("itemReader")
                .resource(inputFile)
                .lineMapper(new PassThroughLineMapper())
                .build();
    }
    
    @Bean
    @StepScope
    public FlatFileItemWriter<String> fileItemWriter(@Value("#{jobParameters['outputFile']}") Resource outputFile){
        return new FlatFileItemWriterBuilder<String>()
                .name("itemWriter")
                .resource(outputFile)
                .lineAggregator(new PassThroughLineAggregator<>())
                .build();
    }
    
	@Bean
	public ItemWriter<String> itemWriter() {
		return items -> {
			for (String item : items) {
				System.out.println(">> current item = " + item);
			}
		};
	}

	@Bean
	public CompletionPolicy completionPolicy() {
		CompositeCompletionPolicy policy =
				new CompositeCompletionPolicy();

		policy.setPolicies(
				new CompletionPolicy[] {
						new TimeoutTerminationPolicy(3),
						new SimpleCompletionPolicy(1000)});

		return policy;
	}

	@Bean
	public CompletionPolicy randomCompletionPolicy() {
		return new RandomChunkSizePolicy();
	}

	public static void main(String[] args) {
        args=new String[]{"inputFile=file:///Users/shangyehua/tmp/stack.txt","outputFile=file:///Users/shangyehua/tmp/stack-out.txt"};
		SpringApplication.run(ChunkJob.class, args);
	}
}
