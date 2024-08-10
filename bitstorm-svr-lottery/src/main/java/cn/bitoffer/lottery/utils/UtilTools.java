package cn.bitoffer.lottery.utils;

import java.util.Random;

public class UtilTools {
    public static int getRandom(int maxValue) {
        Random random = new Random();
        //设置随机数种子
        random.setSeed(System.currentTimeMillis());
        return random.nextInt(maxValue);
    }

    public static long ipToLong(String ipAddress) {
        String[] ipAddressParts = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressParts.length; i++) {
            int part = Integer.parseInt(ipAddressParts[i]);
            result += part * Math.pow(256, 3 - i);
        }
        return result;
    }
}
