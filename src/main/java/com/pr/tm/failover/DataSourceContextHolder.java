package com.pr.tm.failover;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class DataSourceContextHolder {
//    private static ThreadLocal<DataSourceEnum> threadLocal;
//
//    public DataSourceContextHolder() {
//        threadLocal = new ThreadLocal<>();
//    }
//
//    public static void clearBranchContext() {
//        threadLocal.remove();
//    }
//
//    public DataSourceEnum getBranchContext() {
//        return threadLocal.get();
//    }
//
//    public void setBranchContext(DataSourceEnum dataSourceEnum) {
//        threadLocal.set(dataSourceEnum);
//    }
//}
