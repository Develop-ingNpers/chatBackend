package com.project.chat.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.chat.Repository.ChatRoomRepository;
import com.project.chat.Vo.ChatMessage;
import com.project.chat.Vo.ChatRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
//@RequestMapping("/chat")	
public class ChatSampleController {
	
	private final SimpMessageSendingOperations messagingTemplate;
	
	
	private final ChatRoomRepository chatRoomRepository;
	
	
	@GetMapping("/chat/room")
	public String sample() {
		return "chat/room";
	}
	
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		if(ChatMessage.MessageType.ENTER.equals(message.getType())) {
			message.setMessage(message.getSender()+"님이 입장하셨습니다.");
		}
		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
	} 
	
	@GetMapping("/chat/rooms")
	@ResponseBody
	public List<ChatRoom> room(){
		return chatRoomRepository.findAllRoom();
	}
	
	@PostMapping("/chat/room")
	@ResponseBody
	public ChatRoom createRoom(@RequestParam String name) {
		return chatRoomRepository.createChatRoom(name);
	}
	
	@GetMapping("/chat/room/enter/{roomId}")
	public String roomDetail(Model model, @PathVariable String roomId){
		model.addAttribute("roomId",roomId);
		return "chat/roomdetail";
	}
	
	
	@GetMapping("/chat/room/{roomId}")
	@ResponseBody
	public ChatRoom roomInfo(@PathVariable String roomId){
		return chatRoomRepository.findRoomById(roomId);
	}
	

}
