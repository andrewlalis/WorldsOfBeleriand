package nl.andrewl.worlds_of_beleriand.server.service;

import lombok.RequiredArgsConstructor;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.ChatData;
import nl.andrewl.worlds_of_beleriand.server.api.user.dto.ChatPayload;
import nl.andrewl.worlds_of_beleriand.server.dao.LocationRepository;
import nl.andrewl.worlds_of_beleriand.server.dao.PublicChatRepository;
import nl.andrewl.worlds_of_beleriand.server.model.user.PublicChat;
import nl.andrewl.worlds_of_beleriand.server.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
	private final LocationRepository locationRepository;
	private final PublicChatRepository publicChatRepository;

	@Transactional(readOnly = true)
	public List<ChatData> getRecentPublicChats(User user) {
		var location = this.locationRepository.findByUsersContaining(user)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User is not at a known location."));
		return this.publicChatRepository.findAllByLocationOrderByCreatedAtDesc(location, Pageable.ofSize(25))
				.map(chat -> new ChatData(
						chat.getId(),
						chat.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
						chat.getAuthor().getName(),
						chat.getMessage()
				))
				.toList();
	}

	@Transactional
	public ChatData addPublicChat(User user, ChatPayload payload) {
		var location = this.locationRepository.findByUsersContaining(user)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User is not at a known location."));
		var chat = this.publicChatRepository.save(new PublicChat(user, payload.message(), location));
		return new ChatData(chat.getId(), chat.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), user.getName(), chat.getMessage());
	}
}
