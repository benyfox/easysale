package com.example.onlinemarketplace.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AssistantRequest {
    private List<ChatMessage> messages;
    private boolean isAnalyse;

    @Setter
    @Getter
    public static class ChatMessage {
        private String role;
        private String text;

    }
}
