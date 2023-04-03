package com.alexmisko;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alexmisko.netty.WSServer;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner{

    @Value("${netty.port}")
    private Integer port;

    @Value("${netty.application.name}")
    private String name;

    @Value("${spring.cloud.nacos.discovery.ip}")
    private String ip;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    private String serverAddr;

    
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        registerNamingService();
        WSServer.getInstance().start();
    }

    private void registerNamingService() {
        try {
            NamingService namingService = NamingFactory.createNamingService(serverAddr);
            namingService.registerInstance(name, ip, port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
