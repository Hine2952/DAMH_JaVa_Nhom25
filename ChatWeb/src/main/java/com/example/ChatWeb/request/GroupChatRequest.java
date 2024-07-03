package com.example.ChatWeb.request;

import lombok.*;

import java.util.List;
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class GroupChatRequest {
    private List<Integer> userIds;
    private String chatname;
    private String chatimage;
}
