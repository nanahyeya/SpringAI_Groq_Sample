package com.example.speingai.rag;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Model(
        @JsonProperty("company")
        String company,
        @JsonProperty("model")
        String model,
        @JsonProperty("context_window_size")
        int contextWindowSize) {

}
