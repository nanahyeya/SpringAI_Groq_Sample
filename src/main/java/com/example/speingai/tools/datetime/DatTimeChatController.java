package com.example.speingai.tools.datetime;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//Tool 사용 활성화
//spring.ai.openai.chat.options.tools.enabled=true

@RestController
public class DatTimeChatController {

    private final ChatClient chatClient;

    public DatTimeChatController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("You are a helpful assistant with access to date and time tools. " +
                        "ALWAYS use the available tools to get current date/time information. " +
                        "Do not guess or calculate dates manually.")
                .build();
    }

    @GetMapping("/api/tools/datetime")
    public String tools(@RequestParam(defaultValue = "What day is tomorrow?") String question) {
        return chatClient.prompt()
                .user("Please use the available tools to answer this question: " + question)
                .tools(new DatTimeTools())
                .call()
                .content();
    }

    // 강제로 Tool 호출하는 버전
    @GetMapping("/api/tools/datetime/force")
    public String forceTools(@RequestParam(defaultValue = "What day is tomorrow?") String question) {
        return chatClient.prompt()
                .user("First, get the current date using the available tools, then answer: " + question)
                .tools(new DatTimeTools())
                .call()
                .content();
    }

    // 디버깅용 - Tool 직접 호출
    @GetMapping("/api/tools/datetime/debug")
    public String debugTools() {
        DatTimeTools tools = new DatTimeTools();
        StringBuilder result = new StringBuilder();
        result.append("=== Tool 직접 호출 결과 ===\n");
        result.append("Current DateTime: ").append(tools.getCurrentDateTime()).append("\n");
        result.append("Current Date: ").append(tools.getCurrentDateTime()).append("\n");
        result.append("Tomorrow Day: ").append(tools.getTomorrowDayOfWeek()).append("\n");
        return result.toString();
    }

    // 더 구체적인 지시
    @GetMapping("/api/tools/datetime/specific")
    public String specificTools() {
        return chatClient.prompt()
                .user("Use getTomorrowDayOfWeek() tool to tell me what day tomorrow is.")
                .tools(new DatTimeTools())
                .call()
                .content();
    }
}
