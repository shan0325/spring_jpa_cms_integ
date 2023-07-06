package com.shan.spring.config.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@RequiredArgsConstructor
@EnableJpaRepositories(
        basePackages = "com.shan.spring.cms.repository", // repository 패키지 경로
        entityManagerFactoryRef = "cmsEntityManager", // EntityManager의 이름
        transactionManagerRef = "cmsTransactionManager" // 트랜잭션 매니저의 이름
)
@Configuration
public class CmsDatasourceConfig {

    private final Environment env;

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource-cms")
    public DataSource cmsDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean cmsEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(cmsDataSource());
        em.setPackagesToScan("com.shan.spring.cms.domain");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Primary
    @Bean
    public PlatformTransactionManager cmsTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(cmsEntityManager().getObject());
        return transactionManager;
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.hbm2ddl-auto")); // jpa.hibernate.ddl-auto 속성과 같음
        return properties;
    }
}
