package com.alexmisko.netty.enums;

public enum MsgTypeEnum{

    CONNECT(1, "第一次(或重连)初始化连接"),
	CHAT(2, "聊天消息"),	
	SIGNED(3, "消息签收"),
	KEEPALIVE(4, "客户端保持心跳"),
	FOLLOW(5, "关注");

    public final Integer type;
	public final String content;
	
	MsgTypeEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}