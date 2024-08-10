package cn.bitoffer.shorturlx.mapper;


import cn.bitoffer.shorturlx.model.UrlMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UrlMapMapper {


    void dbCreate(@Param("urlMap") UrlMap urlMap);

    String dbGetShortUrl(@Param("longUrl") String longUrl);
    String dbGetLongUrl(@Param("shortUrl") String shortUrl);
    void dbUpdate(@Param("shortUrl") String shortUrl, @Param("longUrl") String longUrl);
}
