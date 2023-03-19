package com.alexmisko.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;

@Component
public class UserChannelManager {
    public static Map<Long, Channel> userChannelGroup = new ConcurrentHashMap<>();
}


