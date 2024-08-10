package cn.bitoffer.api.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bitstorm-svr-leaf", path = "/bitstorm/feign/leaf")
public interface LeafFeignClient {

    /**
     * 获取id
     *
     * @param key 业务标识
     */
    @GetMapping(value = "/insider/segment")
    ResponseEntity<Long> getSegmentID(@RequestParam("key") String key);
}
