package com.ridko.wcs.controller.printer;

import com.ridko.wcs.domain.printer.Label;
import com.ridko.wcs.service.printer.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author SexyChan
 * @Date 2018/11/22 10:31
 */
@RestController
@RequestMapping("/printer")
@CrossOrigin
public class PrinterController {

    @Autowired
    PrinterService printerService;

//    @PostMapping("/print")
//    public ResponseEntity print(@RequestBody Label label) {
//        try {
//
//            String epc = label.getEpc();
//            if (epc == null || epc.length() % 4 != 0) {
//                return ResponseEntity.status(401).body("布票号为：" + label.getTicketNo() + "的标签EPC为空或格式错误");
//            }
//        } catch (NullPointerException e) {
//            return ResponseEntity.status(400).body("EPC为空,Exception:" + e.getMessage());
//        }
//
//        try {
//
//            Boolean flag = printerService.print(label);
//
//            if (!flag) {
//                return ResponseEntity.status(402).body("打印失败,检查打印机是否连接或参数是否正确");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(402).body("打印失败,检查打印机是否连接或参数是否正确");
//        }
//        return ResponseEntity.status(200).body("打印成功");
//
//    }

    @PostMapping("/printAll")
    public ResponseEntity<String> printAll(@RequestBody List<Label> labels) {

        String zpl = "";
        for (Label label : labels) {
            try {
                String epc = label.getEpc();
                if (epc == null || epc.length() % 4 != 0) {
                    return ResponseEntity.status(401).body("布票号为：" + label.getTicketNo() + "的标签EPC为空或格式错误");
                }
            } catch (NullPointerException e) {
                return ResponseEntity.status(400).body("布票号为：" + label.getTicketNo() + "的标签EPC为空,Exception:" + e.getMessage());
            }
            //R110XI4   MSUNG24 FONTR
//            zpl += "^XA^LH84,27" +
//                    "^CI26^SEE:GB.DAT^CW1,E:MSUNG24.FNT" +
//                    "^MD10" +
//                    "^FT40,146^A1N,42,42^FD嘉^FS" +
//                    "^FT40,198^A1N,42,42^FD谦^FS" +
//                    "^FT40,250^A1N,42,42^FD纺^FS" +
//                    "^FT40,302^A1N,42,42^FD织^FS" +
//                    "^FO30,13^GB780,325,2^FS" +
//                    "^FO101,144^GB709,0,2^FS" +
//                    "^FO101,237^GB709,0,2^FS" +
//                    "^FO101,76^GB709,0,2^FS" +
//                    "^FO195,14^GB0,324,2^FS" +
//                    "^FO101,15^GB0,323,2^FS" +
//                    "^FT40,72^A1N,42,42^FDJQ^FS" +
//                    "^FT468,303^A1N,36,36^FD重量^FS" +
//                    "^FT468,212^A1N,36,36^FD色号^FS" +
//                    "^FT100,303^A1N,36,36^FD布种^FS" +
//                    "^FT100,211^A1N,36,36^FD颜色^FS" +
//                    "^FT100,126^A1N,36,36^FD缸号^FS" +
//                    "^FT115,61^A1N,42,42^FD"+label.getTicketNo()+"^FS" +
//                    "^FT573,302^A1N,50,50^FD"+label.getWeight()+"^FS" +
//                    "^FT573,212^A1N,50,50^FD"+label.getColorNo()+"^FS" +
//                    "^FT205,303^A1N,55,55^FD"+label.getClothType()+"^FS" +
//                    "^FT205,209^A1N,55,55^FD"+label.getColorName()+"^FS" +
//                    "^FT205,127^A1N,45,45^FD"+label.getVatDye()+"^FS" +
//                    "^FT205,63^A1N,36,36^FD"+label.getClothName()+"^FS" +
//                    "^FO468,144^GB0,194,2^FS" +
//                    "^FO562,144^GB0,193,2^FS" +
//                    "^FO712,237^GB0,100,2^FS" +
//                    "^FT720,304^A1N,50,50^FDKG^FS" +
//                    "^RFW,H,1,2,1^FD3400^FS" +
//                    "^RFW,H,2,12,1^FD"+label.getEpc()+"^FS" +
//                    "^PQ1,0,1,Y^XZ";
            // "^RZ19051905,E,L^FS" +

            //T410
            zpl += "^XA" +
                    "^MMT" +
                    "^PW980" +
                    "^LL0406" +
                    "^LS0" +
                    "^CI26^SEE:GB.DAT^CW1,E:FONTR.FNT" +
                    "^FT99,162^A1N,45,45^FH\\^FD嘉^FS" +
                    "^FT99,214^A1N,45,45^FH\\^FD谦^FS" +
                    "^FT99,266^A1N,45,45^FH\\^FD纺^FS" +
                    "^FT99,318^A1N,45,45^FH\\^FD织^FS" +
                    "^FO84,29^GB780,325,2^FS" +
                    "^FO766,264^GB0,89,2^FS" +
                    "^FO617,175^GB0,179,2^FS" +
                    "^FO155,174^GB709,0,2^FS" +
                    "^FO522,174^GB0,180,2^FS" +
                    "^FO155,31^GB0,324,2^FS" +
                    "^FO155,263^GB709,0,2^FS" +
                    "^FO250,30^GB0,325,2^FS" +
                    "^FT98,88^A1N,45,45^FH\\^FDJQ^FS" +
                    "^FT526,324^A1N,42,42^FH\\^FD重量^FS" +
                    "^FT526,236^A1N,42,42^FH\\^FD色号^FS" +
                    "^FT159,148^A1N,42,42^FH\\^FD缸号^FS" +
                    "^FT159,322^A1N,42,42^FH\\^FD布种^FS" +
                    "^FT158,232^A1N,42,42^FH\\^FD颜色^FS" +
                    "^FO155,92^GB709,0,2^FS" +
                    "^FT174,78^A1N,50,50^FH\\^FD" + label.getTicketNo() + "^FS" +
                    "^FT622,240^A1N,52,52^FH\\^FD" + label.getColorNo() + "^FS" +
                    "^FT254,328^A1N,48,48^FH\\^FD" + label.getClothType() + "^FS" +
                    "^FT621,327^A1N,52,52^FH\\^FD" + label.getWeight() + "^FS" +
                    "^FT254,151^A1N,52,52^FH\\^FD" + label.getVatDye() + "^FS" +
                    "^FT252,79^A1N,48,48^FH\\^FD" + label.getClothName() + "^FS" +
                    "^FT253,238^A1N,52,52^FH\\^FD" + label.getColorName() + "^FS" +
                    "^FT783,329^A1N,52,52^FH\\^FDKG^FS" +
                    "^RFW,H,1,2,1^FD3400^FS" +
                    "^RFW,H,2,12,1^FD" + label.getEpc() + "^FS" +
                    "^PQ1,0,1,Y^XZ";
        }
        try {
            Boolean flag = printerService.print(zpl);
            if (!flag) {
                return ResponseEntity.status(402).body("打印失败,检查打印机是否连接或参数是否正确");
            }
        } catch (Exception e) {
            return ResponseEntity.status(402).body("打印失败,检查打印机是否连接或参数是否正确");
        }
        return ResponseEntity.status(200).body("打印成功");
    }
}
