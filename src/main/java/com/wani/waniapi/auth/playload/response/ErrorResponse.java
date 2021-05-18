package com.wani.waniapi.auth.playload.response;

public class ErrorResponse {
    private Integer status;
    private String code;
    private String detail;

    public ErrorResponse(
            Integer status,
            String code,
            String detail
    ) {
        this.status = status;
        this.code = code;
        this.detail = detail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code){this.code = code;}

}
