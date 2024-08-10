package cn.bitoffer.lottery.controller;

import cn.bitoffer.lottery.common.ResponseEntity;
import cn.bitoffer.lottery.common.ResponseEnum;
import cn.bitoffer.lottery.model.LotteryResult;
import cn.bitoffer.lottery.service.LotteryService;
import cn.bitoffer.lottery.service.impl.LotteryServiceImpl1;
import cn.bitoffer.lottery.service.impl.LotteryServiceImpl2;
import cn.bitoffer.lottery.service.impl.LotteryServiceImpl3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/lottery")
@Slf4j
public class LotteryController {

    @Autowired
    private LotteryServiceImpl1 lotteryServiceImpl1;
    @Autowired
    private LotteryServiceImpl2 lotteryServiceImpl2;
    @Autowired
    private LotteryServiceImpl3 lotteryServiceImpl3;

    public static class LotteryReq{
        int userId;
        String userName;
        String ip;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }
    }

    public static class AddPrizeListReq {
        int userId;
        List<ViewPrize> viewPrizeList;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<ViewPrize> getViewPrizeList() {
            return viewPrizeList;
        }

        public void setViewPrizeList(List<ViewPrize> viewPrizeList) {
            this.viewPrizeList = viewPrizeList;
        }
    }

    public static class ImportCacheCouponReq {
        int userId;
        ViewCoupon viewCoupon;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public ViewCoupon getViewCoupon() {
            return viewCoupon;
        }

        public void setViewCoupon(ViewCoupon viewCoupon) {
            this.viewCoupon = viewCoupon;
        }
    }

    @PostMapping(value = "/v1/get_lucky", consumes = "application/json; charset=utf-8")
    public ResponseEntity<LotteryResult> lotteryV1( @RequestHeader("X-User-Id") Long tokenUserID,@RequestBody LotteryReq req) {
        LotteryResult lotteryResult = null;
        try {
            Long userID = null;
            if (tokenUserID != null) {
                userID = tokenUserID;
            }else {
                userID = new Long(req.userId);
            }
            String userName = req.userName;
            String ip = req.ip;
            lotteryResult = lotteryServiceImpl1.lottery(userID, userName, ip);
        } catch (Exception e) {
            System.out.println("lottery err " + e.getMessage());
            return ResponseEntity.fail();
        }
        return ResponseEntity.resp(ResponseEnum.OK, lotteryResult);
    }

    @PostMapping(value = "/v2/get_lucky", consumes = "application/json; charset=utf-8")
    public ResponseEntity<LotteryResult> lotteryV2(@RequestHeader("X-User-Id") Long tokenUserID,@RequestBody LotteryReq req) {
        LotteryResult lotteryResult = null;
        try {
            Long userID = null;
            if (tokenUserID != null) {
                userID = tokenUserID;
            }else {
                userID = new Long(req.userId);
            }
            String userName = req.userName;
            String ip = req.ip;
            lotteryResult = lotteryServiceImpl2.lottery(userID, userName, ip);
        } catch (Exception e) {
            System.out.println("lottery err " + e.getMessage());
            return ResponseEntity.fail();
        }
        return ResponseEntity.resp(ResponseEnum.OK, lotteryResult);
    }

    @PostMapping(value = "/v3/get_lucky", consumes = "application/json; charset=utf-8")
    public ResponseEntity<LotteryResult> lotteryV3(@RequestHeader("X-User-Id") Long tokenUserID,@RequestBody LotteryReq req) {
        LotteryResult lotteryResult = null;
        try {
            Long userID = null;
            if (tokenUserID != null) {
                userID = tokenUserID;
            }else {
                userID = new Long(req.userId);
            }
            String userName = req.userName;
            String ip = req.ip;
            lotteryResult = lotteryServiceImpl3.lottery(userID, userName, ip);

        } catch (Exception e) {
            System.out.println("lottery err " + e.getMessage());
            return ResponseEntity.fail();
        }
        return ResponseEntity.resp(ResponseEnum.OK, lotteryResult);
    }

    // 新增奖品列表
    @PostMapping(value = "/add_prize_list", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> addPrizeList(@RequestBody AddPrizeListReq req) {
        Long userId = new Long(req.userId);
        if (userId <= 0) {
            return ResponseEntity.fail();
        }
        lotteryServiceImpl1.addPrizeList(req.viewPrizeList);
        return ResponseEntity.ok();
    }

    @PostMapping(value = "/import_coupon_cache", consumes = "application/json; charset=utf-8")
    public ResponseEntity<String> ImportCacheCoupon(@RequestBody ImportCacheCouponReq req)  {
        Long userId = new Long(req.userId);
        if (userId <= 0) {
            return ResponseEntity.fail();
        }
        String code = req.viewCoupon.getCode();
        Long prizeId = new Long(req.viewCoupon.getPrizeId());
        try {
            lotteryServiceImpl2.importCouponWithCache(prizeId, code);
        } catch (Exception e) {
            log.info("import_coupon_cache err " + e.getMessage());
            return ResponseEntity.fail();
        }
        return ResponseEntity.ok();
    }

}