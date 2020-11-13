package jp.interceptor;

import com.alibaba.fastjson.JSONObject;
import jp.anno.NoStandParam;
import jp.anno.StandJsonParam;
import jp.exception.CustomException;
import jp.utils.CommonUtils;
import jp.utils.JsonUtils;
import jp.utils.ResultVoUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class VisitAspect {

    @Pointcut("@annotation(jp.anno.NoStandParam)")
    public void pointCut() {
    }

    @Pointcut("@annotation(jp.anno.StandJsonParam)")
    public void standJsonPointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String customerIP = CommonUtils.getIpAddress(request);
        System.out.println("访问IP:" + customerIP);

        String localIp = CommonUtils.getLocalIp();
        System.out.println("当前服务IP:" + localIp);

        //获取被代理的方法名
        String methodName = joinPoint.getSignature().getName();
        System.out.println("访问接口方法:" + methodName);
        //获取被代理对象
        Class<?> tarClass = joinPoint.getTarget().getClass();
        //获取参数
        Object[] arguments = joinPoint.getArgs();
        //描述
        StringBuilder description = new StringBuilder();
        String needShowField = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object result = null;

        try {
            Method method = tarClass.getDeclaredMethod(methodName, methodSignature.getParameterTypes());
            NoStandParam annotation = method.getAnnotation(NoStandParam.class);
            if (annotation != null) {
                description = new StringBuilder(annotation.description());
            }

            System.out.println("description:" + description);
            System.out.println("acceptData:" + JSONObject.toJSONString(arguments));

            result = joinPoint.proceed();
            System.out.println("resultData:" + JSONObject.toJSONString(result));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
        }
        return result;
    }

    @Around("standJsonPointCut()")
    public Object standAround(ProceedingJoinPoint joinPoint) {

        System.out.println("标准JSON");
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String ip = CommonUtils.getIpAddress(request);
        System.out.println("访问IP:" + ip);

        //获取被代理的方法名
        String methodName = joinPoint.getSignature().getName();
        System.out.println(methodName);
        //获取被代理对象
        Class<?> tarClass = joinPoint.getTarget().getClass();
        //获取参数
        Object[] arguments = joinPoint.getArgs();
        //描述
        StringBuilder description = new StringBuilder();
        String needShowField = null;
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object result = null;

        try {
            Method method = tarClass.getDeclaredMethod(methodName, methodSignature.getParameterTypes());
            StandJsonParam annotation = method.getAnnotation(StandJsonParam.class);
            if (annotation != null) {
                description = new StringBuilder(annotation.description());
            }

            System.out.println("description:" + description);

            //将参数解析成JSON：arguments[0]
            String acceptData = JsonUtils.streamToJsonData(request);

            System.out.println("acceptData:" + JSONObject.parseObject(acceptData));

            result = joinPoint.proceed();
            System.out.println("resultData:" + JSONObject.toJSONString(result));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return ResultVoUtil.error(((CustomException) throwable).getCode(), ((CustomException) throwable).getMessage());
        } finally {
        }
        return result;
    }
}
