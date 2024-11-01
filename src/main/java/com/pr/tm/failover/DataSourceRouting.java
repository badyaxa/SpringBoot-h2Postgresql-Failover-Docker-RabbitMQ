package com.pr.tm.failover;
//
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//import org.springframework.stereotype.Component;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class DataSourceRouting extends AbstractRoutingDataSource {
//    private final DataSourcePrimaryConfig dataSourcePrimaryConfig;
//    private final DataSourceSecondaryConfig dataSourceSecondaryConfig;
//    private final DataSourceContextHolder dataSourceContextHolder;
//
//    public DataSourceRouting(DataSourceContextHolder dataSourceContextHolder,
//                             DataSourcePrimaryConfig dataSourcePrimaryConfig,
//                             DataSourceSecondaryConfig dataSourceSecondaryConfig) {
//        this.dataSourcePrimaryConfig = dataSourcePrimaryConfig;
//        this.dataSourceSecondaryConfig = dataSourceSecondaryConfig;
//        this.dataSourceContextHolder = dataSourceContextHolder;
//
//        Map<Object, Object> dataSourceMap = new HashMap<>();
//        dataSourceMap.put(DataSourceEnum.PRIMARY, dataSourcePrimaryDataSource());
//        dataSourceMap.put(DataSourceEnum.SECONDARY, dataSourceSecondaryDataSource());
//        this.setTargetDataSources(dataSourceMap);
//        this.setDefaultTargetDataSource(dataSourcePrimaryDataSource());
//    }
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        return dataSourceContextHolder.getBranchContext();
//    }
//
//    public DataSource dataSourcePrimaryDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(dataSourcePrimaryConfig.getUrl());
//        dataSource.setUsername(dataSourcePrimaryConfig.getUsername());
//        dataSource.setPassword(dataSourcePrimaryConfig.getPassword());
//        return dataSource;
//    }
//
//    public DataSource dataSourceSecondaryDataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUrl(dataSourceSecondaryConfig.getUrl());
//        dataSource.setUsername(dataSourceSecondaryConfig.getUsername());
//        dataSource.setPassword(dataSourceSecondaryConfig.getPassword());
//        return dataSource;
//    }
//}
