package org.song.enctyption.encryptionapply.advice;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.song.encription.basedemo.utils.aes.Encryption;
import org.song.encription.basedemo.utils.des.DESHelper;
import org.song.enctyption.encryptionapply.annotations.SecurityParameter;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * 注解@ControllerAdvice 的作用
 * 1. ModelAttribute: 暴露@RequestMapping 方法返回值为模型数据：放在功能处理方法的返回值上时，是暴露功能处理方法的返回值为模型数据，用于视图页面展示时使用。
 * 2. InitBinder : 用于自定义@RequestMapping 方法参数绑定
 * 3. ResponseBodyAdvice : 用于@ResponseBody返回值增加处理
 *
 * @description: 请求数据解密
 * @author: jiali.song@song.com
 * @date: 2018年07月18日 17:46:08
 **/
@ControllerAdvice
public class MyRequestBodyAdvice implements RequestBodyAdvice {

    private static final Logger logger = LoggerFactory.getLogger(MyRequestBodyAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            if (methodParameter.getMethod().isAnnotationPresent(SecurityParameter.class)) {
                //获取注解配置的包含和去除字段
                SecurityParameter serializedField = methodParameter.getMethodAnnotation(SecurityParameter.class);
                //入参是否需要解密
                if (serializedField.inDecode()) {
                    logger.info("对方法method :【" + methodParameter.getMethod().getName() + "】请求数据进行解密");
                    return new MyHttpInputMessage(inputMessage);
                }
            }
            return inputMessage;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("对方法method :【" + methodParameter.getMethod().getName() + "】返回数据进行解密出现异常：" + e.getMessage());
            return inputMessage;
        }
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    class MyHttpInputMessage implements HttpInputMessage {
        private HttpHeaders headers;

        private InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
            this.headers = inputMessage.getHeaders();
            // 待解密内容
            String inputString = IOUtils.toString(inputMessage.getBody(), "UTF-8");
            // 解密后内容
            String decrypt = DESHelper.decrypt(inputString);
            this.body = IOUtils.toInputStream(decrypt, "UTF-8");

            // 使用 AES
//            String decode4http = Encryption.decode4http(inputString);
//            this.body = IOUtils.toInputStream(decrypt, "UTF-8");
        }

        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}