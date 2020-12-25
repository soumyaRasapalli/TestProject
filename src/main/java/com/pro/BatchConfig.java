package com.pro;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    public StepBuilderFactory stepBuilderFactory;
	
	private Resource inputResource = new FileSystemResource("C://input//Details.csv");
	
	private Resource outputResource = new FileSystemResource("C://file//output.csv");
	
	@Bean
	public Job job1() {
		return jobBuilderFactory.get("job1").incrementer(new RunIdIncrementer()).flow(s1()).next(s2()).end().build();
		
	}
	
	@Bean
	public Step s1() {
		return stepBuilderFactory.get("step1").<Employee, Employee> chunk(2).reader(batch_reader()).processor(batch_processor()).writer(to_DB()).build();
		
	}
	
	@Bean
	public Step s2() {
		return stepBuilderFactory.get("step2").<Employee, Employee> chunk(2).reader(batch_reader()).processor(batch_processor()).writer(to_file()).build();
		
	}
	
	@Bean
	public ItemReader<Employee> batch_reader(){
		FlatFileItemReader<Employee> itemReader=new FlatFileItemReader<Employee>();
		itemReader.setResource(inputResource);
		itemReader.setLineMapper(lineMapper());
		itemReader.setLinesToSkip(1);
		/*try {
			itemReader.read();
		} catch (UnexpectedInputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println("reading the file");
		return itemReader;
		
	}
	
	
	@Bean
	public LineMapper<Employee> lineMapper(){
		DefaultLineMapper<Employee> defaultLineMapper=new DefaultLineMapper<Employee>();
		DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] {"Id","Name","Age","Profession"});
		//BeanWrapperFieldSetMapper<Employee> fieldSetMapper=new BeanWrapperFieldSetMapper<Employee>();
	    defaultLineMapper.setLineTokenizer(tokenizer);
		defaultLineMapper.setFieldSetMapper(new CustomMapper());
		return defaultLineMapper;
		
	}
	
	@Bean
	public ItemProcessor<Employee,Employee> batch_processor(){
		return new EmployeeItemProcessor();
		
	}
	
	@Bean
	public JdbcBatchItemWriter<Employee> to_DB(){
		JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<Employee>();
		itemWriter.setDataSource(dataSource());
		itemWriter.setSql("INSERT INTO EMPLOYEE (ID, NAME, AGE, PROFESSION) VALUES (:Id, :Name, :Age, :Profession)");
	    itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Employee>());
		return itemWriter;
		
	}
	
	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
	    return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
	            .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
	            .addScript("classpath:employee.sql")
	            .setType(EmbeddedDatabaseType.H2)
	            .build();
		
	}
	
	@Bean
	public ItemWriter<Employee> to_file(){
		FlatFileItemWriter<Employee> itemWriter = new FlatFileItemWriter<Employee>();
		itemWriter.setResource(outputResource);
		itemWriter.setAppendAllowed(false);
		DelimitedLineAggregator<Employee> lineAggregator=new DelimitedLineAggregator<Employee>();
		lineAggregator.setDelimiter(",");
		BeanWrapperFieldExtractor<Employee> fieldExtractor=new BeanWrapperFieldExtractor<Employee>();
		fieldExtractor.setNames(new String[] {"Id","Name","Age","Profession"});
		lineAggregator.setFieldExtractor(fieldExtractor);
		itemWriter.setLineAggregator(lineAggregator);
		return itemWriter;
		
	}

}
