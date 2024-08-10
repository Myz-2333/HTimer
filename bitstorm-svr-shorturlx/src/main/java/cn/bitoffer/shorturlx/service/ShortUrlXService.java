package cn.bitoffer.shorturlx.service;

/**
 * @author 狂飙训练营
 */
public interface ShortUrlXService {

    String getV1LongUrl(String shortUrl);
    String getV2LongUrl(String shortUrl);

    String getV3LongUrl(String shortUrl);

    String createV1ShortUrl(String longUrl);

    String createV2ShortUrl(String longUrl);

    String createV3ShortUrl(String longUrl);

}
