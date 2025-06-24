package com.example.speingai.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

//public record Activity(String activity, String location, String day, String time) {
//}

public record Activity(
        @JsonProperty("activity")
        @JsonPropertyDescription("활동명 또는 관광지명")
        String activity,

        @JsonProperty("location")
        @JsonPropertyDescription("구체적인 위치나 주소")
        String location,

        @JsonProperty("day")
        @JsonPropertyDescription("날짜 (예: Day 1, 1일차)")
        String day,

        @JsonProperty("time")
        @JsonPropertyDescription("시간 (예: 09:00-11:00)")
        String time
) {
}