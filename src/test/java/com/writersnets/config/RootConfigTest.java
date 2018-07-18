package com.writersnets.config;

//import liquibase.integration.spring.SpringLiquibase;
import com.writersnets.security.SecurityConfigTest;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Created by mhenr on 27.09.2017.
 */
@Profile("test")
@Configuration
@ComponentScan(basePackageClasses = {SecurityConfigTest.class})
@EntityScan(basePackages = "com.writersnets.models.entities")
@EnableJpaRepositories(basePackages = "com.writersnets.repositories")
public class RootConfigTest {
    //@Autowired
    //private JwtFilter jwtFilter;

    /*
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.json");
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(false);
        return liquibase;
    }
    */

    //@Bean
    //public FilterRegistrationBean jwtFilter() {
     //   final FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
     //   registrationBean.setFilter(jwtFilter);
     //   registrationBean.addUrlPatterns("/*");
     //   return registrationBean;
    //}

    @Bean
    @ConfigurationProperties(prefix = "spring.mail")
    public JavaMailSender mailSender() {
        JavaMailSender mailSender = new JavaMailSenderImpl();
        return mailSender;
    }
}