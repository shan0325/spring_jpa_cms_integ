package com.shan.spring.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public class QuerydslConfig {

    @PersistenceContext(unitName = "cmsEntityManager")
    private EntityManager cmsEntityManager;

    @PersistenceContext(unitName = "userEntityManager")
    private EntityManager userEntityManager;

    @Primary
    @Bean
    public JPAQueryFactory cmsQueryFactory() {
        return new JPAQueryFactory(cmsEntityManager);
    }

    @Bean
    public JPAQueryFactory userQueryFactory() {
        return new JPAQueryFactory(userEntityManager);
    }
}
