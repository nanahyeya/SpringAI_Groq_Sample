package com.example.speingai.prompt;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArticleController {

    private final ChatClient chatClient;

    public ArticleController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/api/posts/new")
    public String newPost(@RequestParam(value = "topic", defaultValue = "JDK Virtual Threads")
                              String topic) {

        // A system message in LLMs is a special type of input that provides high-level instructions, context, or behavioral
        // guidelines to the model before it processes user queries. Think of it as the "behind-the-scenes"
        // instructions that shape how the AI should respond.
        //
        // Use it as a guide or a restriction to the model's behavior
        var system = """
                Blog Post Generator Guidelines:
                
                1. Length & Purpose: Generate 500-word blog posts that inform and engage general audiences.
                
                2. Structure:
                   - Introduction: Hook readers and establish the topic's relevance
                   - Body: Develop 3 main points with supporting evidence and examples
                   - Conclusion: Summarize key takeaways and include a call-to-action
                
                3. Content Requirements:
                   - Include real-world applications or case studies
                   - Incorporate relevant statistics or data points when appropriate
                   - Explain benefits/implications clearly for non-experts
                
                4. Tone & Style:
                   - Write in an informative yet conversational voice
                   - Use accessible language while maintaining authority
                   - Break up text with subheadings and short paragraphs
                
                5. Response Format: Deliver complete, ready-to-publish posts with a suggested title.
                """;

        var hangul_system_msg = """
            블로그 게시물 생성 지침:
            
            1. 길이 및 목적: 일반 독자에게 정보를 제공하고 참여를 유도하는 500단어 분량의 블로그 게시물을 작성하세요.
            
            2. 구성:
            - 서론: 독자의 관심을 끌고 주제의 관련성을 확립하세요.
            - 본문: 뒷받침하는 증거와 사례를 통해 3가지 주요 요점을 제시하세요.
            - 결론: 핵심 요점을 요약하고 행동 촉구를 포함하세요.
            
            3. 콘텐츠 요구 사항:
            - 실제 적용 사례 또는 사례 연구를 포함하세요.
            - 적절한 경우 관련 통계 또는 데이터 포인트를 포함하세요.
            - 비전문가를 위해 이점/영향을 명확하게 설명하세요.
            
            4. 어조 및 스타일:
            - 정보를 제공하면서도 대화체로 작성하세요.
            - 권위를 유지하면서 접근하기 쉬운 언어를 사용하세요.
            - 부제목과 짧은 단락으로 텍스트를 구분하세요.
            
            5. 답변 형식: 제안된 제목과 함께 게시 준비가 완료된 완성된 게시물을 제공하세요.
            """;

        return chatClient.prompt()
                //.system(system)
                .system(hangul_system_msg)
                .user(u -> {
                    u.text("{topic}에 대한 블로그 게시물을 작성해주세요. 한국어로 작성해 주세요.");
                    u.param("topic",topic);
                })
                .call()
                .content();
    }

}
