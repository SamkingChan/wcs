package com.ridko.wcs.domain.common;

/**
 * @Author SexyChan
 * @Date 2018/12/01 14:32
 */
public class ValidateOutResult {
    private String productNo;
    private String selColor;
    private String colorName;
    private String prodName;
    private String vatNo;
    private String fabRoll;
    private String weightOut;
    private String result;
    private String epc;
    private String bizNo;
    private String referBizNo;
    private String checkType;
    private String outpId;

    public String getOutpId() {
        return outpId;
    }

    public void setOutpId(String outpId) {
        this.outpId = outpId;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public ValidateOutResult() {
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getSelColor() {
        return selColor;
    }

    public void setSelColor(String selColor) {
        this.selColor = selColor;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public String getFabRoll() {
        return fabRoll;
    }

    public void setFabRoll(String fabRoll) {
        this.fabRoll = fabRoll;
    }

    public String getWeightOut() {
        return weightOut;
    }

    public void setWeightOut(String weightOut) {
        this.weightOut = weightOut;
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

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getReferBizNo() {
        return referBizNo;
    }

    public void setReferBizNo(String referBizNo) {
        this.referBizNo = referBizNo;
    }
}
