package com.example.speingai.tools.weather;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    private final ChatClient chatClient;
    private final WeatherTools weatherTools;

    public WeatherController(ChatClient.Builder builder, WeatherTools weatherTools) {
        this.chatClient = builder.build();
        this.weatherTools = weatherTools;
    }

    /*
        날씨 예보 요청
        http://localhost:8080/api/tools/weather?message=What is the weather forecast for Los Angeles (34.0522, -118.2437)?

        기상 경보 요청
        http://localhost:8080/api/tools/weather?message=Are there any active weather alerts in California?

        자연스러운 질문
        http://localhost:8080/api/tools/weather?message=I'm planning to visit Seattle this weekend. What's the weather going to be like?
     */
    @GetMapping("/api/tools/weather")
    public String getAlerts(@RequestParam(defaultValue = "Weather alerts for CA?") String message) {
        return chatClient.prompt()
                .tools(weatherTools)
                .user(message)
                .call()
                .content();
    }
}
