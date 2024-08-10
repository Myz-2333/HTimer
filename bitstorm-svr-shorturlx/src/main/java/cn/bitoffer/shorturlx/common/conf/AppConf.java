package cn.bitoffer.shorturlx.common.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppConf {

    @Value("${app.workId}")
    private int workId;

    @Value("${app.workerIdBits}")
    private int workerIdBits;
}


