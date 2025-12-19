package com.springAI.advisor_app.config;

import com.springAI.advisor_app.advisor.TokenPrintAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AiConfig {

    @Bean
    public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){

        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(10)
                .build();

    }

    private Logger logger = LoggerFactory.getLogger(AiConfig.class);

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory){


        this.logger.info("ChatMemory Implementation Class: " + chatMemory.getClass().getName());

        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();

        return builder
                .defaultAdvisors(messageChatMemoryAdvisor ,new TokenPrintAdvisor(),  new SafeGuardAdvisor(List.of("games")))
                .defaultSystem("You are a helpful coding assistant and an expert programmer.\n" +
                        "Keep the answer concise but complete.\n")
                .defaultOptions(GoogleGenAiChatOptions.builder()
                        .model("gemini-2.5-flash")
                        .temperature(0.3)
                        .maxOutputTokens(2000)
                        .build())
                .build();


    }


}
