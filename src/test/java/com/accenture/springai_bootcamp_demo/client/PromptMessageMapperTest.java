package com.accenture.springai_bootcamp_demo.client;

import com.accenture.springai_bootcamp_demo.entity.ChatMessage;
import com.accenture.springai_bootcamp_demo.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PromptMessageMapperTest {

    @Test
    void mapsRolesInOrderAndSkipsSystem() {
        List<ChatMessage> history = List.of(
                ChatMessage.of(Role.SYSTEM, "ignored"),
                ChatMessage.of(Role.USER, "hi"),
                ChatMessage.of(Role.ASSISTANT, "hello"),
                ChatMessage.of(Role.USER, "bye"));

        List<Message> result = PromptMessageMapper.toPromptMessages(history, 10);

        assertThat(result).hasSize(3);
        assertThat(result.get(0)).isInstanceOf(UserMessage.class);
        assertThat(result.get(1)).isInstanceOf(AssistantMessage.class);
        assertThat(result.get(2).getText()).isEqualTo("bye");
    }

    @Test
    void keepsOnlyMostRecentMessages() {
        List<ChatMessage> history = List.of(
                ChatMessage.of(Role.USER, "one"),
                ChatMessage.of(Role.ASSISTANT, "two"),
                ChatMessage.of(Role.USER, "three"));

        List<Message> result = PromptMessageMapper.toPromptMessages(history, 2);

        assertThat(result).extracting(Message::getText).containsExactly("two", "three");
    }

    @Test
    void emptyHistoryGivesEmptyList() {
        assertThat(PromptMessageMapper.toPromptMessages(List.of(), 5)).isEmpty();
    }
}
