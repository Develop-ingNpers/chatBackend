package com.project.chat.Vo;

import java.util.UUID;

import lombok.Data;

@Data
public class ChatRoom {
	private String roomId;
	private String name;
	
	public static ChatRoom create(String name) {
		ChatRoom chatRoom = new ChatRoom();
		chatRoom.roomId = UUID.randomUUID().toString();
		chatRoom.name = name;
		return chatRoom;
	}

}
