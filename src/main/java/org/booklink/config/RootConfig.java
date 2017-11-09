package org.booklink.config;

import liquibase.integration.spring.SpringLiquibase;
import org.booklink.security.JwtFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.sql.DataSource;

/**
 * Created by mhenr on 27.09.2017.
 */
@Profile("production")
@Configuration
@ComponentScan(basePackages = "org.booklink.config")
public class RootConfig {
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.json");
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(false);
        return liquibase;
    }

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.mail")
    public JavaMailSender mailSender() {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        return mailSender;
    }
}
