package com.yunnzh.cms.common;

import com.yunnzh.cms.util.JsonUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>All rights Reserved, Designed By YNZH.</p>
 *
 * @Copyright: Copyright(C) 2018.
 * @Company: HQYG.
 * @author: luoliyuan
 * @Createdate: 2018/2/12 14:42
 */
@Aspect
@Component
public class ControllerAspectAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAspectAdvice.class);

    @Pointcut("execution(public * com.ynzh.houtai.web.*.*(..)) && !execution(public * com.yunnzh.cms.web.IndxController.*(..))")
    public void controllerAround() {
    }

    @Around("controllerAround()")
    public Object doControllerAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String clzName = signature.toShortString();
        Logger logger = LoggerFactory.getLogger(pjp.getTarget().getClass());

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequestWrapper request = (HttpServletRequestWrapper) servletRequestAttributes.getRequest();
        HttpServletResponseWrapper response = (HttpServletResponseWrapper) servletRequestAttributes.getResponse();

        if (request == null) {
            return pjp.proceed();
        }

        Map<String, String> paramMap = getRequestParam(request);
        String content = getRequestContent(request);

        String input = String.format("httpServletRequest-[url:{},X-Real-IP:{},$http_true_client_ip:{}, remoteAddr:{},paramsMap:{}, bodyContent:{}]",
                request.getRequestURL(), request.getHeader("X-Real-IP"), request.getHeader("$http_true_client_ip"),request.getRemoteAddr(),
                JsonUtils.toJson(paramMap), content);
        logger.info("{} : input {}", clzName, input);
        long start = System.currentTimeMillis();

        String resultMap = null;
        String returnUrl = null;

        try{
            Object result = pjp.proceed();
            if (result instanceof String){
                returnUrl = (String) result;
            }else if (result instanceof ModelAndView){
                Map<String, Object> model = ((ModelAndView) result).getModel();
                resultMap = JsonUtils.toJson(((ModelAndView) result).getModel());
                returnUrl = ((ModelAndView) result).getViewName();
            }else {
                RequestMapping requestMappingAnnotation = signature.getMethod().getAnnotation(RequestMapping.class);
                if(requestMappingAnnotation != null && requestMappingAnnotation.produces() != null && requestMappingAnnotation.produces().length > 0){
                    response.setContentType(requestMappingAnnotation.produces()[0]);
                }
                if(Objects.nonNull(result)){
                    if(result instanceof String){
                        response.getWriter().write(String.valueOf(result));
                    }else{
                        if(signature.getMethod().getAnnotation(ResponseBody.class) != null){
                            response.setContentType("application/json");
                        }
                        response.getWriter().write(JsonUtils.toJson(result));
                    }
                }
            }
            return result;
        }catch (Exception e){

        }finally {
            //出参日志
            String output = String.format("httpServletResponse-[redirectUrl: {}], result: {}", returnUrl, resultMap);
            logger.info("{} output: {}, execute time: {}ms", clzName, output, System.currentTimeMillis() - start);
        }


        return null;
    }

    private Map<String, String> getRequestParam(HttpServletRequest request) {
        Map<String, String[]> paramsMap = request.getParameterMap();
        Map<String, String> result = new HashMap<>(paramsMap.size());
        paramsMap.forEach((key, value) -> result.put(key, value[0]));
        return result;
    }

    private static final int BUFFER_SIZE = 1024;

    private String getRequestContent(HttpServletRequest request) {
        try {
            InputStream is = request.getInputStream();
            FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = is.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            if (buffer.length != 0) {
                return new String(buffer, request.getCharacterEncoding());
            }
        } catch (IOException e) {
            logger.error("request content error", e);
        }

        return null;
    }
}
