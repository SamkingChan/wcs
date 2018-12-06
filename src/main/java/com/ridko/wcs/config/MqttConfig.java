package com.ridko.wcs.config;

import lombok.extern.java.Log;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;

/**
 * MQTT 配置
 *
 * @author smitea
 * @since 2018-10-17
 */
@Log
@Configuration
public class MqttConfig {

  @Value("${mqtt.client}")
  private String client = "reader-web-server";

  @Value("${mqtt.host}")
  private String host = "localhost";

  @Value("${mqtt.port}")
  private int port = 1883;

  @Value("${mqtt.username}")
  private String username = "admin";

  @Value("${mqtt.password}")
  private String password = "admin";

  @Bean
  public MQTT mqtt() throws URISyntaxException {
    MQTT mqtt = new MQTT();
    mqtt.setClientId(client);
    mqtt.setHost(host, port);
    mqtt.setUserName(username);
    mqtt.setPassword(password);
    return mqtt;
  }

  /**
   * MQTT连接器(在创建该Bean的时候就已连接到ActiveMQ中),通过该连接器就可以实现消息的推送和订阅
   *
   * @param mqtt MQTT配置参数
   * @return MQTT连接器
   */
  @Bean
  public FutureConnection futureConnection(MQTT mqtt) {
    FutureConnection futureConnection = mqtt.futureConnection();
    futureConnection.connect().then(new Callback<Void>() {
      @Override
      public void onSuccess(Void aVoid) {
        log.info("MQTT 连接成功!");
      }

      @Override
      public void onFailure(Throwable throwable) {
        log.warning("MQTT 连接失败:!" + throwable.getMessage());
      }
    });
    return futureConnection;
  }
}
