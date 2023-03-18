package com.alexmisko;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alexmisko.netty.WSServer;

@SpringBootApplication
public class ChatApplication implements CommandLineRunner{
    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        WSServer.getInstance().start();
    }
}
