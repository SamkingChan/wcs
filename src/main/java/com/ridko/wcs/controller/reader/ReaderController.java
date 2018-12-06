package com.ridko.wcs.controller.reader;

import com.impinj.octane.*;
import com.ridko.wcs.controller.common.DataController;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author SexyChan
 * @Date 2018/11/23 16:31
 */

@RestController
@RequestMapping("/reader")
@CrossOrigin
public class ReaderController {

    @Autowired
    private DataController dataController;

    private static List<String> history = new CopyOnWriteArrayList<>();

    private ImpinjReader reader = new ImpinjReader();

    @Value("${impinj.ip}")
    private String hostname = "192.168.1.222";

    @GetMapping("/validate")
    public ResponseEntity validate(@RequestParam(required = false) String type) {
        try {

            if(reader.isConnected()){
                history.clear();
                reader.disconnect();
            }
            reader.connect(hostname);

            Settings settings = reader.queryDefaultSettings();

            ReportConfig report = settings.getReport();

            report.setIncludeAntennaPortNumber(true);

            report.setMode(ReportMode.Individual);

            // The reader can be set into various modes in which reader
            // dynamics are optimized for specific regions and environments.
            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
            // and continuously optimizes the reader's configuration
            settings.setReaderMode(ReaderMode.AutoSetDenseReader);

            // set some special settings for antenna 1
            AntennaConfigGroup antennas = settings.getAntennas();
            antennas.disableAll();
            antennas.enableById(new short[]{1});
            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
            antennas.getAntenna((short) 1).setTxPowerinDbm(20);
            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

            reader.applySettings(settings);

            reader.setTagReportListener(new TagReportListener() {
                @Override
                public void onTagReported(ImpinjReader impinjReader, TagReport tagReport) {
                    List<Tag> tagList = tagReport.getTags();
                    for (Tag tag : tagList) {
                        if (!history.contains(tag.getEpc().toHexString())) {
                            String epc = tag.getEpc().toHexString();
                            history.add(epc);
                            System.out.println(epc);
                            if("out".equals(type)){
                                dataController.validateOut(epc);
                            }else {
                                dataController.validate(epc);
                            }
                        }
                    }
                }
            });
            reader.start();
        } catch (OctaneSdkException ex) {
            System.out.println(ex.getMessage());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
        }
        return ResponseEntity.status(200).body("成功！");
    }



    @GetMapping("/close")
    public ResponseEntity close() {
        try {
            history.clear();
            reader.stop();
            reader.disconnect();
            return ResponseEntity.status(200).build();
        } catch (OctaneSdkException e) {
            e.printStackTrace();
            return ResponseEntity.status(200).build();
        }

    }
}
