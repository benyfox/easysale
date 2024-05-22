package com.example.onlinemarketplace.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserRequest {
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String email;

}
