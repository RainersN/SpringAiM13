# Spring AI Bootcamp Demo – OpenRouter Chatbot

Spring Boot 4 chat app (REST API + small static UI, SQLite persistence) whose
replies come from a model on [OpenRouter](https://openrouter.ai) through Spring AI.

## Setup
1. `cp .env.example .env` and set `OPENROUTER_API_KEY`.
2. `./mvnw spring-boot:run`, then open http://localhost:8080.

## What was implemented (the module TODOs)
| TODO | Where | Change |
|------|-------|--------|
| 1 | `pom.xml` | Spring AI BOM (`spring-ai.version` = 2.0.0) + `spring-ai-starter-model-openai` |
| 2 | `application.yaml` | `spring.ai.openai.*` pointed at OpenRouter (`base-url` includes `/v1`), model/key from env vars |
| 3 | `.env.example` | Documented env vars |
| 4 | `OpenRouterClient` | `ChatClient` built from the auto-configured builder with the configured system prompt |
| 5 | `OpenRouterClient.call` | `chatClient.prompt().messages(...).call().content()` with errors wrapped in `OpenRouterException` |
| 6 | `PromptMessageMapper` | Entity -> Spring AI `Message` conversion, kept out of the client |

## Extras
- `openrouter.max-history-messages` (env `OPENROUTER_MAX_HISTORY`, default 20): only the latest N messages are sent to the model.
- Unit tests for the mapper; `contextLoads` test no longer needs a real API key or touches the on-disk DB.

## Notes
- Spring AI 2.x uses the OpenAI Java SDK, so the base URL must end in `/v1`.
- GPT-5-family models reject a `temperature` setting; none is set here.
