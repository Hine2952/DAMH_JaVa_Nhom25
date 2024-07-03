package com.example.ChatWeb.request;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SendmessageReques {
    private Integer userId;
    private Integer chatId;
    private String content;
}
