package org.song.enctyption.encryptionapply.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.song.encription.basedemo.utils.aes.Encryption;
import org.song.encription.basedemo.utils.des.DESHelper;
import org.song.enctyption.encryptionapply.annotations.SecurityParameter;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * ResponseBodyAdvice : 可以对@ResponseBody的返回值进行加工处理，它是一个接口类，
 * 具体处理可以自定义实现类注入到responseBodyAdviceBeans中既可，注入过程由RequestMappingHandlerAdapter类的initControllerAdviceCache去做，
 * 开发者只需要自定义实现类实现ResponseBodyAdvice 接口并添加类注解@ControllerAdvice
 * ResponseBodyAdviceChain : 维护ResponseBodyAdvice列表，循环调用所有的ResponseBodyAdvice
 *
 * @description: 响应数据加密类
 * @author: jiali.song@song.com
 * @date: 2018年07月18日 18:28:19
 **/
@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {

    private final static Logger logger = LoggerFactory.getLogger(MyResponseBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
            //获取注解配置的包含和去除字段
            SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
            //出参是否需要加密
            if (serializedField.outEncode()) {
                logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】响应数据进行加密");
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    // 待加密数据
                    String result = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
                    // 已加密
                    return DESHelper.encrypt(result);

                    // 使用 AES
//                    return Encryption.encode4http(result);

                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】响应数据加密出现异常：" + e.getMessage());
                }
            }
        }
        return body;
    }
}