package com.itbin.aop;

import com.alibaba.fastjson.JSONObject;
import com.itbin.mapper.OperationLogMapper;
import com.itbin.pojo.OperationLog;
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
 * PulseFit operation log aspect
 * Records all create/update/delete operations for audit
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;

    @Around("@annotation(com.itbin.anno.Log)")
    public Object recordOperationLog(ProceedingJoinPoint pjp) throws Throwable {
        // 1. Get operator ID from JWT token
        Integer operatorId = null;
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String token = request.getHeader("token");
                if (token != null && !token.isEmpty()) {
                    Claims claims = JwtUtils.parseToken(token);
                    operatorId = (Integer) claims.get("id");
                }
            }
        } catch (Exception e) {
            log.error("获取操作人ID失败", e);
        }

        // 2. Record start time
        long beginTime = System.currentTimeMillis();

        // 3. Execute target method
        Object result = pjp.proceed();

        // 4. Record end time
        long endTime = System.currentTimeMillis();

        // 5. Build operation log
        OperationLog operationLog = new OperationLog();
        operationLog.setOperatorId(operatorId);
        operationLog.setOperationTime(LocalDateTime.now());
        operationLog.setClassName(pjp.getTarget().getClass().getName());
        operationLog.setMethodName(pjp.getSignature().getName());

        Object[] args = pjp.getArgs();
        String methodParams = redactPassword(Arrays.toString(args));
        if (methodParams.length() > 2000) {
            methodParams = methodParams.substring(0, 2000);
        }
        operationLog.setMethodParams(methodParams);

        String returnValue = redactPassword(JSONObject.toJSONString(result));
        if (returnValue.length() > 2000) {
            returnValue = returnValue.substring(0, 2000);
        }
        operationLog.setReturnValue(returnValue);
        operationLog.setCostTime(endTime - beginTime);

        // 6. Save to pf_operation_log
        try {
            operationLogMapper.insert(operationLog);
            log.info("操作日志记录成功：{}", redactPassword(operationLog.toString()));
        } catch (Exception e) {
            log.error("操作日志记录失败", e);
        }

        return result;
    }

    /**
     * Redact password values from serialized strings.
     * Covers both JSON-style ("password":"value") and toString-style (password=value) representations.
     */
    private String redactPassword(String text) {
        if (text == null) {
            return null;
        }
        // JSON-style: "password":"value" or "password": "value"
        text = text.replaceAll("\"password\"\\s*:\\s*\"[^\"]*\"", "\"password\":\"[REDACTED]\"");
        // toString-style: password=value (until comma, whitespace, closing paren, or brace)
        text = text.replaceAll("password=[^,\\s)}]+", "password=[REDACTED]");
        return text;
    }
}
