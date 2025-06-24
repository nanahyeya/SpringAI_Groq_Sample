package com.example.speingai.output;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VacationPlan {

    private final ChatClient chatClient;

    public VacationPlan(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/api/vacation/unstructured")
    public String vacationUnstructured() {
        return chatClient.prompt()
                //.user("What's a good vacation plan while I'm in Montreal CA for 4 days?")
                .user("프랑스 파리에 4일간 머물 때 좋은 휴가 계획은 무엇인가요?")
                .call()
                .content();
    }

//    @GetMapping("/vacation/structured")
//    public Itinerary vacationStructured(@RequestParam(value = "destination", defaultValue = "Cleveland, OH") String destination) {
//        return chatClient.prompt()
//                .user(u -> {
//                    //u.text("What's a good vacation plan while I'm in {destination} for 3 days?");
//                    u.text("{destination}에 4일간 머물 때 좋은 휴가 계획은 무엇인가요??");
//                    u.param("destination", destination);
//                })
//                .call()
//                .entity(Itinerary.class);
//    }

    @GetMapping("/api/vacation/structured")
    public Itinerary vacationStructured(@RequestParam(value = "destination", defaultValue = "파리, 프랑스")
                                            String destination) {
        return chatClient.prompt()
                .system("""
                    당신은 여행 계획 전문가입니다.
                    사용자가 요청한 목적지에 대해 4일간의 상세한 여행 일정을 작성해주세요.
                    각 활동에는 구체적인 장소, 시간, 날짜를 포함해야 합니다.
                    JSON 형태로 응답하되, 다음 구조를 따라주세요:
                    {
                        "itinerary": [
                            {
                                "activity": "활동명",
                                "location": "구체적 위치",
                                "day": "Day 1",
                                "time": "09:00-11:00"
                            }
                        ]
                    }
                    """)
                .user(u -> {
                    u.text("""
                        {destination}에서 4일간 머물 예정입니다.
                        하루 3-4개의 활동을 포함한 상세한 여행 일정을 작성해주세요.
                        각 활동마다 다음 정보를 포함해주세요:
                        - 활동명 또는 관광지명
                        - 구체적인 위치나 주소
                        - 일차 (Day 1, Day 2, Day 3, Day 4)
                        - 대략적인 시간대
                        """);
                    u.param("destination", destination);
                })
                .call()
                .entity(Itinerary.class);
    }

}
