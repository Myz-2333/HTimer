package cn.bitoffer.gateway.data;

import lombok.Data;


/**
 * @date 2022/4/29 全局过滤异常处理
 */

@Data
public class ApiResult<T> {
    public static final String FAIL_CODE = "0";		//失败码
    public static final String SUC_CODE = "1";		//成功码
    public static final String ERROR_CODE = "2";	//错误码
    public static final String SUC_MESSAGE = "Operate successfully";	//成功信息
    public static final String FAIL_MESSAGE = "Operation failure";		//失败信息
    public static final String ERROR_MESSAGE = "System Error";			//错误信息
    public static final String NOACCESS_MESSAGE = "No permission to access this page.";		//无权操作

    /**
     * 代码
     * 0 失败
     * 1 成功
     * 2 错误
     * @mock 1
     */
    private String code = FAIL_CODE;
    /**
     * 信息（对应code）
     * Operate successfully
     * Operation failure
     * System Error
     * @mock Operate successfully
     */
    private String message = FAIL_MESSAGE;
    /**
     * 模型
     */
    private T data;


    public void setCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCodeToSuccessed() {
        this.code = SUC_CODE;
        this.message = SUC_MESSAGE;
    }
    public void setCodeToSuccessed(T data) {
        this.data = data;
        this.code = SUC_CODE;
        this.message = SUC_MESSAGE;
    }

    public void setCodeToError(String message) {
        this.code = ERROR_CODE;
        this.message = message;
    }

    public void setCodeToError() {
        this.code = ERROR_CODE;
        this.message = ERROR_MESSAGE;
    }

    public void setCodeToFail(String message) {
        this.code = ERROR_CODE;
        this.message = message;
    }

    public void setCodeToFail() {
        this.code = FAIL_CODE;
        this.message = FAIL_MESSAGE;
    }

    public void setCodeByNoAccess() {
        this.code = FAIL_CODE;
        this.message = NOACCESS_MESSAGE;
    }

    public boolean isSuccess() {
        return SUC_CODE.equals(code);
    }

}


