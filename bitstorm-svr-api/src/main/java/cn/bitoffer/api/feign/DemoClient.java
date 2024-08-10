package cn.bitoffer.api.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("bitstorm-svr-demo")
public interface DemoClient {

    /**
     * demo 服务接口
     */
    @GetMapping(value = "/call")
    public String call(@RequestParam("name") String name);

}
