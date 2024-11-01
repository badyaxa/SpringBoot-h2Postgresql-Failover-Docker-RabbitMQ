package com.pr.tm.failover;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Slf4j
//@Service
//public class DataSourceHealthCheckService {
//    private final DataSourceContextHolder dataSourceContextHolder;
//    private final JdbcTemplate jdbcTemplate;
//
//    public DataSourceHealthCheckService(DataSourceContextHolder dataSourceContextHolder,
//                                        JdbcTemplate jdbcTemplate) {
//        this.dataSourceContextHolder = dataSourceContextHolder;
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Scheduled(fixedDelay = 60000)
//    public void checkPrimaryDatabase() {
//        if (!DataSourceEnum.PRIMARY.equals(dataSourceContextHolder.getBranchContext())) {
//            dataSourceContextHolder.setBranchContext(DataSourceEnum.PRIMARY);
//        }
//        try {
//            jdbcTemplate.execute("SELECT 1");
//            log.info("Primary database is up");
//        } catch (Exception e) {
//            log.warn("Primary database is down, switching to secondary");
//            dataSourceContextHolder.setBranchContext(DataSourceEnum.SECONDARY);
//        }
//    }
//}
