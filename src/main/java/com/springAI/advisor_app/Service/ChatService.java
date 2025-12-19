package com.springAI.advisor_app.Service;

import reactor.core.publisher.Flux;

public interface ChatService {

    String chatTemplate(String query, String userId);

    Flux<String> streamChat(String query);
}
