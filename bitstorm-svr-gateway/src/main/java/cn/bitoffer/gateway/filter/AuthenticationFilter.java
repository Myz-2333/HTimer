package cn.bitoffer.gateway.filter;

import cn.bitoffer.common.utils.jwt.JwtUtil;
import com.alibaba.nacos.common.utils.MapUtil;
import com.alibaba.nacos.common.utils.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.ServletRequestPathUtils;

import java.util.Map;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("missing authorization header");
            }
            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            if (null != authHeader && authHeader.startsWith("Bearer ")) {
                authHeader = authHeader.substring(7);
            }

            Map<String,Object> tokenInfo;
            try {
                tokenInfo = JwtUtil.validateToken(authHeader);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("un authorized access to application");
            }
            if(MapUtil.isEmpty(tokenInfo) ||
                    StringUtils.isEmpty((String)tokenInfo.get("userId"))
            ){
                throw new RuntimeException("un authorized access to application. tokenInfo err or userid is empty");
            }
            String userId = (String)tokenInfo.get("userId");
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header("X-User-Id",userId).build();
            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
            return chain.filter(modifiedExchange);
        });
    }

    public static class Config {

    }
}

