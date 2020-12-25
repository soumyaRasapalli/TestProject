package com.pro;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class Application {
	
	@Autowired
	private JobLauncher launcher;
	
	@Autowired
	private Job job1;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		System.out.println("The master branch changes");

	}

	@Scheduled(cron = "0 40/5 12 * * ?")
    public void perform() throws Exception
    {
		JobParameters param=new JobParametersBuilder().addLong("jobId", System.currentTimeMillis()).toJobParameters();
		System.out.println("job is launched here"+job1);
		launcher.run(job1, param);
    }
}
