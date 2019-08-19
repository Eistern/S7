package com.contacts.demo.batch;

import com.contacts.demo.data.types.Person;
import com.contacts.demo.elasticsearch.PersonSearchEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchJobConfiguration {
    @Bean
    public Step processDatabase(StepBuilderFactory builderFactory, ItemReader<Person> reader,
                                ItemWriter<PersonSearchEntity> writer, ItemProcessor<Person, PersonSearchEntity> processor) {
        return builderFactory.get("step")
                .<Person, PersonSearchEntity>chunk(100)
                .writer(writer)
                .reader(reader)
                .processor(processor)
                .build();
    }

    @Bean
    public Job indexPersons(JobBuilderFactory jobs, Step step) {
        return jobs.get("index")
                .preventRestart()
                .start(step)
                .build();
    }

    @Bean
    public JobRepository jobRepository() throws Exception {
        MapJobRepositoryFactoryBean factoryBean = new MapJobRepositoryFactoryBean(new ResourcelessTransactionManager());
        return factoryBean.getObject();
    }

    @Bean
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.afterPropertiesSet();

        return jobLauncher;
    }
}
