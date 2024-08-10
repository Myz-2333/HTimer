package cn.bitoffer.lottery.mapper;

import cn.bitoffer.lottery.model.BlackUser;
import cn.bitoffer.lottery.model.Coupon;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface CouponMapper {
    ArrayList<Coupon> getAll();

    ArrayList<Coupon> getCouponListByPrizeID(@Param("prizeID") Long prizeID);

    int save(@Param("coupon") Coupon coupon);

    void updateByCode(@Param("code") String code);

    Coupon getGetNextUsefulCoupon(@Param("prizeID") Long prize,@Param("couponID") Long couponID);

}
