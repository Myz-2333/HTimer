package cn.bitoffer.shorturlx.utils;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Base62 {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();
    private static String shuffledAlphabet;

    private Base62() {
        // 私有构造函数
        shuffledAlphabet = shuffleString();
    }

    public String generateShortUrl(Long id) {
        return base62Encode(id);
    }
    private static String base62Encode(Long decimal) {
        StringBuilder result = new StringBuilder();
        while (decimal > 0) {
            int remainder = (int) (decimal % 62);
            result.append(shuffledAlphabet.charAt(remainder));
            decimal /= 62;
        }
        return result.reverse().toString();
    }
    private static String shuffleString() {
        char[] chars = Base62.ALPHABET.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
        return new String(chars);
    }
}