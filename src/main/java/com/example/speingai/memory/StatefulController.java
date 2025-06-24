package com.example.speingai.memory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatefulController {

    private final ChatClient chatClient;

    public StatefulController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @GetMapping("/api/memory")
    public String home(@RequestParam(defaultValue = "마틴 파울러가 집필한 책은 어떤 것들이 있나요?") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

}
