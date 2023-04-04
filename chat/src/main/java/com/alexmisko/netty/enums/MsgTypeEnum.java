package com.alexmisko.netty.enums;


public enum MsgTypeEnum{

	WORD("word", "文字消息"),	
	PHOTO("photo", "图片消息"),
	KEEPALIVE("keepalive", "客户端保持心跳");

    public final String type;
	public final String content;
	
	MsgTypeEnum(String type, String content){
		this.type = type;
		this.content = content;
	}
	
	public String getType() {
		return type;
	}  
}