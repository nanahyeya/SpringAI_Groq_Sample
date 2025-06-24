package com.example.speingai.rag;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Models(
        @JsonProperty("models")
        List<Model> models) {
}
