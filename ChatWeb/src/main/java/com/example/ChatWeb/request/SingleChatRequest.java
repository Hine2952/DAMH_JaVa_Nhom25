package com.example.ChatWeb.request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SingleChatRequest {
    private Integer userld;
}
