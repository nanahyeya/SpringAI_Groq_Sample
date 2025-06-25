package com.example.speingai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ModelsController {

    private final ChatClient chatClient;

    public ModelsController(ChatClient.Builder builder, VectorStore vectorStore) {

//        this.chatClient = builder
//                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))
//                .build();

        SearchRequest searchRequest = new SearchRequest.Builder()
                .topK(5)
                .similarityThreshold(0.7)
                //.filterExpression("company == 'OpenAI'")
                .build();

        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(searchRequest)
                .build();

        this.chatClient = builder.defaultAdvisors(advisor).build();
    }

    @GetMapping("/api/rag/models")
    public Models faq(@RequestParam(
            value = "message",
            defaultValue = "Give me a list of all the models from OpenAI along with their context window.") String message) {

//        return chatClient.prompt()
//                .user(message)
//                .call()
//                .entity(Models.class);

        String enhancedPrompt = message +
                "\n\nPlease respond with a valid JSON object in this exact format:\n" +
                "{\n" +
                "  \"models\": [\n" +
                "    {\n" +
                "      \"company\": \"company_name\",\n" +
                "      \"model\": \"model_name\",\n" +
                "      \"context_window_size\": number\n" +
                "    }\n" +
                "  ]\n" +
                "}\n" +
                "Only return valid JSON, no additional text.";

        try {
            return chatClient.prompt()
                    .user(enhancedPrompt)
                    .call()
                    .entity(Models.class);
        } catch (Exception e) {
            // 파싱 실패 시 빈 결과 반환
            return new Models(List.of());
        }
    }


    // 디버깅용 엔드포인트
    @GetMapping("/api/rag/models/raw")
    public String getRawResponse(@RequestParam(
            value = "message",
            defaultValue = "List OpenAI models") String message) {

        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }



}
