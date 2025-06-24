package com.example.speingai.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("groq")
@Configuration
public class OllamaConfig {

    @Bean
    @Primary
    public ChatModel groqChatModel(@Value("${OPENAI_API_KEY}") String apiKey) {
        OpenAiApi openAiApi = OpenAiApi.builder()
                .baseUrl("https://api.groq.com/openai")
                .apiKey(apiKey)
                .build();

        OpenAiChatOptions chatOptions = new OpenAiChatOptions.Builder()
                .model("meta-llama/llama-4-scout-17b-16e-instruct")
                .build();

        return OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .defaultOptions(chatOptions)
                .build();
    }

    @Bean
    @Primary
    public EmbeddingModel customOllamaEmbeddingModel() {
        OllamaApi ollamaApi = new OllamaApi.Builder()
                .baseUrl("http://localhost:11434")
                .build();

        OllamaOptions options = OllamaOptions.builder()
                //.model("qwen2.5:1.5b")
                .model("bge-m3:latest")
                .build();

        //return new OllamaEmbeddingModel(ollamaApi, options);
        return OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }

}
