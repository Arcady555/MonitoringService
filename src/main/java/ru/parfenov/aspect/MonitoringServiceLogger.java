package ru.parfenov.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;

@Aspect
@Slf4j
public class MonitoringServiceLogger {

    @Pointcut("execution(* UserStoreImpl.getAll())")
    public void userStoreImplGetAll() {
    }

    @Pointcut("@annotation(* UserStoreImpl.findById(int userId))")
    public void userStoreImplFindById() {
    }

    @Pointcut("@annotation(* UserStoreImpl.getByLogin(String login))")
    public void userStoreImplGetByLogin() {
    }

    @Pointcut("@annotation(* UserStoreImpl.create(User user))")
    public void userStoreImplCreate() {
    }

    @Pointcut("@annotation(* UserStoreImpl.insertUserHistory(User user, String newHistory))")
    public void userStoreImplInsertUserHistory() {
    }


    @Pointcut("@annotation(* PointValueStoreImpl.create(PointValue pointValue))")
    public void pointValueStoreImplCreate() {
    }

    @Pointcut("@annotation(* PointValueStoreImpl.findByUser(User user))")
    public void pointValueStoreImplFindByUser() {
    }

    @Pointcut("@annotation(* PointValueStoreImpl.getLastData(int userId))")
    public void pointValueStoreImplGetLastData() {
    }

    @Pointcut("@annotation(* PointValueStoreImpl.getDataForSpecMonth(User user, LocalDateTime date))")
    public void pointValueStoreImplGetDataForSpecMonth() {
    }


    @Pointcut("@annotation(* UserServiceImpl.reg(String login, String password))")
    public void UserServiceImplReg() {
    }

    @Pointcut("@annotation(* UserServiceImpl.enter(String login))")
    public void userServiceImplEnter() {
    }

    @Pointcut("@annotation(* UserServiceImpl.viewAllUsers())")
    public void userServiceImplViewAllUsers() {
    }

    @Pointcut("@annotation(* UserServiceImpl.viewUserHistory(String login))")
    public void userServiceImplViewUserHistory() {
    }

    @Pointcut("@annotation(* UserServiceImpl.getByLogin(String login))")
    public void userServiceImplGetByLogin() {
    }


    @Pointcut("@annotation(* PointValueServiceImpl.submitData(String login, List<PointValue> list))")
    public void pointValueServiceImplSubmitData() {
    }

    @Pointcut("@annotation(* PointValueServiceImpl.viewLastData(String login))")
    public void pointValueServiceImplViewLastData() {
    }

    @Pointcut("@annotation(* PointValueServiceImpl.viewDataForSpecMonth(String login, int month, int year))")
    public void pointValueServiceImplViewDataForSpecMonth() {
    }

    @Pointcut("@annotation(* PointValueServiceImpl.viewDataHistory(String login))")
    public void pointValueServiceImplViewDataHistory() {
    }

    @Pointcut("@annotation(* PointValueServiceImpl.toOut(String login))")
    public void pointValueServiceImplToOut() {
    }

    @Pointcut("@annotation(* PointValueServiceImpl.validationOnceInMonth(String login))")
    public void pointValueServiceImplValidationOnceInMonth() {
    }

    @Around("execution(* *(..))")
    public void logCallMethod(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.debug("Call method " + methodName + " " + LocalDateTime.now());
        try {
            joinPoint.proceed();
            log.debug("End method " + methodName + " " + LocalDateTime.now());
        } catch (Throwable e) {
            log.error("Exception:", e);
        }
    }
}
