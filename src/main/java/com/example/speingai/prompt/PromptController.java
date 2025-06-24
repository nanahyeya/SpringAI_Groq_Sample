package com.example.speingai.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PromptController {

    private final ChatClient chatClient;
    @Value("classpath:/prompts/youtube.st")
    private Resource ytPromptResource;

    public PromptController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/api/prompt-one")
    public String findPopularYouTubersStepOne(@RequestParam(value = "genre", defaultValue = "tech")
                                                  String genre) {
//        String message = """
//            List 10 of the most popular YouTubers in {genre} along with their current subscriber counts. If you don't know
//            the answer , just say "I don't know".
//            """;
        String message = """
                현재 구독자 수와 함께 {genre}에서 가장 인기 있는 YouTuber 10명을 나열하세요. 당신이 모른다면
                                                                 대답은 그냥 "모르겠어요"라고 말하세요.
            """;
        PromptTemplate promptTemplate = new PromptTemplate(message);
        Prompt prompt = promptTemplate.create(Map.of("genre",genre));
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/api/prompt-two")
    public String findPopularYouTubers(@RequestParam(value = "genre", defaultValue = "tech")
                                           String genre) {
        PromptTemplate promptTemplate = new PromptTemplate(ytPromptResource);
        Prompt prompt = promptTemplate.create(Map.of("genre", genre));
        return chatClient.prompt(prompt).call().content();
    }

    @GetMapping("/api/jokes")
    public String jokes() {
//        var user = new UserMessage("Tell me a dad joke");
//        var user = new UserMessage("아재개그를 하나 말해 주세요");
//        var user = new UserMessage("Tell me a very serious joke about the earth");
        var user = new UserMessage("지구에 관한 아주 진지한 농담 하나 말해 주세요");

//        var system = new SystemMessage("You primary function is to tell Dad Jokes. If someone asks you for any other type of joke please tell them you only know Dad Jokes");
        var system = new SystemMessage("당신의 주요 기능은 아재개그를 전하는 것입니다. 누군가가 다른 유형의 농담을 요청하면 아재개그만 알고 있다고 말해주세요.");

//        Prompt prompt = new Prompt(List.of(user));
        Prompt prompt = new Prompt(List.of(system, user));
        return chatClient.prompt(prompt).call().content();
    }


    @GetMapping("/api/translate")
    public String translate() {
//        var userMsg = """
//                모든 내용을 이해했다면 서비스 시작을 위해 "Okay, Let's roll!!" 이라고 답해주세요.
//                """;
        var systemMsg = """
                당신은 이제부터 영한 번역 서비스를 위한 번역 로봇입니다.
                사용자가 입력하는 영문에 대해 문장마다 끊어 영문을 출력하고 한 줄을 띄고 한글로 번역해 주세요.
                또한 영문은 굵음 처리를 하여 출력하고 한글은 소괄호 () 안에 넣어 출력해 주세요.
                예를 들어 "Hello! I am  a  student." 라는 문장이 입력 된다면 아래와 같이 출력해야 합니다.
                
                Hello!
                (안녕)
                I am a student.
                (나는 학생이야.)
                
                또 당신은 지금부터 한글로 어떠한 질문을 받아도 "영어 문장을 입력하세요!!" 이라고만 대답해야 합니다.
                그리고 한글로 질문자와 대화하지 말아야 합니다.
                """;
//        var userMsg = """
//                AI의 태스크를 수행하는 성능은 프롬프트의 퀄리티에 의해 크게 좌우됩니다.
//                """;

        var userMsg = """
                In the heart of a bustling city,there was a small,quaint bookstore. It was nestled between towering skyscrapers,
                a place where one could escape from the noise of the modern world.
                Its wooden door was always open for those who sought solace in the world of words.
                """;

        return chatClient.prompt()
                .system(systemMsg)
                .user(userMsg)
                .call()
                .content();
    }
}       