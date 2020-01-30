package com.qcmoke.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Configuration
public class MultipleDataSourceConfig {

    /**
     * auth数据源（默认）
     */
    @Primary
    @Bean(name = "authDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.auth")
    public DataSource authDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * system数据源
     */
    @Bean(name = "systemDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.system")
    public DataSource systemDataSource() {
        return DataSourceBuilder.create().build();
    }


    /**
     * auth jdbc工具
     */
    @Primary
    @Bean(name = "authJdbcTemplate")
    public JdbcTemplate authJdbcTemplate(@Qualifier("authDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    /**
     * auth事务管理
     * spring事务配置数据源代理
     */
    @Primary
    @Bean("authDataSourceTransactionManager")
    public DataSourceTransactionManager authDataSourceTransactionManager(@Qualifier("authDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * auth事务管理工具
     */
    @Primary
    @Bean("authTransactionTemplate")
    public TransactionTemplate authTransactionTemplate(@Qualifier("authDataSourceTransactionManager") DataSourceTransactionManager dataSourceTransactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        //设置事务隔离级别
        //transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        //设置事务传递机制
        //transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionTemplate.setTransactionManager(dataSourceTransactionManager);
        return transactionTemplate;
    }


    /**
     * system jdbc工具
     */
    @Bean(name = "systemJdbcTemplate")
    public JdbcTemplate systemJdbcTemplate(@Qualifier("systemDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }


    /**
     * system事务管理
     * spring事务配置数据源代理
     */
    @Bean("systemDataSourceTransactionManager")
    public DataSourceTransactionManager systemDataSourceTransactionManager(@Qualifier("systemDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

    /**
     * system事务管理工具
     */
    @Bean("systemTransactionTemplate")
    public TransactionTemplate systemTransactionTemplate(@Qualifier("systemDataSourceTransactionManager") DataSourceTransactionManager dataSourceTransactionManager) {
        TransactionTemplate transactionTemplate = new TransactionTemplate();
        transactionTemplate.setTransactionManager(dataSourceTransactionManager);
        return transactionTemplate;
    }
}
