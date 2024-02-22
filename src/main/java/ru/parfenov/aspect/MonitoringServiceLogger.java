package ru.parfenov.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;

@Aspect
public class MonitoringServiceLogger {
    private final static Logger LOG =
            Logger.getLogger("MonitoringServiceLogger.class");

    @Pointcut("execution(* SqlUserStore.getAll())")
    public void sqlUserStoreGetAll() {
    }

    @Pointcut("@annotation(* SqlUserStore.findById(int userId))")
    public void sqlUserStoreFindById() {
    }

    @Pointcut("@annotation(* SqlUserStore.getByLogin(String login))")
    public void sqlUserStoreGetByLogin() {
    }

    @Pointcut("@annotation(* SqlUserStore.create(User user))")
    public void sqlUserStoreCreate() {
    }

    @Pointcut("@annotation(* SqlUserStore.insertUserHistory(User user, String newHistory))")
    public void sqlUserStoreInsertUserHistory() {
    }


    @Pointcut("@annotation(* SqlPointValueStore.create(PointValue pointValue))")
    public void sqlPointValueStoreCreate() {
    }

    @Pointcut("@annotation(* SqlPointValueStore.findByUser(User user))")
    public void sqlPointValueStoreFindByUser() {
    }

    @Pointcut("@annotation(* SqlPointValueStore.getLastData(int userId))")
    public void sqlPointValueStoreGetLastData() {
    }

    @Pointcut("@annotation(* SqlPointValueStore.getDataForSpecMonth(User user, LocalDateTime date))")
    public void sqlPointValueStoreGetDataForSpecMonth() {
    }


    @Pointcut("@annotation(* JdbcUserService.reg(String login, String password))")
    public void jdbcUserServiceReg() {
    }

    @Pointcut("@annotation(* JdbcUserService.enter(String login))")
    public void jdbcUserServiceEnter() {
    }

    @Pointcut("@annotation(* JdbcUserService.viewAllUsers())")
    public void jdbcUserServiceViewAllUsers() {
    }

    @Pointcut("@annotation(* JdbcUserService.viewUserHistory(String login))")
    public void jdbcUserServiceViewUserHistory() {
    }

    @Pointcut("@annotation(* JdbcUserService.getByLogin(String login))")
    public void jdbcUserServiceGetByLogin() {
    }


    @Pointcut("@annotation(* JdbcPointValueService.submitData(String login, List<PointValue> list))")
    public void jdbcPointValueServiceSubmitData() {
    }

    @Pointcut("@annotation(* JdbcPointValueService.viewLastData(String login))")
    public void jdbcPointValueServiceViewLastData() {
    }

    @Pointcut("@annotation(* JdbcPointValueService.viewDataForSpecMonth(String login, int month, int year))")
    public void jdbcPointValueServiceViewDataForSpecMonth() {
    }

    @Pointcut("@annotation(* JdbcPointValueService.viewDataHistory(String login))")
    public void jdbcPointValueServiceViewDataHistory() {
    }

    @Pointcut("@annotation(* JdbcPointValueService.toOut(String login))")
    public void jdbcPointValueServiceToOut() {
    }

    @Pointcut("@annotation(* JdbcPointValueService.validationOnceInMonth(String login))")
    public void jdbcPointValueServiceValidationOnceInMonth() {
    }

    @Around("execution(* *(..))")
    public void logCallMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        LOG.debug("Call method " + methodName + " " + LocalDateTime.now());
        try {
            joinPoint.proceed();
            LOG.debug("End method " + methodName + " " + LocalDateTime.now());
        } catch (Throwable e) {
            LOG.error("Exception:", e);
        }
    }
}
