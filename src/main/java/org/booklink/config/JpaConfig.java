package org.booklink.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by mhenr on 30.09.2017.
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.booklink.repositories")
public class JpaConfig {
    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource prodDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean session = new LocalSessionFactoryBean();
        session.setDataSource(prodDataSource());
        session.setPackagesToScan(new String[] {"org.booklink.models"});
        Properties props = new Properties();
        props.setProperty("dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
        session.setHibernateProperties(props);
        return session;
    }
}
