package com.yunnzh.cms.web.result;

/**
 * <p>All rights Reserved, Designed By HQYG.</p>
 *
 * @Copyright: Copyright(C) 2016.
 * @Company: HQYG.
 * @author: luoliyuan
 * @Createdate: 2018/2/217:06
 */
public enum ErrorCode {
    PARAMETER_ERROR(10000, "parameter error"),
    OBJECT_NOT_EXIST(10001,"object not exist","%s not exist"),
    UNKOWN_ERROR(99999,"system unkown");

    private int code;
    private String message;
    private String extendMessage;

    ErrorCode(int code, String message) {
        this(code,message,"");
    }

    ErrorCode(int code, String message, String extendMessage) {

        this.code = code;
        this.message = message;
        this.extendMessage = extendMessage;
    }

    public BusinessException getException(){
        return new BusinessException(this);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getExtendMessage() {
        return extendMessage;
    }
}
