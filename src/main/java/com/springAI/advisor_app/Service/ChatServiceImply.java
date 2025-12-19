package com.springAI.advisor_app.Service;

import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;
import reactor.core.publisher.Flux;


@Service
public class ChatServiceImply implements ChatService {

    private ChatClient chatClient;

    @Value("classpath:/prompts/user-message.st")
    private Resource userMessage;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;

    public ChatServiceImply(ChatClient chatClient) {
        this.chatClient = chatClient;

    }

    @Override
    public String chatTemplate(String query, String userId ) {

        return this.chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, userId))
//                .advisors(new SimpleLoggerAdvisor())
                .system(system   ->
                        system.text(this.systemMessage))
                .user(user ->
                        user.text(this.userMessage).param("concept", query))
                .call()
                .content();
    }

    @Override
    public Flux<String> streamChat(String query) {
        return this.chatClient
                .prompt()
                .system(system -> system.text(this.systemMessage))
                .user(user ->  user.text(this.userMessage).param("concept", query))
                .stream()
                .content();
    }


}

