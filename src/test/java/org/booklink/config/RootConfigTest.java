package org.booklink.config;

import org.booklink.repositories.BookRepository;
import org.booklink.security.SecurityConfigTest;
import org.booklink.services.TestBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

/**
 * Created by mhenr on 27.09.2017.
 */
@Profile("test")
@Configuration
@ComponentScan(basePackageClasses = {SecurityConfigTest.class, TestBean.class, BookRepository.class})
public class RootConfigTest {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource prodDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }
}
