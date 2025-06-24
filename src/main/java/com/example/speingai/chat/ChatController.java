package com.example.speingai.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .build();
    }

    @GetMapping("/api/chat")
    public String generate(@RequestParam(value = "message",
            defaultValue = "Spring AI는 무엇입니까?") String message) {
        //return chatClient.call(message);
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }


    @GetMapping("/api/chatmap")
    Map<String,String> chat(@RequestParam(defaultValue = "파이썬") String topic) {

        var response = chatClient
                .prompt()
                .user(u -> u.text("{topic}이란 무엇인지 말해 주세요.")
                        .param("topic", topic))
                .call()
                .content();
        return Map.of("answer", response);
    }

    @GetMapping("/api/chatresponse")
    public ChatResponse jokeWithResponse(
            @RequestParam(value = "message",
            defaultValue = "SpringBoot이란 무엇인지 말해 주세요.") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
    }

    @GetMapping("/api/chat-with-prompt")
    Map<String,String> chatWithPrompt(@RequestParam(value = "subject",
            defaultValue = "생성형 AI") String subject) {
        PromptTemplate promptTemplate = new PromptTemplate("{subject}란 무엇인가요? ");
        Prompt prompt = promptTemplate.create(Map.of("subject", subject));
        String answer = chatClient
                .prompt(prompt)
                .call()
                .content();
        return Map.of( "answer", answer);
    }

}
