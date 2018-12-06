package com.ridko.wcs.domain.common;

/**
 * @Author SexyChan
 * @Date 2018/11/27 20:04
 */
public class ValidateWebResult {
    private String fab_roll;
    private String weight_in;
    private String exception;
    private String result;
    private String epc;

    public ValidateWebResult() {
    }

    public String getEpc() {
        return epc;
    }

    public void setEpc(String epc) {
        this.epc = epc;
    }

    public String getFab_roll() {
        return fab_roll;
    }

    public void setFab_roll(String fab_roll) {
        this.fab_roll = fab_roll;
    }

    public String getWeight_in() {
        return weight_in;
    }

    public void setWeight_in(String weight_in) {
        this.weight_in = weight_in;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
