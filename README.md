# Spring AI Bootcamp Demo – Ollama Chatbot

Spring Boot 4 chat app (REST API + small static UI, SQLite persistence) whose
replies come from a local model running in [Ollama](https://ollama.com) through Spring AI.

## Setup
1. Install Ollama from https://ollama.com and make sure it is running (it serves `http://localhost:11434`).
2. Pull a model: `ollama pull llama3.2` (about 2 GB; use `ollama list` to see the exact installed name).
3. `cp .env.example .env` and adjust `OLLAMA_MODEL` if you pulled a different model.
4. `./mvnw spring-boot:run`, then open http://localhost:8080.

No API key is needed.

## Configuration
| Variable | Default | Purpose |
|----------|---------|---------|
| `OLLAMA_BASE_URL` | `http://localhost:11434` | Where the Ollama server listens |
| `OLLAMA_MODEL` | `llama3.2` | Model name, must match `ollama list` exactly |
| `OPENROUTER_SYSTEM_PROMPT` | `You are a helpful, concise assistant.` | System prompt sent with every conversation |
| `OPENROUTER_MAX_HISTORY` | `20` | Number of latest messages sent to the model |
| `SQLITE_DB_PATH` | `chatbot.db` | SQLite database file |

The `openrouter.*` property names (and the `OpenRouterClient` class name) are
kept from the original project. They are provider-independent, so they work
unchanged with Ollama.

## What was implemented (the module TODOs)
| TODO | Where | Change |
|------|-------|--------|
| 1 | `pom.xml` | Spring AI BOM (`spring-ai.version` = 2.0.0) + `spring-ai-starter-model-ollama` |
| 2 | `application.yaml` | `spring.ai.ollama.*` (base URL and chat model from env vars) |
| 3 | `.env.example` | Documented env vars |
| 4 | `OpenRouterClient` | `ChatClient` built from the auto-configured builder with the configured system prompt |
| 5 | `OpenRouterClient.call` | `chatClient.prompt().messages(...).call().content()` with errors wrapped in `OpenRouterException` |
| 6 | `PromptMessageMapper` | Entity -> Spring AI `Message` conversion, kept out of the client |

## Extras
- `openrouter.max-history-messages`: only the latest N messages are sent to the model.
- Unit tests for the mapper; the `contextLoads` test uses an in-memory database and does not need Ollama running.

## Troubleshooting
- **502 from the API:** Ollama is not running, or `OLLAMA_MODEL` does not match a pulled model.
- **Startup fails with multiple chat model beans:** only one Spring AI model starter (Ollama) may be in `pom.xml`; remove the OpenAI one.
- **First reply is slow:** Ollama loads the model into memory on first use.
- Do not add `/v1` to `OLLAMA_BASE_URL`.
