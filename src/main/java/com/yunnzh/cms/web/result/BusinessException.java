package com.yunnzh.cms.web.result;

/**
 * <p>All rights Reserved, Designed By HQYG.</p>
 *
 * @Copyright: Copyright(C) 2016.
 * @Company: HQYG.
 * @author: luoliyuan
 * @Createdate: 2018/2/217:14
 */
public class BusinessException extends RuntimeException {
    private int code;
    private String message;

    public BusinessException(ErrorCode errorCode){
        this.code =errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
