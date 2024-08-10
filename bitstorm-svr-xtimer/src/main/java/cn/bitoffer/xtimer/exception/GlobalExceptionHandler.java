package cn.bitoffer.xtimer.exception;

import cn.bitoffer.common.model.ResponseEntity;
import cn.bitoffer.xtimer.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResponseEntity.failBusinessException(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResponseEntity.fail();
    }
}
