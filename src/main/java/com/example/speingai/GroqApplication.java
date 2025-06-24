package com.example.speingai;

import org.springframework.ai.model.ollama.autoconfigure.OllamaChatAutoConfiguration;
import org.springframework.ai.model.ollama.autoconfigure.OllamaEmbeddingAutoConfiguration;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/*
  Groq 무료 Model을 사용할때
  : Chat은 OllamaChatAutoConfiguration 사용하지 않음, 대신 OpenAiChatAutoConfiguration 사용함
  : Embedding은 OpenAiEmbeddingAutoConfiguration  사용하지 않음, 대신 OllamaEmbeddingAutoConfiguration 사용함
*/
@SpringBootApplication(exclude = {
		OllamaChatAutoConfiguration.class,
		OpenAiEmbeddingAutoConfiguration.class
})

/*
  ChatGPT 유료 Model을 사용할때
  : Chat은 OllamaChatAutoConfiguration 사용하지 않음, 대신 OpenAiChatAutoConfiguration 사용함
  : Embedding은 OllamaEmbeddingAutoConfiguration  사용하지 않음, 대신  OpenAiEmbeddingAutoConfiguration 사용함
*/

//@SpringBootApplication(exclude = {
//		OllamaChatAutoConfiguration.class,
//		OllamaEmbeddingAutoConfiguration.class
//})
public class GroqApplication {
	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication.run(GroqApplication.class, args);
	}

	@Bean
	public String printApiKey() {
		String api_key = env.getProperty("spring.ai.openai.api-key");
		String model = env.getProperty("spring.ai.openai.chat.options.model");

		if(api_key != null){
			System.out.println("API_KEY = " + api_key.substring(0,10));
			System.out.println("Model Name = " + model);
		}
		return "";
	}

}
