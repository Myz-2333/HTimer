package cn.bitoffer.api.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

@Component
@Slf4j
public class ContextFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        log.error("hello hello hellohello");
        // 从请求上下文中获取userId
        // 从请求头中获取 X-User-Id
        List<String> userId= (List<String>)template.headers().get("X-User-Id");
        if (userId != null) {
            // 将 userId 放入上下文（Context）中
            //MyContextHolder.setUserId(userId);
        }
        if (userId != null) {
            // 将userId添加到请求头中，以便传递给下游的微服务
            template.header("X-User-Id", userId);
        }
    }
}

