// package com.example.Chapter07;

// import java.util.Arrays;
// import java.util.List;
// import com.example.Chapter07.batch.CustomerFieldSetMapper;
// import com.example.Chapter07.batch.CustomerFileLineTokenizer;
// import com.example.Chapter07.domain.Customer;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
// import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepScope;
// import org.springframework.batch.core.launch.support.RunIdIncrementer;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.batch.item.file.FlatFileItemReader;
// import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.Bean;
// import org.springframework.core.io.Resource;

// @EnableBatchProcessing
// @SpringBootApplication
// public class DelimitedJob {

// 	@Autowired
// 	private JobBuilderFactory jobBuilderFactory;

// 	@Autowired
// 	private StepBuilderFactory stepBuilderFactory;

// 	// @Bean
// 	// @StepScope
// 	// public FlatFileItemReader<Customer> customerItemReader(@Value("#{jobParameters['customerFile']}")Resource inputFile) {
// 	// 	return new FlatFileItemReaderBuilder<Customer>()
// 	// 			.name("customerItemReader")
// 	// 			.delimited()
// 	// 			.names(new String[] {"firstName",
// 	// 					"middleInitial",
// 	// 					"lastName",
// 	// 					"addressNumber",
// 	// 					"street",
// 	// 					"city",
// 	// 					"state",
//     //                     "zipCode"})
//     //             // .targetType(Customer.class)
// 	// 			.fieldSetMapper(new CustomerFieldSetMapper())
// 	// 			.resource(inputFile)
// 	// 			.build();
// 	// }
// 	@Bean
// 	@StepScope
// 	public FlatFileItemReader<Customer> customerItemReader(@Value("#{jobParameters['customerFile']}")Resource inputFile) {
// 		return new FlatFileItemReaderBuilder<Customer>()
// 				.name("customerItemReader")
// 				.lineTokenizer(new CustomerFileLineTokenizer())
// 				.fieldSetMapper(new CustomerFieldSetMapper())
// 				.resource(inputFile)
// 				.build();
// 	}

// 	@Bean
// 	public ItemWriter<Customer> itemWriter() {
// 		return (items) -> items.forEach(System.out::println);
// 	}

// 	@Bean
// 	public Step copyFileStep() {
// 		return this.stepBuilderFactory.get("copyFileStep")
// 				.<Customer, Customer>chunk(10)
// 				.reader(customerItemReader(null))
// 				.writer(itemWriter())
// 				.build();
// 	}

// 	@Bean
// 	public Job job() {
// 		return this.jobBuilderFactory.get("job")
//                 .start(copyFileStep())
//                 .incrementer(new RunIdIncrementer())
// 				.build();
// 	}


// 	public static void main(String[] args) {
// 		List<String> realArgs = Arrays.asList("customerFile=/input/customer.csv","foo=bar");

// 		SpringApplication.run(DelimitedJob.class, realArgs.toArray(new String[2]));
// 	}

// }


