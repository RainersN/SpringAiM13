package com.accenture.springai_bootcamp_demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Application-level settings for the OpenRouter integration. Connection
 * details (base URL, API key, model) live under {@code spring.ai.openai.*}.
 *
 * @param systemPrompt       instructions prepended to every conversation
 * @param maxHistoryMessages how many of the most recent messages are sent to the model
 */
@ConfigurationProperties(prefix = "openrouter")
public record OpenRouterProperties(String systemPrompt, Integer maxHistoryMessages) {

    static final String DEFAULT_SYSTEM_PROMPT = "You are a helpful, concise assistant.";
    static final int DEFAULT_MAX_HISTORY = 20;

    public OpenRouterProperties {
        if (systemPrompt == null || systemPrompt.isBlank()) {
            systemPrompt = DEFAULT_SYSTEM_PROMPT;
        }
        if (maxHistoryMessages == null || maxHistoryMessages < 1) {
            maxHistoryMessages = DEFAULT_MAX_HISTORY;
        }
    }
}
