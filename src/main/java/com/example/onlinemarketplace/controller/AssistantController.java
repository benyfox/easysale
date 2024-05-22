package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.request.AssistantRequest;
import com.example.onlinemarketplace.service.AssistantService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/assistant")
public class AssistantController {
    private final AssistantService assistantService;

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    @PostMapping
    public void chatStream(@RequestBody AssistantRequest requestBody,
                           HttpServletResponse response,
                           @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        if (requestBody.isAnalyse()) assistantService.analyse(response, userDetails.getUsername());
        else assistantService.chatStream(requestBody, response);
    }
}

