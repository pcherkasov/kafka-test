package com.pavelc.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource")
  public DataSource dataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @ConfigurationProperties(prefix = "spring.liquibase")
  public LiquibaseProperties liquibaseProperties() {
    return new LiquibaseProperties();
  }

  @Bean
  public SpringLiquibase liquibase() {
    return springLiquibase(dataSource(), liquibaseProperties());
  }

  private static SpringLiquibase springLiquibase(DataSource ds, LiquibaseProperties lp) {
    SpringLiquibase sl = new SpringLiquibase();
    sl.setDataSource(ds);
    sl.setChangeLog(lp.getChangeLog());
    sl.setContexts(lp.getContexts());
    sl.setShouldRun(lp.isEnabled());
    sl.setChangeLogParameters(lp.getParameters());
    return sl;
  }
}
