package cn.bitoffer.shorturlx.controller;

import cn.bitoffer.shorturlx.common.ResponseEntity;
import cn.bitoffer.shorturlx.service.ShortUrlXService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("/shorturlx")
@Slf4j
public class ShortUrlXController {

    @Autowired
    private ShortUrlXService shortUrlService;

    // v1
    @GetMapping("/v1/{shortUrl}")
    public void redirectToLongUrlV1(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = shortUrlService.getV1LongUrl(shortUrl);
        if (longUrl == null) {
            log.info("redirectToLongUrlV1: {} 此短链无效", shortUrl);
            response.setStatus(404);
            return;
        }
        sendRedirect(longUrl, response);
    }

    @PostMapping("/v1/shorten")
    public ResponseEntity<String> createShortUrlV1(@RequestBody createShortUrlRequest request) {
        String shortUrl = shortUrlService.createV1ShortUrl(request.longUrl);
        return ResponseEntity.ok(shortUrl);
    }

    // v2
    @GetMapping("/v2/{shortUrl}")
    public void redirectToLongUrlV2(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = shortUrlService.getV2LongUrl(shortUrl);
        sendRedirect(longUrl, response);
    }

    @PostMapping("/v2/shorten")
    public ResponseEntity<String> createShortUrlV2(@RequestBody createShortUrlRequest request) {
        String shortUrl = shortUrlService.createV2ShortUrl(request.longUrl);
        return ResponseEntity.ok(shortUrl);
    }

    @GetMapping("/v3/{shortUrl}")
    public void redirectToLongUrlV3(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = shortUrlService.getV3LongUrl(shortUrl);
        sendRedirect(longUrl, response);
    }


    @PostMapping("/v3/shorten")
    public ResponseEntity<String> createShortUrlV3(@RequestBody createShortUrlRequest request) {
        String shortUrl = shortUrlService.createV3ShortUrl(request.longUrl);
        return ResponseEntity.ok(shortUrl);
    }

    // ping
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }

    // 进行重定向的函数
    public void sendRedirect(String longUrl, HttpServletResponse response) throws IOException {
        response.sendRedirect(longUrl);
        response.setHeader("Location", longUrl);
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

    public static class createShortUrlRequest {
        public String longUrl;
    }
}
