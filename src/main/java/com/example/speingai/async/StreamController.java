package com.example.speingai.async;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@RestController
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/api/without-stream")
    public String withoutStream(@RequestParam(
            value = "message",
            defaultValue = "I'm visiting San Francisco next month, what are 10 places I must visit?") String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    // http --stream :8080/stream
    @GetMapping(value = "/api/stream")
    public Flux<String> stream(@RequestParam(
            value = "message",
            defaultValue = "I'm visiting Korea next month, what are 10 places I must visit?") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .map(content -> content + "\n")
                .delayElements(Duration.ofMillis(100));
    }

    //"text/event-stream;charset=UTF-8"
    @GetMapping(value = "/api/stream/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamSSE(@RequestParam(
            value = "message",
            defaultValue = "저는 한국을 방문할 예정인데, 꼭 가봐야 할 곳 10곳은 어디인가요?") String message) {
        return chatClient.prompt()
                .user(message)
                .stream()
                .content()
                .delayElements(Duration.ofMillis(100))
                .map(content -> ServerSentEvent.builder(content)
                        .event("message")
                        .build());
    }

}