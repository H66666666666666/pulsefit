package com.itbin.aop;

import com.alibaba.fastjson.JSONObject;
import com.itbin.mapper.OperateLogMapper;
import com.itbin.pojo.OperateLog;
import com.itbin.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 操作日志切面类
 * 用于记录系统所有增、删、改功能接口的操作日志
 */
@Slf4j
@Aspect
@Component
public class OperateLogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    /**
     * 环绕通知：拦截controller包中的所有增删改方法
     * 拦截@PostMapping、@PutMapping、@DeleteMapping注解的方法
     */
    @Around("@annotation(com.itbin.anno.Log)")
    public Object recordOperateLog(ProceedingJoinPoint pjp) throws Throwable {
        // 1. 获取操作人ID（从请求头中获取token并解析）
        Integer operateEmpId = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("token");
                if (token != null && !token.isEmpty()) {
                    Claims claims = JwtUtils.parseToken(token);
                    operateEmpId = (Integer) claims.get("id");
                }
            }
        } catch (Exception e) {
            log.error("获取操作人ID失败", e);
        }

        // 2. 记录操作开始时间
        long beginTime = System.currentTimeMillis();

        // 3. 执行目标方法
        Object result = pjp.proceed();

        // 4. 记录操作结束时间
        long endTime = System.currentTimeMillis();

        // 5. 组装日志对象
        OperateLog operateLog = new OperateLog();
        operateLog.setOperateEmpId(operateEmpId); // 操作人ID
        operateLog.setOperateTime(LocalDateTime.now()); // 操作时间
        operateLog.setClassName(pjp.getTarget().getClass().getName()); // 目标类的全类名
        operateLog.setMethodName(pjp.getSignature().getName()); // 目标方法名
        
        // 方法参数（转换为JSON字符串）
        Object[] args = pjp.getArgs();
        String methodParams = Arrays.toString(args);
        // 如果参数过长，进行截取（数据库字段长度限制为2000）
        if (methodParams.length() > 2000) {
            methodParams = methodParams.substring(0, 2000);
        }
        operateLog.setMethodParams(methodParams);
        
        // 返回值（转换为JSON字符串）
        String returnValue = JSONObject.toJSONString(result);
        // 如果返回值过长，进行截取
        if (returnValue.length() > 2000) {
            returnValue = returnValue.substring(0, 2000);
        }
        operateLog.setReturnValue(returnValue);
        
        operateLog.setCostTime(endTime - beginTime); // 方法执行耗时（毫秒）

        // 6. 保存日志到数据库
        try {
            operateLogMapper.insert(operateLog);
            log.info("操作日志记录成功：{}", operateLog);
        } catch (Exception e) {
            log.error("操作日志记录失败", e);
        }

        // 7. 返回目标方法的执行结果
        return result;
    }
}
