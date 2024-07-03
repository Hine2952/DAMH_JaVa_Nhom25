package com.example.ChatWeb.request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    private String fullname;
    private String profilePic;
}

