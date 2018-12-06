# zebra打印机代码说明
 + [com\ridko\wcs\domain\printer\Label](Label): 标签对象
 + [com\ridko\wcs\service\printer\impl\PrinterServerImpl](PrinterService):打印机服务层
 + [com\ridko\wcs\controller\printer\PrinterController](PrinterController):打印机控制层
 
### Label标签对象
 
1.描述说明
        
    封装了标签对象，通过get/set方法可以获取或设置参数，并把对象封装好放到打印控制层就能把相关信息生成zpl语言传入打印机执行打印。
    除了EPC必传，其他参数皆做了空值处理。
    在Controller层里通过前端传过来的Json对象自动映射相应字段。

2.参数说明：
 
          ticketNo：布票号
          weight：重量
          colorName：颜色
          clothName：布种名称
          clothType：布种
          vatDye：缸号
          colorNo：色号
          epc：EPC码 必传
 
### PrinterServerImpl 打印机服务实现

1.描述说明：

    封装了打印机的连接及打印方法。
    
2.参数说明：
     
        connection：打印机TCP连接对象
        ip：打印机的IP地址，通过@Value注入，可在application.properties文件里的zebra.ip中更改
        port：打印机端口，默认为9100，可在application.properties文件里的zebra.port中更改
    
3.方法说明：
        
        Boolean print(String zpl)，通过@Autowird注入PrinterSrevice即可使用该方法
        return 打印成功失败的Boolean值
        param 打印机zpl命令
        
   - 打印机zpl命令，详情见 [斑马打印机ZPL语言](https://www.zebra.com/us/en/support-downloads/knowledge-articles/zpl-command-information-and-details.html)
        
              
### PrinterController 打印机控制接口



1.描述说明：

    封装了打印机的控制器及http接口
    
2.参数说明：
     
        printerService：自动注入的打印机服务层对象
    
3.方法说明：
        
        ResponseEntity<String> printAll(@RequestBody List<Label> labels)
        return http返回 state、body
        param labels对象的Json数组映射
        
        此方法下定义了zpl语言，是根据仓库实际需求排版的标签，如需改变请参考斑马打印机ZPL语言说明        
    
   - 打印机zpl命令，详情见 [斑马打印机ZPL语言](https://www.zebra.com/us/en/support-downloads/knowledge-articles/zpl-command-information-and-details.html)




# zebra打印机Http接口文档

 + [/printer/print](print): 打印一张标签 （需求里暂时用不上）
 + [/printer/printAll](printAll): 打印多张标签
 
 ### 打印一张标签
 1.API：
    
        url:http://127.0.0.1:8080/printer/print
    
        method:POST
    
        content-type:application/json
    
        type:text
 
 
 2.JSON格式示例：
    
    
        {
            "ticketNo":"041",
            "weight":"24.9",
            "colorName":"17##深宝蓝",
            "clothName":"JQ6662##精绵斜纹拉架单卫衣",
            "clothType":"JQ6662        JQ176354",
            "vatDye":"A71016073",
            "colorNo":"C39461",
            "epc":"30350A5370 00411542249762"
        }
        
        
 3.参数说明：
  
  
         ticketNo：布票号
         weight：重量
         colorName：颜色
         clothName：布种名称
         clothType：布种
         vatDye：缸号
         colorNo：色号
         epc：EPC码 必传
        
  
 4.返回值：
 
    1.成功
            status:200
            message:打印成功
            cause：ok
          
    2.失败
            status:400
            message:EPC为空,Exception:null
            cause:epc值为必传，不能为空。16进制，即 epc%4==0 
            
            status:401
            message:布票号为：xxx 的标签EPC为空或格式错误
            cause:epc值为必传，不能为空。且epc%4==0，目前定长为24位16进制
            
            status:402
            message:打印失败,检查打印机是否连接或参数是否正确
            cause:上位机与打印机通讯失败，检查上位机与打印机网络连接是否正常。或打印机内部是否出错
            
            
            
            
 ## 打印多张标签
 
 ##### 注意：如果多张标签打印，打印到中间一张标签epc存在空值或格式（epc%4==0）不正确,后面的都不打印。所以要保证epc格式正确且不为空。否则要提供重新勾选打印
 
 1.API：
 
    
        url:http://127.0.0.1:8080/printer/printAll
    
        method:POST
    
        content-type:application/json
    
        type:text
        
 
 2.JSON格式示例：
    
    
     [
        {
            "ticketNo":"041",
             "weight":"24.9",
             "colorName":"17##深宝蓝",
             "clothName":"JQ6662##精绵斜纹拉架单卫衣",
             "clothType":"JQ6662        JQ176354",
             "vatDye":"A71016073",
             "colorNo":"C39461",
             "epc":"30350A537000411542249762"
        },
        {
            "ticketNo":"042",
             "weight":"25.9",
             "colorName":"18##翡翠绿",
             "clothName":"JQ6622##纺织深布",
             "clothType":"JQ6622        JQ176254",
             "vatDye":"A71016055",
             "colorNo":"C49475",
             "epc":"30350A537000421542249762"
        },
        {
             "ticketNo":"043",
             "weight":"26.9",
             "colorName":"19##中国红",
             "clothName":"JQ6633##中国红心系天下纺织裤袜鞋",
             "clothType":"JQ6633        JQ176233",
             "vatDye":"A71016033",
             "colorNo":"C59433",
             "epc":"30350A537000431542249762"
        }
      ]
        
        
 3.参数说明：
  
  
         ticketNo：布票号
         weight：重量
         colorName：颜色
         clothName：布种名称
         clothType：布种
         vatDye：缸号
         colorNo：色号
         epc：EPC码 必传
        
  
 4.返回值：
 
    1.成功
            status:200
            message:打印成功
            cause：ok
          
    2.失败
            status:400
            message:EPC为空,Exception:null
            cause:epc值为必传，不能为空。16进制，即 epc%4==0 
            
            status:401
            message:布票号为：xxx 的标签EPC为空或格式错误
            cause:epc值为必传，不能为空。且epc%4==0，目前定长为24位16进制
            
            status:402
            message:打印失败,检查打印机是否连接或参数是否正确
            cause:上位机与打印机通讯失败，检查上位机与打印机网络连接是否正常。或打印机内部是否出错
            