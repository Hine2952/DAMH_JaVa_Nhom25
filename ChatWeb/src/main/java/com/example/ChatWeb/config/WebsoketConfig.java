package com.example.ChatWeb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebsoketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void  registerStompEndpoints(StompEndpointRegistry registery){
        registery.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
    }
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registery){
        registery.setApplicationDestinationPrefixes("/app");
        registery.enableSimpleBroker("group", "/user");
        registery.setUserDestinationPrefix("/user");
    }

}
