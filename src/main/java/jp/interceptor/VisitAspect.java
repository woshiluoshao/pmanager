package jp.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jp.anno.NoStandParam;
import jp.anno.StandJsonParam;
import jp.db.dao.IDaoImpl;
import jp.entity.UserOperationLogEntity;
import jp.exception.CustomException;
import jp.utils.CommonUtils;
import jp.utils.DateUtils;
import jp.utils.JsonUtils;
import jp.utils.ResultVoUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

@Aspect
@Component
public class VisitAspect {

    @Autowired(required = false)
    ThreadPoolExecutor poolExecutor;

    @Autowired
    IDaoImpl daoImpl;

    @Pointcut("@annotation(jp.anno.StandJsonParam)")
    public void standJsonPointCut() {
    }

    @Pointcut("@annotation(jp.anno.NoStandParam)")
    public void pointCut() {
    }

    @Around("standJsonPointCut()")
    public Object standAround(ProceedingJoinPoint joinPoint) {

        Object result = aspectFormVisit(joinPoint, 1);
        return result;
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {

        Object result = aspectFormVisit(joinPoint, 2);
        return result;
    }

    private Object aspectFormVisit(ProceedingJoinPoint joinPoint, int flag) {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        UserOperationLogEntity logEntity = new UserOperationLogEntity();

        String customerIP = CommonUtils.getIpAddress(request);
        System.out.println("访问IP:" + customerIP);
        logEntity.setVisitIp(customerIP);

        String serverIp = CommonUtils.getLocalIp();
        System.out.println("当前服务IP:" + serverIp);
        logEntity.setServerIp(serverIp);

        String actionUrl = request.getRequestURI();
        System.out.println("请求地址:" + actionUrl);
        logEntity.setActionUrl(actionUrl);

        //获取被代理的方法名
        String methodName = joinPoint.getSignature().getName();
        System.out.println("访问接口方法:" + methodName);
        logEntity.setMethodName(methodName);

        StringBuilder module = new StringBuilder();
        StringBuilder methods = new StringBuilder();

        //获取被代理对象
        Class<?> tarClass = joinPoint.getTarget().getClass();
        //获取参数
        Object[] arguments = joinPoint.getArgs();
        //描述

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object result = null;

        try {
            Method method = tarClass.getDeclaredMethod(methodName, methodSignature.getParameterTypes());

            if (flag == 1) {
                StandJsonParam annotation = method.getAnnotation(StandJsonParam.class);
                module = new StringBuilder(annotation.module());
                methods = new StringBuilder(annotation.methods());

                //将参数解析成JSON：arguments[0]
                String acceptData = JsonUtils.streamToJsonData(request);
                System.out.println("acceptData:" + acceptData);
                logEntity.setAcceptData(acceptData);
            } else {
                NoStandParam annotation = method.getAnnotation(NoStandParam.class);
                module = new StringBuilder(annotation.module());
                methods = new StringBuilder(annotation.methods());
                //将参数解析成JSON
                String acceptData = JSON.toJSONString(arguments);
                System.out.println("acceptData:" + acceptData);
                logEntity.setAcceptData(acceptData);
            }

            System.out.println("module:" + module);
            System.out.println("methods:" + methods);
            logEntity.setModule(module.toString());
            logEntity.setMethods(methods.toString());

            result = joinPoint.proceed();
            System.out.println("resultData:" + JSONObject.toJSONString(result));
            logEntity.setReturnData(JSONObject.toJSONString(result));
            logEntity.setComments(1);
            logEntity.setCreateTime(DateUtils.getCurrentTime());

        } catch (Throwable throwable) {
            //throwable.printStackTrace();
            System.out.println("拦截器拦截到异常啦:" + throwable.getMessage());
            String code = ((CustomException) throwable).getCode();
            String msg = ((CustomException) throwable).getMessage();
            logEntity.setReturnData(JSONObject.toJSONString(ResultVoUtil.error(code , msg)));
            logEntity.setCreateTime(DateUtils.getCurrentTime());
            logEntity.setComments(2);
            return ResultVoUtil.error(code , msg);
        } finally {
            //插入日志(执行日志)
            //poolExecutor.submit(()-> {
                daoImpl.insertUserOperaLog(logEntity);
           // });
        }

        return result;
    }
}
