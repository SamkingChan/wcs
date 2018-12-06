package com.ridko.wcs.controller.common;

import com.google.gson.Gson;
import com.ridko.wcs.domain.common.ValidateOutResult;
import com.ridko.wcs.domain.common.ValidateOutWebResult;
import com.ridko.wcs.domain.common.ValidateResult;
import com.ridko.wcs.domain.common.ValidateWebResult;
import com.ridko.wcs.utils.HttpClientUtil;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author SexyChan
 * @Date 2018/11/26 10:35
 */
@RestController
@RequestMapping("/data")
@CrossOrigin
public class DataController {

    @Value("${wms.ip}")
    private String wmsIp = "47.107.112.133";
//    private String wmsIp = "192.168.43.33";

    @Value("${wms.port}")
    private String wmsPort = "8080";

    private String url = "http://" + wmsIp + ":" + wmsPort + "/wms/";

    @Autowired
    private FutureConnection futureConnection;

    private Map getData;
    private Map map;

    private Gson gson = new Gson();
    private List<String> oldEpc = new CopyOnWriteArrayList<>();
    private List<ValidateWebResult> validateWebResultList = new CopyOnWriteArrayList<>();
    private List<ValidateResult> validateResultList = new CopyOnWriteArrayList<>();

    /**
     * 获取入库单
     *
     * @param inOrder 入库单号
     * @return
     */
    @GetMapping("/getInOrder/{inOrder}")
    public ResponseEntity getPrintData(@PathVariable String inOrder) {
        try {
            String json = HttpClientUtil.doGet(url + "product/in/" + inOrder);
            if (json != null && !(json.equals(""))) {
                return ResponseEntity.status(200).body(json);
            }
            return ResponseEntity.status(400).body("获取失败，数据为空");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("获取失败" + e.getMessage());
        }
    }

    /**
     * 获取库存单号
     *
     * @param vatDye
     * @return
     */
    @GetMapping("/getStoreByVatDye/{vatDye}")
    public ResponseEntity getStoreByVatDye(@PathVariable String vatDye) {
        try {
            String json = HttpClientUtil.doGet(url + "product/inByVatno/" + vatDye);
            if (json != null && !(json.equals(""))) {
                return ResponseEntity.status(200).body(json);
            }
            return ResponseEntity.status(400).body("获取失败，数据为空");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("获取失败" + e.getMessage());
        }
    }

    @GetMapping("/getInNoListByVatDye/{vatDye}")
    public ResponseEntity getInNoListByVatDye(@PathVariable String vatDye) {
        try {
            String vatDyeEncoder = URLEncoder.encode(vatDye, "UTF-8");
            String json = HttpClientUtil.doGet(url + "product/inNoListByVatno/" + vatDyeEncoder);
            if (json != null && !(json.equals(""))) {
                return ResponseEntity.status(200).body(json);
            }
            return ResponseEntity.status(400).body("获取失败，数据为空");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("获取失败" + e.getMessage());
        }
    }


    @GetMapping("/getListByInNo/{inNo}")
    public ResponseEntity getListByInNo(@PathVariable String inNo) {
        try {
            String json = HttpClientUtil.doGet(url + "product/in/filter/" + inNo);
            if (json != null && !(json.equals(""))) {
                return ResponseEntity.status(200).body(json);
            }
            return ResponseEntity.status(400).body("获取失败，数据为空");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("获取失败" + e.getMessage());
        }
    }


    /**
     * 保存打印信息
     *
     * @param type   入库10，出库20
     * @param object 所有打印的信息
     * @return
     */
    @PostMapping("/saveData/{type}/{sessionKey}")
    public ResponseEntity savePrintData(@PathVariable String type, @PathVariable String sessionKey, @RequestBody String object) {
        try {
            String json = HttpClientUtil.doPostJson(url + "product/print/" + type + "/" + sessionKey, object);
            Map<String,Object> jsons = gson.fromJson(json,Map.class);
            String result = jsons.get("result").toString();
            if ("0".equals(result)) {
                return ResponseEntity.status(400).body("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("保存失败");
        }
        return ResponseEntity.status(200).body("保存成功");
    }


    @GetMapping("/getValidateInOrder/{inOrder}")
    public ResponseEntity getValidateInOrder(@PathVariable String inOrder) {
        try {
            oldEpc.clear();
            validateWebResultList.clear();
            validateResultList.clear();
            String json = HttpClientUtil.doGet(url + "check/in/" + inOrder);
            Map map = gson.fromJson(json, Map.class);
            String dataStr = map.get("data").toString();
            Map data = gson.fromJson(dataStr, Map.class);
            List<Map> indLists = (List<Map>) data.get("indList");
            for (Map indList : indLists) {
                String epc = indList.get("epc").toString();
                String fab_roll = indList.get("fab_roll").toString();
                String weight_in = indList.get("weight_in").toString();
                String result = indList.get("result").toString();
                String in_no = indList.get("in_no").toString();

                ValidateResult validateResult = new ValidateResult();
                validateResult.setBizNo(in_no);
                validateResult.setCheckType("10");
                validateResult.setEpc(epc);
                validateResult.setResult(result);
                validateResultList.add(validateResult);

                ValidateWebResult validateWebResult = new ValidateWebResult();
                validateWebResult.setFab_roll(fab_roll);
                validateWebResult.setWeight_in(weight_in);
                validateWebResult.setResult(result);
                validateWebResult.setEpc(epc);
                validateWebResultList.add(validateWebResult);
                oldEpc.add(epc);
            }
            return ResponseEntity.status(200).body(validateWebResultList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("存在空指针或其他异常"+e.getMessage());
        }
    }

    public void validate(String newEpc) {

        for (ValidateWebResult validateWebResult : validateWebResultList) {
            if (validateWebResult.getEpc().equals(newEpc)) {
                validateWebResultList.remove(validateWebResult);
                validateWebResult.setResult("1");
                validateWebResultList.add(validateWebResult);
            } else if (!oldEpc.contains(newEpc)) {
                try {
                    String json = HttpClientUtil.doGet(url + "product/getProductInd/" + newEpc);
                    map = gson.fromJson(json, Map.class);
                    if ("2".equals(map.get("result").toString())) {
                        ValidateWebResult unknownResult = new ValidateWebResult();
                        unknownResult.setResult("3");
                        unknownResult.setEpc(newEpc);
                        unknownResult.setFab_roll("---");
                        unknownResult.setWeight_in("---");
                        unknownResult.setException("未知标签");
                        validateWebResultList.add(unknownResult);
                    } else {
                        String dataStr = map.get("data").toString();
                        getData = gson.fromJson(dataStr, Map.class);
                        String in_no = getData.get("in_no").toString();
                        String fab_roll = getData.get("fab_roll").toString();
                        String weight_in = getData.get("weight_in").toString();
                        String epc = getData.get("epc").toString();
                        ValidateWebResult newValidateWebResult = new ValidateWebResult();
                        newValidateWebResult.setResult("3");
                        newValidateWebResult.setEpc(epc);
                        newValidateWebResult.setWeight_in(weight_in);
                        newValidateWebResult.setFab_roll(fab_roll);
                        newValidateWebResult.setException(in_no);
                        validateWebResultList.add(newValidateWebResult);
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        sendMq();

        for (ValidateResult validateResult : validateResultList) {
            if (validateResult.getEpc().equals(newEpc)) {
                validateResultList.remove(validateResult);
                validateResult.setResult("1");
                validateResultList.add(validateResult);
            } else if (!oldEpc.contains(newEpc)) {
                try {
                    if ("2".equals(map.get("result").toString())) {
                        ValidateResult unknownResult = new ValidateResult();
                        unknownResult.setResult("3");
                        unknownResult.setEpc(newEpc);
                        unknownResult.setCheckType("10");
                        unknownResult.setBizNo(validateResult.getBizNo());
                        unknownResult.setReferBizNo("未知标签");
                        validateResultList.add(unknownResult);
                    } else {
                        String epc = getData.get("epc").toString();
                        String in_no = getData.get("in_no").toString();
                        ValidateResult newValidateResult = new ValidateResult();
                        newValidateResult.setResult("3");
                        newValidateResult.setEpc(epc);
                        newValidateResult.setBizNo(validateResult.getBizNo());
                        newValidateResult.setReferBizNo(in_no);
                        newValidateResult.setCheckType("10");
                        validateResultList.add(newValidateResult);
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    @GetMapping("/saveValidate/{sessionKey}")
    public ResponseEntity saveValidate(@PathVariable String sessionKey) {
        try {
            String validateResult = gson.toJson(validateResultList);
            HttpClientUtil.doPostJson(url + "check/save/" + sessionKey, validateResult);
            validateResultList.clear();
            validateWebResultList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(200).body("保存成功！");
    }


    private void sendMq() {
        futureConnection.publish(new UTF8Buffer("event/validate"), new UTF8Buffer(gson.toJson(validateWebResultList)), QoS.AT_LEAST_ONCE, false);
    }

    private void sendOutMq() {
        futureConnection.publish(new UTF8Buffer("event/validate"), new UTF8Buffer(gson.toJson(validateOutWebResultList)), QoS.AT_LEAST_ONCE, false);
    }
//
//    @GetMapping("/getValidateOutOrder/{outOrder}")
//    public ResponseEntity getValidateOutOrder(@PathVariable String outOrder) {
//
//        try {
//            String json = HttpClientUtil.doGet(url + "check/out/" + outOrder);
//            return ResponseEntity.status(200).body(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(400).build();
//        }
//    }


    @GetMapping("/getInOrderByEpc/{epc}")
    public ResponseEntity getInOrderByEpc(@PathVariable String epc) {
        try {
            String json = HttpClientUtil.doGet(url + "product/getProductInd/" + epc);
            return ResponseEntity.status(200).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("获取错误！");
        }
    }

    @PostMapping("/findInPrintHistory/{sessionId}")
    public ResponseEntity findInHistoryByPage(@PathVariable String sessionId, @RequestBody String data) {
        try {
            String json = HttpClientUtil.doPostJson(url + "history/print/in?sessionid=" + sessionId, data);
            return ResponseEntity.status(200).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("获取错误！");
        }
    }

    @PostMapping("/findInValidateHistory/{sessionId}")
    public ResponseEntity findInPrintHistory(@PathVariable String sessionId, @RequestBody String data) {
        try {
            String json = HttpClientUtil.doPostJson(url + "history/check/in?sessionid=" + sessionId, data);
            return ResponseEntity.status(200).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("获取错误！");
        }
    }


    //=============================出库校验=========================================================


    private List<ValidateOutWebResult> validateOutWebResultList = new CopyOnWriteArrayList<>();
    private List<ValidateOutResult> validateOutResultList = new CopyOnWriteArrayList<>();


    @GetMapping("/getValidateOutOrder/{outOrder}")
    public ResponseEntity getValidateOutOrder(@PathVariable String outOrder, @RequestParam(required = false) String vatDye) {
        try {
            oldEpc.clear();
            validateOutWebResultList.clear();
            validateOutResultList.clear();
            String json = HttpClientUtil.doGet(url + "product/out/" + outOrder + "?vatNo=" + vatDye);
            Map map = gson.fromJson(json, Map.class);
            String dataStr = map.get("data").toString();
            if(dataStr.equals("null")){
                return ResponseEntity.status(400).body("找不到此单号");
            }
            Map data = gson.fromJson(dataStr, Map.class);
            List<Map> productOutDLists = (List<Map>) data.get("productoutDList");
            for (Map productOutPList : productOutDLists) {
                String product_no = productOutPList.get("product_no").toString();
                String sel_color = productOutPList.get("sel_color").toString();
                String color_name = productOutPList.get("color_name").toString();
                String prod_name = productOutPList.get("prod_name").toString();
                String vat_no = productOutPList.get("vat_no").toString();
                String fab_roll = productOutPList.get("fab_roll").toString();
                String weight_out = productOutPList.get("weight_out").toString();
                String result = productOutPList.get("result").toString();
                String epc = productOutPList.get("epc").toString();
                String out_no = productOutPList.get("out_no").toString();
                String outp_id = productOutPList.get("outp_id").toString();

                ValidateOutResult validateOutResult = new ValidateOutResult();
                validateOutResult.setBizNo(out_no);
                validateOutResult.setProductNo(product_no);
                validateOutResult.setSelColor(sel_color);
                validateOutResult.setColorName(color_name);
                validateOutResult.setProdName(prod_name);
                validateOutResult.setVatNo(vat_no);
                validateOutResult.setFabRoll(fab_roll);
                validateOutResult.setWeightOut(weight_out);
                validateOutResult.setResult(result);
                validateOutResult.setEpc(epc);
                validateOutResult.setCheckType("20");
                validateOutResult.setOutpId(outp_id);
                validateOutResultList.add(validateOutResult);

                ValidateOutWebResult validateOutWebResult = new ValidateOutWebResult();
                validateOutWebResult.setProduct_no(product_no);
                validateOutWebResult.setSel_color(sel_color);
                validateOutWebResult.setColor_name(color_name);
                validateOutWebResult.setProd_name(prod_name);
                validateOutWebResult.setVat_no(vat_no);
                validateOutWebResult.setFab_roll(fab_roll);
                validateOutWebResult.setWeight_out(weight_out);
                validateOutWebResult.setResult(result);
                validateOutWebResult.setEpc(epc);
                validateOutWebResultList.add(validateOutWebResult);
                oldEpc.add(epc);
            }
            return ResponseEntity.status(200).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("WMS获取过来的某些值可能存在空指针！" + e.getMessage());
        }
    }

    public void validateOut(String newEpc) {

        for (ValidateOutWebResult validateOutWebResult : validateOutWebResultList) {
            if (validateOutWebResult.getEpc().equals(newEpc)) {
                validateOutWebResultList.remove(validateOutWebResult);
                validateOutWebResult.setResult("1");
                validateOutWebResultList.add(validateOutWebResult);
            } else if (!oldEpc.contains(newEpc)) {
                try {
                    String json = HttpClientUtil.doGet(url + "product/getProductOutd/" + newEpc);
                    map = gson.fromJson(json, Map.class);
                    if ("2".equals(map.get("result").toString())) {
                        ValidateOutWebResult unknownOutResult = new ValidateOutWebResult();
                        unknownOutResult.setProduct_no("---");
                        unknownOutResult.setSel_color("---");
                        unknownOutResult.setColor_name("---");
                        unknownOutResult.setProd_name("---");
                        unknownOutResult.setVat_no("---");
                        unknownOutResult.setFab_roll("---");
                        unknownOutResult.setWeight_out("---");
                        unknownOutResult.setResult("3");
                        unknownOutResult.setEpc(newEpc);
                        unknownOutResult.setException("未知标签");
                        validateOutWebResultList.add(unknownOutResult);
                    } else {
                        String dataStr = map.get("data").toString();
                        getData = gson.fromJson(dataStr, Map.class);
                        String product_no = getData.get("product_no").toString();
                        String sel_color = getData.get("sel_color").toString();
                        String color_name = getData.get("color_name").toString();
                        String prod_name = getData.get("prod_name").toString();
                        String vat_no = getData.get("vat_no").toString();
                        String fab_roll = getData.get("fab_roll").toString();
                        String weight_out = getData.get("weight_out").toString();
                        String out_no = getData.get("out_no").toString();
                        String epc = getData.get("epc").toString();
                        ValidateOutWebResult newValidateOutWebResult = new ValidateOutWebResult();
                        newValidateOutWebResult.setProduct_no(product_no);
                        newValidateOutWebResult.setSel_color(sel_color);
                        newValidateOutWebResult.setColor_name(color_name);
                        newValidateOutWebResult.setProd_name(prod_name);
                        newValidateOutWebResult.setVat_no(vat_no);
                        newValidateOutWebResult.setFab_roll(fab_roll);
                        newValidateOutWebResult.setWeight_out(weight_out);
                        newValidateOutWebResult.setResult("3");
                        newValidateOutWebResult.setEpc(epc);
                        newValidateOutWebResult.setException(out_no);

                        validateOutWebResultList.add(newValidateOutWebResult);
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        sendOutMq();

        for (ValidateOutResult validateOutResult : validateOutResultList) {
            if (validateOutResult.getEpc().equals(newEpc)) {
                validateOutResultList.remove(validateOutResult);
                validateOutResult.setResult("1");
                validateOutResultList.add(validateOutResult);
            } else if (!oldEpc.contains(newEpc)) {
                try {
                    if ("2".equals(map.get("result").toString())) {
                        ValidateOutResult unknownOutResult = new ValidateOutResult();
                        unknownOutResult.setBizNo(validateOutResult.getBizNo());
                        unknownOutResult.setResult("3");
                        unknownOutResult.setEpc(newEpc);
                        unknownOutResult.setCheckType("20");
                        unknownOutResult.setReferBizNo("未知标签");
                        validateOutResultList.add(unknownOutResult);
                    } else {
                        String product_no = getData.get("product_no").toString();
                        String sel_color = getData.get("sel_color").toString();
                        String color_name = getData.get("color_name").toString();
                        String prod_name = getData.get("prod_name").toString();
                        String vat_no = getData.get("vat_no").toString();
                        String fab_roll = getData.get("fab_roll").toString();
                        String weight_out = getData.get("weight_out").toString();
                        String out_no = getData.get("out_no").toString();
                        String epc = getData.get("epc").toString();
                        String outp_id = getData.get("outp_id").toString();

                        ValidateOutResult newValidateOutResult = new ValidateOutResult();
                        newValidateOutResult.setBizNo(out_no);
                        newValidateOutResult.setProductNo(product_no);
                        newValidateOutResult.setSelColor(sel_color);
                        newValidateOutResult.setColorName(color_name);
                        newValidateOutResult.setProdName(prod_name);
                        newValidateOutResult.setVatNo(vat_no);
                        newValidateOutResult.setFabRoll(fab_roll);
                        newValidateOutResult.setWeightOut(weight_out);
                        newValidateOutResult.setResult("3");
                        newValidateOutResult.setEpc(epc);
                        newValidateOutResult.setCheckType("20");
                        newValidateOutResult.setOutpId(outp_id);
                        validateOutResultList.add(newValidateOutResult);
                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }


    /**
     * 12、根据缸号查出库单的单号列表
     * 接口地址：http://192.168.1.116:8080/wms/product/outNoListByVatno/{vatNo}
     * 请求类型：get
     * 入参：vatNo（例如：D151011011）
     * 返回值(code=0表示查不到数据，1有数据)：
     * {
     * "code": "1",
     * "message": null,
     * "data": [
     * "A15103796 "
     * ]
     * }
     */

    @GetMapping("/getOutNoListByVatDye/{vatDye}")
    public ResponseEntity getOutNoListByVatDye(@PathVariable String vatDye) {
        try {
            String json = HttpClientUtil.doGet(url + "product/outNoListByVatno/" + vatDye);
            return ResponseEntity.status(200).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("根据缸号获取数据时发生异常！" + e.getMessage());
        }
    }


    /**
     * 7、保存校验接口
     * <p>
     * 地址：http://192.168.1.116:8080/wms/check/save/{sessionKey}
     * 入参：
     * 1、sessionKey
     * 2、校验结果json(bizNo:入库单号或出库单号、checkType:10入库20出库、result:校验的结果：1匹配，2校验时缺失，3校验时增加,4其他)
     * [{"epc":1111,"bizNo":2222,"checkType":10,"result":1}]
     * <p>
     * 返回值（json字符串,result=1成功，0失败）
     * {
     * "result": 1,
     * "msg": "",
     * "data": null
     * }
     *
     * @param sessionKey
     * @return
     */


    @GetMapping("/saveOutValidate/{sessionKey}")
    public ResponseEntity saveOutValidate(@PathVariable String sessionKey) {
        try {
            String validateOutResult = gson.toJson(validateOutResultList);
            String json = HttpClientUtil.doPostJson(url + "check/save/" + sessionKey, validateOutResult);
            validateOutResultList.clear();
            validateOutWebResultList.clear();
            return ResponseEntity.status(200).body("保存成功！" + json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("保存出库校验时异常" + e.getMessage());
        }

    }


    /**
     * 14、出库校验历史记录查询
     * http://192.168.1.116:8080/wms/history/check/out?sessionid={sessionid}
     * <p>
     * {
     * out_no,out_type,out_date,
     * vat_no,product_no,prod_name,
     * color_name,sel_color,color_code,qty_kg,
     * userName,createTime,cnt, normalCnt, lessCnt, moreCnt,
     * erpCnt
     * }
     */

    @PostMapping("/getOutValidateHistory/{sessionKey}")
    public ResponseEntity getOutValidateHistory(@PathVariable String sessionKey) {
        try {
            String json = HttpClientUtil.doPost(url + "history/check/out?sessionid=" + sessionKey);
            return ResponseEntity.status(200).body(json);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("查询出库校验历史时异常！" + e.getMessage());
        }
    }
}
