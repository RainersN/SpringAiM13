package com.accenture.springai_bootcamp_demo.client;

import com.accenture.springai_bootcamp_demo.entity.ChatMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;
import java.util.Objects;

/**
 * Converts persisted {@link ChatMessage} entities into Spring AI {@link Message}
 * objects. Keeps payload construction out of {@link OpenRouterClient}.
 */
final class PromptMessageMapper {

    private PromptMessageMapper() {
    }

    /**
     * Maps the last {@code maxMessages} entries of the history, oldest first.
     * SYSTEM entries are skipped: the system prompt is configured once on the
     * ChatClient and must not be duplicated.
     */
    static List<Message> toPromptMessages(List<ChatMessage> history, int maxMessages) {
        if (history == null || history.isEmpty()) {
            return List.of();
        }
        int from = Math.max(0, history.size() - maxMessages);
        return history.subList(from, history.size()).stream()
                .map(PromptMessageMapper::toMessage)
                .filter(Objects::nonNull)
                .toList();
    }

    private static Message toMessage(ChatMessage message) {
        return switch (message.getRole()) {
            case USER -> UserMessage.builder().text(message.getContent()).build();
            case ASSISTANT -> AssistantMessage.builder().content(message.getContent()).build();
            case SYSTEM -> null;
        };
    }
}
