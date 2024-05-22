package com.example.onlinemarketplace.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private HttpStatus status;
    private String message;
}