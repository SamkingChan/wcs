//package com.ridko.wcs.utils;
//
//import com.impinj.octane.*;
//import org.fusesource.hawtbuf.UTF8Buffer;
//import org.fusesource.mqtt.client.FutureConnection;
//import org.fusesource.mqtt.client.QoS;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
// * @Author SexyChan
// * @Date 2018/11/23 16:39
// */
//public class Reader {
//
//    @Autowired
//    private FutureConnection futureConnection;
//
//    private static List<String> history = new CopyOnWriteArrayList<>();
//
//    private ImpinjReader reader = new ImpinjReader();
//
//    @Value("${impinj.ip}")
//    private String hostname = "192.168.1.221";
//
//    public void start() {
//        try {
//
//            reader.connect(hostname);
//
//            Settings settings = reader.queryDefaultSettings();
//
//            ReportConfig report = settings.getReport();
//
//            report.setIncludeAntennaPortNumber(true);
//
//            report.setMode(ReportMode.Individual);
//
//            // The reader can be set into various modes in which reader
//            // dynamics are optimized for specific regions and environments.
//            // The following mode, AutoSetDenseReader, monitors RF noise and interference and then automatically
//            // and continuously optimizes the reader's configuration
//            settings.setReaderMode(ReaderMode.AutoSetDenseReader);
//
//            // set some special settings for antenna 1
//            AntennaConfigGroup antennas = settings.getAntennas();
//            antennas.disableAll();
//            antennas.enableById(new short[]{1});
//            antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
//            antennas.getAntenna((short) 1).setIsMaxTxPower(false);
//            antennas.getAntenna((short) 1).setTxPowerinDbm(10);
//            antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);
//
//            reader.applySettings(settings);
//            reader.setTagReportListener(new TagReportListener() {
//                @Override
//                public void onTagReported(ImpinjReader impinjReader, TagReport tagReport) {
//                    List<Tag> tagList = tagReport.getTags();
//                    for (Tag tag : tagList) {
//                        if (!history.contains(tag.getEpc().toHexString())) {
//                            history.add(tag.getEpc().toHexString());
//                            futureConnection.publish(new UTF8Buffer("event/reader"),new UTF8Buffer(tag.getEpc().toHexString()), QoS.AT_LEAST_ONCE,false);
//                        }
//                    }
//                }
//            });
//            reader.start();
//        } catch (OctaneSdkException ex) {
//            System.out.println(ex.getMessage());
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace(System.out);
//        }
//    }
//
//    public List<String> getEpc() {
//        return history;
//    }
//
//    public void close() {
//        try {
//            reader.stop();
//            reader.disconnect();
//        } catch (OctaneSdkException e) {
//            e.printStackTrace();
//        }
//    }
//}
