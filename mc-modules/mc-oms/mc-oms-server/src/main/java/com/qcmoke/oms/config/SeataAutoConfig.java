package com.qcmoke.oms.config;


import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author qcmoke
 */
@Configuration
public class SeataAutoConfig {

    @Autowired(required = true)
    private DataSourceProperties dataSourceProperties;
    private final static Logger logger = LoggerFactory.getLogger(SeataAutoConfig.class);
    private DataSourceProxy dataSourceProxy;

    @Bean(name = "dataSource")
    @Primary
    public DataSource druidDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        logger.info("dataSourceProperties.getUrl():{}", dataSourceProperties.getUrl());
        druidDataSource.setUrl(dataSourceProperties.getUrl());
        druidDataSource.setUsername(dataSourceProperties.getUsername());
        druidDataSource.setPassword(dataSourceProperties.getPassword());
        druidDataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(0);
        druidDataSource.setMaxActive(180);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(0);
        druidDataSource.setValidationQuery("Select 1 from DUAL");
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(25200000);
        druidDataSource.setRemoveAbandoned(true);
        druidDataSource.setRemoveAbandonedTimeout(1800);
        druidDataSource.setLogAbandoned(true);
        logger.info("装载dataSource........");
        dataSourceProxy = new DataSourceProxy(druidDataSource);
        return dataSourceProxy;
    }

    /**
     * init datasource proxy
     */
    @Bean
    public DataSourceProxy dataSourceProxy() {
        logger.info("代理dataSource........");
        return dataSourceProxy;
    }

}
