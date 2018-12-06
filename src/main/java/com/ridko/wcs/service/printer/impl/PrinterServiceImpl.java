package com.ridko.wcs.service.printer.impl;

import com.ridko.wcs.service.printer.PrinterService;
import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @Author SexyChan
 * @Date 2018/11/22 09:19
 */
@Service
public class PrinterServiceImpl implements PrinterService {

    private Connection connection;

    @Value("${zebra.ip}")
    private String ip = "192.168.1.251";

    @Value("${zebra.port}")
    private int port = 9100;

    @Override
    public Boolean print(String zpl) {
        try {

            // 创建打印机连接
            connection = new TcpConnection(ip, port);
            byte[] content = zpl.getBytes("GB18030");
            connection.open();
            connection.write(content);
            connection.close();
            return true;
        } catch (Exception e) {
            try {
                connection.close();
            } catch (ConnectionException ce) {
                ce.printStackTrace();
            }
            e.printStackTrace();
            return false;
        }
    }
}
