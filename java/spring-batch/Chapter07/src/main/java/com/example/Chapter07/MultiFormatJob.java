package com.example.Chapter07;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.Chapter07.batch.TransactionFieldSetMapper;
import com.example.Chapter07.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

// @EnableBatchProcessing
// @SpringBootApplication
public class MultiFormatJob {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Bean
	@StepScope
	public FlatFileItemReader customerItemReader(
			@Value("#{jobParameters['customerFile']}")Resource inputFile) {

		return new FlatFileItemReaderBuilder<Customer>()
				.name("customerItemReader")
				.lineMapper(lineTokenizer())
				.resource(inputFile)
				.build();
	}

	@Bean
	public PatternMatchingCompositeLineMapper lineTokenizer() {
		Map<String, LineTokenizer> lineTokenizers =
				new HashMap<>(2);

		lineTokenizers.put("CUST*", customerLineTokenizer());
		lineTokenizers.put("TRANS*", transactionLineTokenizer());

		Map<String, FieldSetMapper> fieldSetMappers =
				new HashMap<>(2);

		BeanWrapperFieldSetMapper<Customer> customerFieldSetMapper =
				new BeanWrapperFieldSetMapper<>();
		customerFieldSetMapper.setTargetType(Customer.class);

		fieldSetMappers.put("CUST*", customerFieldSetMapper);
		fieldSetMappers.put("TRANS*", new TransactionFieldSetMapper());

		PatternMatchingCompositeLineMapper lineMappers =
				new PatternMatchingCompositeLineMapper();

		lineMappers.setTokenizers(lineTokenizers);
		lineMappers.setFieldSetMappers(fieldSetMappers);

		return lineMappers;
	}

	@Bean
	public DelimitedLineTokenizer transactionLineTokenizer() {
		DelimitedLineTokenizer lineTokenizer =
				new DelimitedLineTokenizer();

		lineTokenizer.setNames("prefix",
				"accountNumber",
				"transactionDate",
				"amount");

		return lineTokenizer;
	}

	@Bean
	public DelimitedLineTokenizer customerLineTokenizer() {
		DelimitedLineTokenizer lineTokenizer =
				new DelimitedLineTokenizer();

		lineTokenizer.setNames("firstName",
				"middleInitial",
				"lastName",
				"address",
				"city",
				"state",
				"zipCode");

		lineTokenizer.setIncludedFields(1, 2, 3, 4, 5, 6, 7);

		return lineTokenizer;
	}

	@Bean
	public ItemWriter itemWriter() {
		return (items) -> items.forEach(System.out::println);
	}

	@Bean
	public Step copyFileStep() {
		return this.stepBuilderFactory.get("copyFileStep")
				.<Customer, Customer>chunk(10)
				.reader(customerItemReader(null))
				.writer(itemWriter())
				.build();
	}

	@Bean
	public Job job() {
		return this.jobBuilderFactory.get("job")
                .start(copyFileStep())
                .incrementer(new RunIdIncrementer())
				.build();
	}


	public static void main(String[] args) {
		List<String> realArgs = Collections.singletonList("customerFile=/input/customerMultiFormat.csv");

		SpringApplication.run(MultiFormatJob.class, realArgs.toArray(new String[1]));
	}

}

