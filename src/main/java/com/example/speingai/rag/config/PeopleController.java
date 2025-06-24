package com.example.speingai.rag.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class PeopleController {
    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    //private final ObjectMapper objectMapper;

    public PeopleController(ChatClient.Builder chatClientBuilder,
                            VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/api/rag/people")
    Person chatWithRag(@RequestParam(value = "name", defaultValue = "마틴") String name) {
        //유사도 검색
        List<Document> similarDocuments =
                vectorStore.similaritySearch(SearchRequest.builder().query(name).topK(2).build());
        //List<Document> => String
        String information = similarDocuments
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant.
                
                Use the following information to answer the question:
                {information}
                
                Important guidelines:
                - Only include experienceInYears if you can calculate it from actual career start information
                - If birth year is given but career start is unknown, do not guess the experience years
                - Focus on factual information from the provided text
                """);
        var systemMessage = systemPromptTemplate.createMessage(
                Map.of("information", information));

        var outputConverter = new BeanOutputConverter<>(Person.class);
        System.out.println("BeanOutputConverter 의 getJsonSchema()");
        System.out.println(outputConverter.getJsonSchema());
        System.out.println("BeanOutputConverter 의 getFormat()");
        System.out.println(outputConverter.getFormat());

        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
                Tell me about {name} as if current date is {current_date}.
                For experienceInYears, calculate from when they started their professional career, not from birth.
                If the exact start date is not available, use a reasonable estimate or set to 0.
                
                {format}
                """);

        Map<String, Object> model = Map.of("name", name,
                "current_date", LocalDate.now(),
                "format", outputConverter.getFormat());

        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());

        var prompt = new Prompt(List.of(systemMessage, userMessage));

        try {
            var response = chatClient.prompt(prompt).call().content();
            System.out.println("변환 전 원시 응답: " + response);
            Person person = outputConverter.convert(response);
            System.out.println("변환된 Person 객체: " + person);
            return person;
        } catch (Exception e) {
            System.out.println("변환 오류: " + e.getMessage());
            e.printStackTrace();
            return new Person(null, null, 0, List.of());
        }

    }


    @GetMapping("/api/rag/people/debug")
    public String debugResponse(@RequestParam(value = "name", defaultValue = "마틴") String name) {
        List<Document> similarDocuments =
                vectorStore.similaritySearch(SearchRequest.builder().query(name).topK(2).build());
        String information = similarDocuments
                .stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));

        var systemPromptTemplate = new SystemPromptTemplate("""
                You are a helpful assistant.
                
                Use the following information to answer the question:
                {information}
                
                Important guidelines:
                - Only include experienceInYears if you can calculate it from actual career start information
                - If birth year is given but career start is unknown, do not guess the experience years
                - Focus on factual information from the provided text
                """);

        var systemMessage = systemPromptTemplate.createMessage(
                Map.of("information", information));

        var outputConverter = new BeanOutputConverter<>(Person.class);
        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
                Tell me about {name} as if current date is {current_date}.
                For experienceInYears, calculate from when they started their professional career, not from birth.
                If the exact start date is not available, use a reasonable estimate or set to 0.
                {format}
                """);

        Map<String, Object> model = Map.of("name", name,
                "current_date", LocalDate.now(),
                "format", outputConverter.getFormat());
        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());
        var prompt = new Prompt(List.of(systemMessage, userMessage));

        // 실제 응답 확인
        var response = chatClient.prompt(prompt).call().content();

        System.out.println("=== RAW RESPONSE ===");
        System.out.println(response);
        System.out.println("=== END RESPONSE ===");

        return response;
    }

}

record Person(
        String name,
        String dateOfBirth,
        Integer experienceInYears,
        List<String> books) {

}
