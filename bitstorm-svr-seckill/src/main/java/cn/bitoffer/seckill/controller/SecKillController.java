package cn.bitoffer.seckill.controller;

import cn.bitoffer.seckill.common.ResponseEntity;
import cn.bitoffer.seckill.common.ResponseEnum;
import cn.bitoffer.seckill.model.Goods;
import cn.bitoffer.seckill.service.SecKillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/sec_kill")
@Slf4j
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    @GetMapping("/get_goods_info")
    public ResponseEntity<GetGoodsInfoData> getGoodsInfo(@RequestHeader("Trace-ID") String traceID,
                                                         @RequestHeader("User-ID") Long userID,
                                                         @RequestParam(name = "goodsNum") String goodsNum) {
        Goods goods = secKillService.getGoodsByNum(traceID, userID, goodsNum);
        GetGoodsInfoData data = new GetGoodsInfoData();
        data.goodsInfo = new GoodsInfo();
        data.goodsInfo.goodsNum = goods.getGoodsNum();
        data.goodsInfo.goodsName = goods.getGoodsName();
        data.goodsInfo.price = goods.getPrice();
        data.goodsInfo.picUrl = goods.getPicUrl();
        data.goodsInfo.seller = goods.getSeller();
        System.out.println(data);
        return ResponseEntity.resp(ResponseEnum.OK, data);
    }
    @GetMapping(value ="/get_goods_list")
    public ResponseEntity<GetGoodsListData> getGoodsList(@RequestHeader("Trace-ID") String traceID,
                                                         @RequestHeader("User-ID") Long userID,
                                                         @RequestParam(name = "offset") Integer offset,
                                                         @RequestParam(name = "limit") Integer limit) {
        if (limit == 0) {
            limit = 200;
        }
        ArrayList<Goods> goodsList = secKillService.getGoodsList(traceID, userID, offset, limit);
        GetGoodsListData data = new GetGoodsListData();
        data.goodsList = new ArrayList<GoodsInfo>();
        for (int i = 0; i < goodsList.size(); i++) {
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.goodsNum = goodsList.get(i).getGoodsNum();
            goodsInfo.goodsName = goodsList.get(i).getGoodsName();
            goodsInfo.price = goodsList.get(i).getPrice();
            goodsInfo.picUrl = goodsList.get(i).getPicUrl();
            goodsInfo.seller = goodsList.get(i).getSeller();
            data.goodsList.add(goodsInfo);
        }
        System.out.println(data);
        return ResponseEntity.resp(ResponseEnum.OK, data);
    }
    @PostMapping(value = "/v1/sec_kill", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> secKillV1(@RequestHeader("Trace-ID") String traceID,
                                            @RequestHeader("User-ID") Long userID,
                                            @RequestBody SecKillV1Req data) {
        String orderNum = "";
        //try {
        //    String goodsNum = data.goodsNum;
        //    Integer num = data.num;
        //    orderNum = secKillService.secKillV1(traceID, userID, goodsNum, num);
        //    System.out.println(orderNum);
        //} catch (Exception e) {
        //    System.out.println("seckill err " + e.getMessage());
        //}
        String goodsNum = data.goodsNum;
        Integer num = data.num;
        orderNum = secKillService.secKillV1(traceID, userID, goodsNum, num);
        System.out.println(orderNum);
        return ResponseEntity.ok(orderNum);
    }

    @PostMapping(value = "/v2/sec_kill", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> secKillV2(@RequestHeader("Trace-ID") String traceID,
                                            @RequestHeader("User-ID") Long userID,
                                            @RequestBody SecKillV1Req data) {
        String orderNum = "";
        try {
            String goodsNum = data.goodsNum;
            Integer num = data.num;
            orderNum = secKillService.secKillV2(traceID, userID, goodsNum, num);
            System.out.println(orderNum);
        } catch (Exception e) {
            System.out.println("seckill err " + e.getMessage());
        }
        return ResponseEntity.ok(orderNum);
    }

    @PostMapping(value = "/v3/sec_kill", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> secKillV3(@RequestHeader("Trace-ID") String traceID,
                                            @RequestHeader("User-ID") Long userID,
                                            @RequestBody SecKillV1Req data) {
        String secNum = "";
        try {
            String goodsNum = data.goodsNum;
            Integer num = data.num;
            secNum = secKillService.secKillV3(traceID, userID, goodsNum, num);
            System.out.println(secNum);
        } catch (Exception e) {
            System.out.println("seckill err " + e.getMessage());
        }
        return ResponseEntity.ok(secNum);
    }

    public static class SecKillV1Req {
        public String goodsNum;
        public Integer num;
    }



    public static class GetGoodsInfoData {
        public GoodsInfo goodsInfo;
    }

    public static class GetGoodsListData {
        public List<GoodsInfo> goodsList;
    }



    public static class GoodsInfo{
        public String goodsNum;
        public String goodsName;
        public Float price;
        public String picUrl;
        public Long seller;
    }
}