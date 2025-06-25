package com.example.speingai.rag.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class RagConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

    @Value("vectorstore.json")
    private String vectorStoreName;

    @Value("classpath:/data/models.json")
    private Resource models;

    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
        System.out.println(">>> EmbeddingModel = " + embeddingModel.getClass().getName());
        var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
        var vectorStoreFile = getVectorStoreFile();

        if (vectorStoreFile.exists()) {
            log.info("Vector Store File Exists,");
            simpleVectorStore.load(vectorStoreFile);
        } else {
            log.info("===> Vector Store File Does Not Exist, loading documents");
            TextReader textReader = new TextReader(models);
            textReader.getCustomMetadata().put("filename", "models.json");
            List<Document> documents = textReader.get();
            log.info("===> 스플릿팅 전 Document 갯수 = {} ", documents.size());

            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            log.info("===> 스플릿팅 후 Document 갯수 = {} ", splitDocuments.size());

            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
            log.info("===> Vector Store 저장완료 ");
        }
        return simpleVectorStore;
    }

//    @Bean
//    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
//        System.out.println("EmbeddingModel 클래스 = " + embeddingModel.getClass().getName());
//
//        var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
//        var vectorStoreFile = getVectorStoreFile();
//
//        if (vectorStoreFile.exists()) {
//            log.info("Loading existing vector store");
//            simpleVectorStore.load(vectorStoreFile);
//        } else {
//            log.info("Creating new vector store");
//            List<Document> documents = createDocumentsFromJson();
//            simpleVectorStore.add(documents);
//            simpleVectorStore.save(vectorStoreFile);
//        }
//        return simpleVectorStore;
//    }


    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }

//    private List<Document> createDocumentsFromJson() throws IOException {
//        List<Document> documents = new ArrayList<>();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        List<Map<String, Object>> modelList = objectMapper.readValue(
//                models.getInputStream(),
//                new TypeReference<List<Map<String, Object>>>() {}
//        );
//
//        for (Map<String, Object> modelData : modelList) {
//            String company = (String) modelData.get("company");
//            String model = (String) modelData.get("model");
//            Integer contextWindowSize = (Integer) modelData.get("context_window_size");
//
//            String content = String.format(
//                    "Company: %s, Model: %s, Context Window: %d tokens",
//                    company, model, contextWindowSize
//            );
//
//            documents.add(new Document(content));
//        }
//
//        return documents;
//    }


}
