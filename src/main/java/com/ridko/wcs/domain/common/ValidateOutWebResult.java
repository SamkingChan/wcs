package com.ridko.wcs.domain.common;

/**
 * @Author SexyChan
 * @Date 2018/12/01 14:32
 */
public class ValidateOutWebResult {
    private String product_no;
    private String prod_name;
    private String sel_color;
    private String color_name;
    private String vat_no;
    private String fab_roll;
    private String weight_out;
    private String result;
    private String epc;
    private String exception;

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public ValidateOutWebResult() {
    }

    public String getProduct_no() {
        return product_no;
    }

    public void setProduct_no(String product_no) {
        this.product_no = product_no;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getSel_color() {
        return sel_color;
    }

    public void setSel_color(String sel_color) {
        this.sel_color = sel_color;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getVat_no() {
        return vat_no;
    }

    public void setVat_no(String vat_no) {
        this.vat_no = vat_no;
    }

    public String getFab_roll() {
        return fab_roll;
    }

    public void setFab_roll(String fab_roll) {
        this.fab_roll = fab_roll;
    }

    public String getWeight_out() {
        return weight_out;
    }

    public void setWeight_out(String weight_out) {
        this.weight_out = weight_out;
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
