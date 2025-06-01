package com.easystay.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 加入支援 Java 8 日期時間模組
        mapper.registerModule(new JavaTimeModule());

        // 不使用 timestamp 格式輸出日期
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 啟用 Default Typing，並且要用 LaissezFaireSubTypeValidator
        PolymorphicTypeValidator ptv = LaissezFaireSubTypeValidator.instance;
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        return mapper;
    }
}

