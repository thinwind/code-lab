package com.example.Chapter06;

import java.util.Properties;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

// @EnableBatchProcessing
// @SpringBootApplication
// public class RestApplication {

// 	@Autowired
// 	private JobBuilderFactory jobBuilderFactory;

// 	@Autowired
// 	private StepBuilderFactory stepBuilderFactory;

// 	@Bean
// 	public Job job() {
// 		return this.jobBuilderFactory.get("job")
// 				.incrementer(new RunIdIncrementer())
// 				.start(step1())
// 				.build();
// 	}

// 	@Bean
// 	public Step step1() {
// 		return this.stepBuilderFactory.get("step1")
// 				.tasklet((stepContribution, chunkContext) -> {
// 					System.out.println("step 1 ran today!");
// 					return RepeatStatus.FINISHED;
// 				}).build();
// 	}

// 	@RestController
// 	public static class JobLaunchingController {

// 		@Autowired
// 		private JobLauncher jobLauncher;

// 		@Autowired
// 		private ApplicationContext context;

// 		@Autowired
// 		private JobExplorer jobExplorer;

// 		@PostMapping(path = "/run")
// 		public ExitStatus runJob(@RequestBody JobLaunchRequest request) throws Exception {
// 			Job job = this.context.getBean(request.getName(), Job.class);

// 			JobParameters jobParameters =
// 					new JobParametersBuilder(request.getJobParameters(),
// 								this.jobExplorer)
// 							.getNextJobParameters(job)
// 							.toJobParameters();

// 			return this.jobLauncher.run(job, jobParameters).getExitStatus();
// //			Job job = this.context.getBean(request.getName(), Job.class);
// //
// //			return this.jobLauncher.run(job, request.getJobParameters()).getExitStatus();
// 		}
// 	}

// 	public static class JobLaunchRequest {
// 		private String name;

// 		private Properties jobParameters;

// 		public String getName() {
// 			return name;
// 		}

// 		public void setName(String name) {
// 			this.name = name;
// 		}

// 		public Properties getJobParamsProperties() {
// 			return jobParameters;
// 		}

// 		public void setJobParamsProperties(Properties jobParameters) {
// 			this.jobParameters = jobParameters;
// 		}

// 		public JobParameters getJobParameters() {
// 			Properties properties = new Properties();
// 			properties.putAll(this.jobParameters);

// 			return new JobParametersBuilder(properties)
// 					.toJobParameters();
// 		}
// 	}

// 	public static void main(String[] args) {
// 		new SpringApplication(RestApplication.class).run(args);
// 	}
// }
