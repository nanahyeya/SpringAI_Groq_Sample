package com.example.speingai.output;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

//public record Itinerary(List<Activity> itinerary) {
//}

public record Itinerary(
        @JsonProperty("itinerary")
        @JsonPropertyDescription("여행 일정 목록")
        List<Activity> itinerary
) {
}