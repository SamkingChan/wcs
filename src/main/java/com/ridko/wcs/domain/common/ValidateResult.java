package com.ridko.wcs.domain.common;

/**
 * @Author SexyChan
 * @Date 2018/11/27 20:02
 */
public class ValidateResult {

    private String bizNo;
    private String checkType;
    private String result;
    private String epc;
    private String referBizNo;

    public String getReferBizNo() {
        return referBizNo;
    }

    public void setReferBizNo(String referBizNo) {
        this.referBizNo = referBizNo;
    }

    public ValidateResult() {
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }
}
