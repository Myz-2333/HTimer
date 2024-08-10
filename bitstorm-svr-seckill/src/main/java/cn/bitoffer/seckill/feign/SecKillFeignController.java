package cn.bitoffer.seckill.feign;

import cn.bitoffer.seckill.service.SecKillService;
import cn.bitoffer.api.feign.TestSeckillClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class SecKillFeignController implements TestSeckillClient {

    @Autowired
    private SecKillService secKillService;
    public String call(String name) {
        return "";
    }
}
