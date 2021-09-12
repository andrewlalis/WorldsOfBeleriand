package nl.andrewl.worlds_of_beleriand.server.api.world.chat;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.ChatData;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.ChatPayload;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import nl.andrewl.worlds_of_beleriand.server.service.ChatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/world/chat")
@RequiredArgsConstructor
public class ChatController {
	private final ChatService chatService;

	@GetMapping
	public List<ChatData> getRecentChat(User user) {
		return this.chatService.getRecentPublicChats(user);
	}

	@PostMapping
	public ChatData addChat(User user, @RequestBody ChatPayload payload) {
		return this.chatService.addPublicChat(user, payload);
	}
}
