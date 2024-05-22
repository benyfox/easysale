package com.example.onlinemarketplace.service;

import com.example.onlinemarketplace.dto.request.AssistantRequest;
import com.example.onlinemarketplace.dto.response.AssistantResponse;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface AssistantService {
    void chatStream(AssistantRequest requestBody, HttpServletResponse response) throws IOException;
    void analyse(HttpServletResponse response, String username) throws IOException;
}
