package org.panda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"org.panda"}, exclude = {DataSourceAutoConfiguration.class, KafkaAutoConfiguration.class})
public class StreamingServer {
    public static void main(String[] args) {
        SpringApplication.run(StreamingServer.class, args);
    }
}